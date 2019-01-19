/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author Xuan Truong
 */
public class News {
    private int id;
    private String newsType;
    private String title;
    private String linkGetContent;
    private String category;
    private String content;
    private String imgConverted;
    private String author;
    private String publishTime;

    public News(String newsType, String title, String linkGetContent, String category, String content, String imgConverted, String author, String publishTime) {
        this.newsType = newsType;
        this.title = title;
        this.linkGetContent = linkGetContent;
        this.category = category;
        this.content = content;
        this.imgConverted = imgConverted;
        this.author = author;
        this.publishTime = publishTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkGetContent() {
        return linkGetContent;
    }

    public void setLinkGetContent(String linkGetContent) {
        this.linkGetContent = linkGetContent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgConverted() {
        return imgConverted;
    }

    public void setImgConverted(String imgConverted) {
        this.imgConverted = imgConverted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    
    
}
