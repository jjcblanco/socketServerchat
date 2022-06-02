package ThreadedServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static JFrame f;
    public static JTextField tf;

    public static JLabel ipLabel;
    public static InetAddress ip;
    public static String hostname;

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(5056);

        Main.inicializar_frame(ss);
        // running infinite loop for getting
        // client request

        ip = InetAddress.getLocalHost();
        hostname = ip.getHostName();

        ipLabel.setText("IP Local: "+ip.getHostAddress());
        System.out.println("Your current IP address : " + ip);
        System.out.println("Your current Hostname : " + hostname);

        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();


                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos);

                // Invoking the start() method
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

    static void inicializar_frame(ServerSocket ss)
    {

        f=new JFrame("Servidor chat");

        tf=new JTextField();
        tf.setBounds(50,120, 350,60);
        tf.setFont(new Font(tf.getName(), Font.PLAIN, 28));

        JLabel titulo=new JLabel("SERVIDOR");
        titulo.setBounds(380,15, 240,40);
        Font labelFont = titulo.getFont();
        titulo.setFont(new Font(labelFont.getName(), Font.PLAIN, 28));

        ipLabel=new JLabel("ip...");
        ipLabel.setBounds(640,70, 150,30);


        JButton enviarBtn=new JButton("Enviar");
        enviarBtn.setBounds(500,120,95,30);

        JButton escucharBtn=new JButton("Escuchar");
        escucharBtn.setBounds(680,120,95,30);

        escucharBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                tf.setText(" ");
            }
        });

        JButton desconectar=new JButton("Desconectar");
        desconectar.setBounds(680,180,95,30);

        desconectar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    ss.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                tf.setText(" Desconectado");
            }
        });

        JButton salir=new JButton("Salir");
        salir.setBounds(680,240,95,30);

        salir.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    ss.close();
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                tf.setText(" Desconectado");
            }
        });

        f.add(escucharBtn);
        f.add(tf);
        f.add(enviarBtn);
        f.add(desconectar);
        f.add(salir);
        f.add(ipLabel);

        f.add(titulo);
        f.setSize(800,800);
        //f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        f.setLayout(null);
        f.setVisible(true);
    }
}

// ClientHandler class
class ClientHandler extends Thread
{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true)
        {
            try {

                // Ask user what he wants
                dos.writeUTF("What do you want?[Date | Time]..\n"+
                        "Type Exit to terminate connection.");

                // receive the answer from client
                received = dis.readUTF();
                System.out.println(received);
                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

                // creating Date object
                Date date = new Date();

                // write on output stream based on the
                // answer from the client
                switch (received) {

                    case "Date" :
                        System.out.println("entro date");
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    case "Time" :
                        System.out.println("entro time");
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;

                    default:
                        dos.writeUTF("Invalid input");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
