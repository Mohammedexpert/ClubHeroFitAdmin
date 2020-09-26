package com.arslan6015.clubherofitadmin.Model;

public class ClassesList {
    private String id,name,time;


    public ClassesList() {
    }

    public ClassesList(String id, String name, String time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
