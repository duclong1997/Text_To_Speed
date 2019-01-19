/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Xuan Truong
 */
public class ClientDemo {
    public static void main(String[] args) {
        final String serverHost = "localhost";
 
       Socket socketOfClient = null;
       BufferedWriter os = null;
       BufferedReader is = null;
 
       try {
           // Gửi yêu cầu kết nối tới Server đang lắng nghe
           // trên máy 'localhost' cổng 7777.
           socketOfClient = new Socket(serverHost, 1234);

           // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
           os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
 
           // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
           is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

       } catch (UnknownHostException e) {
           System.err.println("Don't know about host " + serverHost);
           return;
       } catch (IOException e) {
           System.err.println("Couldn't get I/O for the connection to " + serverHost);
           return;
       }
 
       try {
           // Ghi dữ liệu vào luồng đầu ra của Socket tại Client.
           
           os.write("genk");
           os.newLine();
           os.flush();
 
           // Đọc dữ liệu trả lời từ phía server
           // Bằng cách đọc luồng đầu vào của Socket tại Client.
           String responseLine;
           while ((responseLine = is.readLine()) != null) {
               System.out.println("Server: " + responseLine);
               
//               if (responseLine.indexOf("OK") != -1) {
//                   break;
//               }
           }
 
           os.close();
           is.close();
           socketOfClient.close();
       } catch (UnknownHostException e) {
           System.err.println("Trying to connect to unknown host: " + e);
       } catch (IOException e) {
           System.err.println("IOException:  " + e);
       }
   }
}
