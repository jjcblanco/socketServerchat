package ThreadedServer;

import java.io.*;
import java.net.Socket;

public class EchoThread extends Thread {
        protected Socket socket;

        public EchoThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {
            System.out.println("ACA ENTROOOO " );
            InputStream inp = null;
            BufferedReader brinp = null;
            DataOutputStream out = null;
            try {
                inp = socket.getInputStream();
                brinp = new BufferedReader(new InputStreamReader(inp));
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                return;
            }
            String line;
            while (true) {
                try {
                    System.out.println("paso");
                    line = brinp.readLine();
                    System.out.println("leyo");
                    if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                        socket.close();
                        return;
                    } else {
                        out.writeBytes(line + "\n\r");
                        System.out.println(line);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
}

