import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final String IP = "127.0.0.1";
    private final int PORT = 8989;
    private Socket sock;
    private PrintWriter writer;
    private Scanner scan;
    private Scanner reader = new Scanner(System.in);


    public void go() {
        setUpNetworking();

        Thread t = new Thread(new IncomingReader());
        t.start();
        System.out.println("Соединение установлено");
    }

    private void setUpNetworking() {
        try {
            sock = new Socket(IP, PORT);
            InputStream inStream = sock.getInputStream();
            OutputStream outStream = sock.getOutputStream();
            scan = new Scanner(inStream, "Cp1251");
            writer = new PrintWriter(new OutputStreamWriter(outStream, "Cp1251"), true);
            System.out.println("Networking established");

        } catch (IOException ex) {
            System.out.println("setUpNetworking error");
        }
    }

   /* private void sendMessage(){
      Scanner  reader = new Scanner(System.in);

        if (reader.hasNext()){
            String s = reader.nextLine();
            writer.println(s);
            System.out.println ("gjnjr ddjlf jnrhsm");

        }
    }*/

    private void connectIn(){
        if (scan.hasNext()) {
            String s = scan.nextLine();
            System.out.println(s);
        }
    }

    private void connectOut(){
        if (reader.hasNext()){
            String s = reader.nextLine();
            System.out.println(s);
            writer.println(s);
        }
    }

    private class IncomingReader implements Runnable {
        String message;

        public void run() {

            try {

                String s;
                while ((scan.hasNext()) || (reader.hasNext())) {
                    connectIn();
                    connectOut();

                    if ((!scan.hasNext())&&(!reader.hasNext())) break;
                }

            } catch (Exception ex) {
                System.out.println("Ошибка в потоке");
                ex.printStackTrace();
            }
        }
    }
}