package model;

import java.io.*;

public class Client {
	/**
	 * Launches client
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        new ClientFoo("localhost", 8080);
    }
}