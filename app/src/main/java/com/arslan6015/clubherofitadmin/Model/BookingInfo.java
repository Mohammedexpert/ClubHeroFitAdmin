package com.arslan6015.clubherofitadmin.Model;

public class BookingInfo {
    private String email,fullName,id,image;

    public BookingInfo(String email, String fullName, String id, String image) {
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.image = image;
    }

    public BookingInfo() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
