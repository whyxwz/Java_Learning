package com.nio.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by xwz on 2018/5/3.
 */
public class TestClient {

    private Socket s;

    public  TestClient(){
        try {
            s = new Socket("127.0.0.1", 8082);
            InputStream is = s.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestClient client = new TestClient();
    }

}
