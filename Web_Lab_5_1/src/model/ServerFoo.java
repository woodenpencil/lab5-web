package model;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class ServerFoo extends Thread {
    private Socket socket;
    private int id;

    private BufferedReader in;

    private BufferedWriter out;

    //private String msg="Big thank you for joining us from server!";
    /**
     * Constructor for serving class
     * @param socket
     * @param id
     * @throws IOException
     */
    public ServerFoo(Socket socket, int id) throws IOException {
        this.socket = socket;
        this.id = id;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }
    /**
     * Accepting incoming messages and redirecting it to recievers.
     */
    @Override
    public void run() {
        String word;
        try {
            try {
                do {
                	System.out.println("waiting for message...");
                	word = in.readLine();
                	System.out.println(word+"(from "+this.id+")");
                	int rec =  Character.getNumericValue(word.charAt(0));
                	String msg = word.substring(2);
                	System.out.println("sending "+msg+" to "+rec);
                	Server.serverList.get(rec).send(this.id, msg);
                	//out.write(word+"\n");
                	//out.flush();
                	
                } while (true);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            System.out.println(e);
            this.downService();
        }
    }
    /**
     * Send accepted message to defined reciever
     * @param sender
     * @param msg
     */
    public void send(int sender, String msg) {
        try {
            out.write(msg + "(from "+sender+")\n");
            out.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    /**
     * Disables current server
     */
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerFoo vr : Server.serverList) {
                    if (vr.equals(this))
                        break;
                    //vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
