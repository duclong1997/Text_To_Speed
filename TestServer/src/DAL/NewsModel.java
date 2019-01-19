/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Entity.News;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Xuan Truong
 */
public class NewsModel {

    private final String GENK = "http://genk.vn/";

    public NewsModel() {
    }

    public List<News> getNewsByType(String type) throws Exception {
        List<News> ln = new ArrayList<>();
        String query = "SELECT top 30 * FROM newstbl WHERE type = '" + type + "'";
        Connection conn = new DBContext().getConnection();
        PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
        //ps.setString(1, type);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String newsType = rs.getString("type");
            String title = rs.getNString("title");
            String link = rs.getNString("link");
            String category = rs.getNString("category");
            String content = rs.getNString("content");
            String img = rs.getNString("img");
            String author = rs.getNString("author");
            String time = rs.getNString("pubtime");
            News n = new News(newsType, title, link, category, content, img, author, time);
            ln.add(n);
        }
        rs.close();
        ps.close();
        conn.close();
        return ln;
    }

    public List<News> getNewsById(String id) throws Exception {
        List<News> ln = new ArrayList<>();
        String query = "SELECT * FROM newstbl WHERE id = '" + id + "'";
        Connection conn = new DBContext().getConnection();
        PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
        //ps.setString(1, type);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String newsType = rs.getString("type");
            String title = rs.getNString("title");
            String link = rs.getNString("link");
            String category = rs.getNString("category");
            String content = rs.getNString("content");
            String img = rs.getNString("img");
            String author = rs.getNString("author");
            String time = rs.getNString("pubtime");
            News n = new News(newsType, title, link, category, content, img, author, time);
            ln.add(n);
        }
        rs.close();
        ps.close();
        conn.close();
        return ln;
    }

    public List<News> getNewsByCategoty(String cat) throws Exception {
        List<News> ln = new ArrayList<>();
        String query = "SELECT * FROM newstbl WHERE category = '" + cat + "'";
        Connection conn = new DBContext().getConnection();
        PreparedStatement ps = (PreparedStatement) conn.prepareStatement(query);
        //ps.setString(1, type);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String newsType = rs.getString("type");
            String title = rs.getNString("title");
            String link = rs.getNString("link");
            String category = rs.getNString("category");
            String content = rs.getNString("content");
            String img = rs.getNString("img");
            String author = rs.getNString("author");
            String time = rs.getNString("pubtime");
            News n = new News(newsType, title, link, category, content, img, author, time);
            ln.add(n);
        }
        rs.close();
        ps.close();
        conn.close();
        return ln;
    }

    public void addNews(News news) throws Exception {
        String query = "INSERT INTO newstbl(type,title,link,category,content,img,author,pubtime)"
                + "VALUES (?,?,?,?,?,?,?,?)";
        Connection c = new DBContext().getConnection();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, news.getNewsType());
        ps.setString(2, news.getTitle());
        ps.setString(3, news.getLinkGetContent());
        ps.setString(4, news.getCategory());
        ps.setString(5, news.getContent());
        ps.setString(6, news.getImgConverted());
        ps.setString(7, news.getAuthor());
        ps.setString(8, news.getPublishTime());
        ps.execute();
        ps.close();
        c.close();
    }

    public void crawlData(String newsAddress) {
        Document freshContent;
        Document mainContent;
        String type = null, title = null, link = null, category = null, content = null, imgConverted = null, author = null, time = null, url = "";
        switch (newsAddress) {
            case "genk": {
                try {
                    freshContent = Jsoup.connect(GENK).get();
                    Elements newsTitle = freshContent.select(".shownews");
                    for (int i = 0; i < newsTitle.size(); i++) {
                        link = newsTitle.get(i).select(".knswli-title").select("a").attr("abs:href");
                        title = newsTitle.get(i).select(".knswli-title").text();
                        if (!link.isEmpty()) {
                            mainContent = Jsoup.connect(link).get();
                            category = newsTitle.get(i).select("a.knswli-category").text();
                            Elements newsContent = mainContent.select(".kbwc-body");
                            type = "genk";
                            content = newsContent.get(0).select(".knc-content").select("p").text();
                            author = newsContent.get(0).select(".kbwcm-author").text();
                            time = newsContent.get(0).select(".kbwcm-time").attr("title");
                            url = newsContent.get(0).select(".knc-content").select("img").first().absUrl("src");
                            if (url.equals("")) {
                                System.out.println("no img");
                            } else {
                                imgConverted = getByteArrayFromImageURL(url);
                            }
                            News n = new News(type, title, link, category, content, imgConverted, author, time);
                            List<News> ln = getNewsByType(type);
                            int count = 0;
                            if (ln.size() <= 0) {
                                addNews(n);
                            } else {
                                for (int j = 0; j < ln.size(); j++) {
                                    if (ln.get(j).getTitle().equalsIgnoreCase(n.getTitle())) {
                                        count++;
                                    }
                                }
                                if (count == 0) {
                                    addNews(n);
                                }
                            }
                        }

                    }

                    break;
                } catch (IOException ex) {
                    Logger.getLogger(NewsModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(NewsModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case "cafef": {
                try {
                    freshContent = Jsoup.connect("http://cafef.vn/").get();
                    Elements newsTitle = freshContent.select(".knswli-right");
                    for (int i = 0; i < newsTitle.size(); i++) {
                        type = "cafef";
                        title = newsTitle.get(i).select("h3").text();
                        link = newsTitle.get(i).select("h3").select("a").attr("abs:href");
                        if (!link.isEmpty()) {
                            category = newsTitle.get(i).select(".time_cate").select("a").text();
                            mainContent = Jsoup.connect(link).get();
                            Elements newsContent = mainContent.select(".left_cate");
                            content = newsContent.get(0).select(".contentdetail").select("p").text();
                            time = newsContent.get(0).select(".pdate").text();
                            author = newsContent.get(0).select(".author").text();
                            boolean flag = newsContent.get(0).select(".w640").hasAttr("img");
                            if(flag){
                                url = newsContent.get(0).select(".w640").tagName("img").attr("abs:src");
                            } else {
                                url = "";
                            }
                            if (url.equals("")) {
                                imgConverted = "noImg";
                            } else {
                                imgConverted = getByteArrayFromImageURL(url);
                            }
                            News n = new News(type, title, link, category, content, imgConverted, author, time);
                            List<News> ln = getNewsByType(type);
                            int count = 0;
                            if (ln.size() <= 0) {
                                addNews(n);
                            } else {
                                for (int j = 0; j < ln.size(); j++) {
                                    if (ln.get(j).getTitle().equalsIgnoreCase(n.getTitle())) {
                                        count++;
                                        System.out.println(link);
                                    }
                                }
                                if (count == 0) {
                                    System.out.println("bingo!!!!");
                                    addNews(n);
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(NewsModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(NewsModel.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case "kenh14":{
                try {
                    freshContent = Jsoup.connect("http://kenh14.vn/").get();
                    Elements newsTitle = freshContent.select(".knswli");
                    for (int i = 0; i < newsTitle.size(); i++) {
                        type = "kenh14";
                        title = newsTitle.get(i).select(".knswli-title").text();
                        category = newsTitle.get(i).select(".knswli-category").select("a").text();
                        link = newsTitle.get(i).select(".knswli-title").select("a").attr("abs:href");
                        System.out.println(link);
                        if (!link.isEmpty() && !link.contains("video")) {
                            mainContent = Jsoup.connect(link).get();
                            Elements newsContent = mainContent.select(".klw-body-top");
                            content = newsContent.get(0).select(".klw-new-content").select("p").text();
                            time = newsContent.get(0).select(".kbwcm-time").text();
                            author = newsContent.get(0).select(".kbwcm-author").text();
                            url = newsContent.get(0).select(".klw-new-content").first().select("img").attr("abs:src");
                            if (url.equals("")) {
                                imgConverted = "noImg";
                            } else {
                                System.out.println(url);
                                imgConverted = getByteArrayFromImageURL(url);
                                System.out.println("have img");
                            }
                            
                            News n = new News(type, title, link, category, content, imgConverted, author, time);
                            List<News> ln = getNewsByType(type);
                            int count = 0;
                            if (ln.size() <= 0) {
                                addNews(n);
                            } else {
                                for (int j = 0; j < ln.size(); j++) {
                                    if (ln.get(j).getTitle().equalsIgnoreCase(n.getTitle()) && n.getCategory().equals("")) {
                                        count++;
                                    }
                                }
                                if (count == 0) {
                                    System.out.println("bingo!!!!");
                                    addNews(n);
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(NewsModel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(NewsModel.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }


    private String getByteArrayFromImageURL(String url) {
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(NewsModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
