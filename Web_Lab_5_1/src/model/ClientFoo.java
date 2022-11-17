package model;


import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

class ClientFoo {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader inSymbol;
    private BufferedWriter outSymbol;
    private BufferedReader inputUser;
    private String msg;
    
    /**
     * Constructor for client
     * @param addr
     * @param port
     */
    public ClientFoo(String addr, int port, String path) {
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.out.println(e);
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            
            if (socket != null) {
                in = socket.getInputStream();
                inSymbol = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                }
            if (socket != null) {
                out = socket.getOutputStream();
                outSymbol = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            this.sendMsg(path);
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
    private void sendMsg(String path) {
        try {
        	BufferedImage image = ImageIO.read(new File(path));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            out.write(size);
            out.write(byteArrayOutputStream.toByteArray());
            out.flush();
            System.out.println("Flushed: " + System.currentTimeMillis());
            
            /*
            String rec = inputUser.readLine();
            String rec = "0";
            outSymbol.write(rec+"\n");
            outSymbol.flush();
			*/
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
     *
     */
    private class readMsg extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                	System.out.println("Reading: " + System.currentTimeMillis());

                    byte[] sizeAr = new byte[4];
                    in.read(sizeAr);
                    int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                    byte[] imageAr = new byte[size];
                    in.read(imageAr);

                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

                    System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
                    ImageIO.write(image, "jpg", new File("recieved_image.jpg"));
                    
                    
                    
                    //int sender = Integer.parseInt(inSymbol.read());
                }
            } catch (IOException e) {
                System.out.println(e);
                ClientFoo.this.downService();
            }
        }
    }

}
