import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final String IP = "127.0.0.1";
    private final int PORT = 8989;
    private Socket sock;
    private Scanner scannerIn;
    private Scanner scannerOut;
    private PrintWriter writer;

    public void go() {
        setUpNetworking();

        Thread t = new Thread(new Reader());
        t.start();

        Thread i = new Thread(new Writer());
        i.start();
        System.out.println("Соединение установлено");
    }

    private void setUpNetworking() {
        try {
            sock = new Socket(IP, PORT);
            InputStream inStream = sock.getInputStream();
            OutputStream outStream = sock.getOutputStream();
            scannerIn = new Scanner(System.in);
            scannerOut = new Scanner(inStream);
            writer = new PrintWriter(new OutputStreamWriter(outStream), true);
            System.out.println("Networking established");
        } catch (IOException ex) {
            System.out.println("setUpNetworking error");
        }
    }


    private void connectIn(){
                if (scannerOut.hasNext()) {
                    String s = scannerOut.nextLine();
                    System.out.println(s);
                }
    }

    private void connectOut(){
            if (scannerIn.hasNext()) {
                String s = scannerIn.nextLine();
                writer.println(s);
            }
    }

    private class Reader implements Runnable {

        public void run() {
            try {
                while (!sock.isOutputShutdown()) {
                    connectIn();
                }
            } catch (Exception ex) {
                System.out.println("Ошибка в потоке");
                ex.printStackTrace();
            }
        }
    }
    private class Writer implements Runnable {

        public void run() {
            try {
                while (!sock.isInputShutdown()) {
                    connectOut();
                }
            } catch (Exception ex) {
                System.out.println("Ошибка в потоке");
                ex.printStackTrace();
            }
        }
    }
}