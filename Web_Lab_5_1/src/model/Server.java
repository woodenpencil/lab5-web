package model;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
	/**
	 * List for parallel working threads serving connected clients
	 */
    public static LinkedList<ServerFoo> serverList = new LinkedList<>();
    /**
     * Launch server
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Server is working");
        try {
        	int i =0;
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerFoo(socket, i));
                    i++;
                    System.out.println("Accepted new socket");
                } catch (IOException e) {
                    System.out.println(e);
                    //socket.close();
                }
            } 
        } finally {
            server.close();
        }
    }
}