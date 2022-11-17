package model;

import java.io.*;

public class Client {
	/**
	 * Launches client
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
    	//File file = new File("/home/anton/eclipse-workspace/lab5-web/Web_Lab_5_1/src/pictures.txt");
        //BufferedReader br = new BufferedReader(new FileReader(file));
        //FileWriter fr = new FileWriter(file, true);
		BufferedReader br = new BufferedReader(new FileReader("/home/anton/eclipse-workspace/lab5-web/Web_Lab_5_1/src/pictures.txt"));

		String st;
        System.out.println("Choose path of picture:");
            while ((st = br.readLine()) != null) {
                System.out.println(st);
            }
        br.close();
        //fr.close();
        
        System.out.print("Input path of picture:");
        BufferedReader inputUser = new BufferedReader(new InputStreamReader(System.in));
        String path = inputUser.readLine();
        System.out.println("You entered: "+path);
        new ClientFoo("localhost", 8080, path);
    }
}