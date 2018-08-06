package com.nio.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xwz on 2018/5/3.
 */
public class TestServer {

    private ServerSocket serverSocket;

    public TestServer() {
        try {
            this.serverSocket = new ServerSocket(8082);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getConnect(){
        Socket s;
        OutputStream os;
        try {
            s = serverSocket.accept();
            os = s.getOutputStream();
            os.write("hello world".getBytes());
            os.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestServer testServer = new TestServer();
        while(true){
            testServer.getConnect();
        }

    }
}
