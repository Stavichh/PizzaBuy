package com.example.pizzabuy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HelloController {

    @FXML
    private Label ADDRESS;

    @FXML
    private ComboBox<Courier> COURIERBOX;

    @FXML
    private Label ISPAID;

    @FXML
    private Button ORDER_INFO;

    @FXML
    private Label ORDERPRICE;

    @FXML
    private Button PEND;

    @FXML
    private Button Update;

    @FXML
    private Label WHOLEPRICE;




    //for SQL Server variables
    String url = "jdbc:mysql://localhost/pizzabuy";
    String user = "root";
    String password = "toor";
    ArrayList<Courier> couriers = new ArrayList<>();


//переход на окно иформации заказов
    @FXML
    void onOrder(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Order_Info.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene( new Scene(root));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //ввод данных в базу
    @FXML
  public void ONPEND(ActionEvent event) {

        pendChanges();
    }

    //следующий заказ без курьера
    @FXML
    void onUpdate(ActionEvent event) {

        initialize();
    }

// вывод данных из базы в окно оператора
public void initialize(){
    try {
        COURIERBOX.getItems().clear();
        couriers.clear();
        Connection con = DriverManager.getConnection(url, user, password);
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM pizzabuy.orders Where Courier = 'no'");
        rs.next();

        ADDRESS.setText(rs.getString(4));
        ISPAID.setText(rs.getString(3));
        WHOLEPRICE.setText(rs.getString(2));

        rs = statement.executeQuery("SELECT * FROM pizzabuy.courier");
        while(rs.next()) {
            couriers.add(new Courier(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        COURIERBOX.getItems().addAll(couriers);


    } catch (SQLException e) {
        e.printStackTrace();
        ADDRESS.setText("нет заказов");
        ISPAID.setText("Закзов Нет");
        WHOLEPRICE.setText("0.0");
    }



}
//ввод в базу курьера и изменение статусов
public void pendChanges(){
    try {
        String search = ADDRESS.getText().toString();
        Connection con = DriverManager.getConnection(url, user, password);
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM pizzabuy.orders WHERE Address = '"+search+"';");
        rs.next();
        String courier = COURIERBOX.getValue().toString();

        //срезаем статус у курьера чтобы после добавить в базу
        if (courier.endsWith("ready")){
            courier.replace("ready", " ");
        } else if (courier.endsWith("enRoute")){
            courier.replace("enRoute", " ");
        } else if (courier.endsWith("returning")){
            courier.replace("returning", " ");
        } else if (courier.endsWith("free")){
            courier.replace("free", " ");
        }

       statement.execute("update pizzabuy.orders Set Courier = '"+courier+"' Where Address = '"+search+"';");

        //таймер для готовки заказов
        Timer timer = new Timer();

        // 1 = Name, 2 = Surname, 3 = ready
        String[] man = courier.split(" ");

        //меняем статус на 'baking' у заказа
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //меняем статус заказа с "pending" на "baking"
                try {

                            statement.execute("update pizzabuy.orders set Status = 'baking' Where Status not in ('ready', 'baking') and Courier not in ('no');");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("теперь baking, а не pending");
            }
        }, 0,10*60*1000);

        //меняем статус на 'ready' у заказа и 'enRoute' у курьера у этого заказа
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                try {

                   statement.execute("update pizzabuy.orders set Status = 'ready' Where Status in ('baking') and Courier not in ('no');");
                   statement.execute("update pizzabuy.courier set ready = 'enRoute' where ready not in ('enRoute') and Name in ('"+man[0]+"') and Surname in ('"+man[2]+"')");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.println("теперь ready а не baking и курьер теперь enRoute а не free");
            }
        },10*60*1000,10*60*500);

        //меняем статус курьера с "enRoute", на "returning" а статус заказа с "ready" на "done"
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                try {

                    statement.execute("update pizzabuy.orders set Status = 'done' Where Status in ('ready') and Courier in ('"+courier+"');");
                    statement.execute("update pizzabuy.courier set ready = 'returning' where ready not in ('returning') and Name in ('"+man[0]+"') and Surname in ('"+man[2]+"')");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.println("теперь done а не ready и курьер теперь returning а не enRoute");
            }
        },10*60*2000,10*60*250);

        //меняем статус курьера с "enRoute" на "free"
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                try {
                    statement.execute("update pizzabuy.courier set ready = 'free' where ready not in ('free') and Name in ('"+man[0]+"') and Surname in ('"+man[2]+"')");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.println("теперь курьер free а не returning");
            }
        },10*60*3000,10*60*250);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}




}