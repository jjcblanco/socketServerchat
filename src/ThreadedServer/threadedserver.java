//import jdk.internal.org.objectweb.asm.ClassReader;
package ThreadedServer;
import ThreadedServer.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class threadedserver {

    public static void main(String[] args) {
        Socket socket = null;
        ServerSocket ss = null;

        try {
              ss = new ServerSocket(6666);   // aca creo el socket en el puerto 6666
            System.out.println("Escuchando: ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {


                socket = ss.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            System.out.println("Se conecto un cliente: ");
            new EchoThread(socket).start();

        }
    }
}