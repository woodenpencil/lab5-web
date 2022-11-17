package model;


import java.io.*;
import java.net.Socket;

class ClientFoo {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String msg;
    /**
     * Constructor for client
     * @param addr
     * @param port
     */
    public ClientFoo(String addr, int port) {
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            if (socket != null) {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            if (socket != null) {
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            this.sendMsg();
        	//String word = in.readLine();
        	//System.out.print(word+"\n");
            new readMsg().start();
        } catch (IOException e) {
            System.out.println(e);
            ClientFoo.this.downService();
        }
    }
    /**
     * Send message to another client
     */
    private void sendMsg() {
        System.out.print("Enter id of reciever and message: ");
        try {
            msg = inputUser.readLine();
            out.write(msg+"\n");
            out.flush();
            //System.out.println("Entered name: " + name);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    /**
     * Disables client
     */
    private void downService() {
        try {
            if (!socket.isClosed()) {
                System.out.println("Good Bye!");
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    /**
     * Read message from another client
     * @author anton
     *
     */
    private class readMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                	System.out.println("waiting for messages:");
                	str = in.readLine();
                	System.out.println(str);
                	/*
                	str = inputUser.readLine();
                    out.write(str);
                    out.flush();
                    if (str.equalsIgnoreCase("x")) {
                        System.out.println("You are going to leave us.");
                        ClientFoo.this.downService();
                        break;
                    }
                    */
                }
            } catch (IOException e) {
                System.out.println(e);
                ClientFoo.this.downService();
            }
        }
    }

}
