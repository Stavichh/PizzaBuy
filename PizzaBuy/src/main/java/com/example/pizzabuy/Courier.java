package com.example.pizzabuy;

public class Courier {
    int id;
    String name ;
    String Sruname;
    String ready;

    public Courier() {
    }

    public Courier(int id, String name, String sruname, String ready) {
        this.id = id;
        this.name = name;
        Sruname = sruname;
        this.ready = ready;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSruname() {
        return Sruname;
    }

    public void setSruname(String sruname) {
        Sruname = sruname;
    }

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return this.name + " " + this.Sruname + " " + this.ready;

    }
}
