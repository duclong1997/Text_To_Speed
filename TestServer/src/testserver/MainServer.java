/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

import DAL.NewsModel;
import Entity.News;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xuan Truong
 */
public class MainServer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(1234);

//            NewsModel model = new NewsModel();
//            model.crawlData("kenh14");
            while (true) {

                Socket socketOfServer = listener.accept();
                ServiceThread serviceThread = new ServiceThread(socketOfServer);
                serviceThread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            listener.close();
        }
    }

    private static class ServiceThread extends Thread {

        private final Socket socket;

        public ServiceThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                while (true) {
                    String command = is.readLine();
                    os.write(toJSON(command));
                    os.newLine();
                    os.flush();
                    break;
                }
                System.out.println("thread stop");
            } catch (IOException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String toJSON(String type) {
        NewsModel model = new NewsModel();
        List<News> list = new ArrayList<>();
        String json = null;
        try {
            list = model.getNewsByType(type);
            Gson gson = new Gson();
            json = gson.toJson(list);
        } catch (Exception ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }

}
