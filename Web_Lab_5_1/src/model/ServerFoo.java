package model;


import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

import javax.imageio.ImageIO;

class ServerFoo extends Thread {
    private Socket socket;
    private int id;

    private InputStream in;
    private OutputStream out;

    private BufferedReader inSymbol;
    private BufferedWriter outSymbol;
    /**
     * Constructor for serving class
     * @param socket
     * @param id
     * @throws IOException
     */
    public ServerFoo(Socket socket, int id) throws IOException {
        this.socket = socket;
        this.id = id;
        in = socket.getInputStream();
        out = socket.getOutputStream();
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
                	System.out.println("Reading: " + System.currentTimeMillis());

                    byte[] sizeAr = new byte[4];
                    in.read(sizeAr);
                    int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                    byte[] imageAr = new byte[size];
                    in.read(imageAr);

                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

                    System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());

                    /*
                	word = inSymbol.readLine();
                	System.out.println(word+"(from "+this.id+")");
                	int rec =  Integer.parseInt(word);
                	System.out.println("sending to "+rec);
                	*/
                	Server.serverList.get(0).send(this.id, image);
                	
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
    public void send(int sender, BufferedImage image) {
        try {
        	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            out.write(size);
            out.write(byteArrayOutputStream.toByteArray());
            out.flush();
            System.out.println("Flushed: " + System.currentTimeMillis());
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
