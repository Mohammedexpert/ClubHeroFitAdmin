package com.arslan6015.clubherofitadmin.Model;

public class NewsList {
    private String id,newsTitle,newsDesp,uploadTime;

    public NewsList(String id, String newsTitle, String newsDesp, String uploadTime) {
        this.id = id;
        this.newsTitle = newsTitle;
        this.newsDesp = newsDesp;
        this.uploadTime = uploadTime;
    }

    public NewsList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDesp() {
        return newsDesp;
    }

    public void setNewsDesp(String newsDesp) {
        this.newsDesp = newsDesp;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}
