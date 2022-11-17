package model;

import java.io.*;

public class Client {

    public static void main(String[] args) throws IOException {
        new ClientFoo("localhost", 8080);
    }
}