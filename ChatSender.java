import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ChatSender extends JFrame implements ActionListener{

    JPanel panel = new JPanel();
    JTextField text = new JTextField("Här skriver man");
    JTextArea area = new JTextArea();
    JButton button = new JButton("Koppla ner");
    String nickName;
    String chatText;
    String dataSend;
    InetAddress toAdr = InetAddress.getByName("234.235.236.237");
    int toPort = 12540;

    MulticastSocket s = new MulticastSocket(toPort);



    public ChatSender() throws IOException {
        s.joinGroup(toAdr);
        nickName = JOptionPane.showInputDialog(null, "Type in you nick name: ");
        if(nickName == null){
            System.exit(0);
        }
        this.setTitle("Chat " + nickName);

        text.setPreferredSize(new Dimension(150,25));
        area.setPreferredSize((new Dimension(150,300)));
        area.setLayout(new BorderLayout(10,10));
        area.setLineWrap(true);
        panel.setLayout(new BorderLayout());
        panel.add(area, BorderLayout.CENTER);
        panel.add(text, BorderLayout.SOUTH);
        panel.add(button, BorderLayout.NORTH);
        button.addActionListener(this);
        text.addActionListener(this);
        this.add(panel);
        this.pack();
        this.setLocation(500,300);
        this.setSize(300,500);
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Thread chatReceiver = new Thread(new ChatReceiver(s, area));
        chatReceiver.start();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == button) {
            dataSend = nickName + " LEFT THE ROOM";
            byte[] data = dataSend.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, toAdr, toPort);
            try {
                s.send(packet);
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (ae.getSource() == text) {
            String chatText = text.getText();
            dataSend = nickName + ": " + chatText;
            byte[] data = dataSend.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, toAdr, toPort);
            try {
                s.send(packet);
            } catch (IOException e) {
                System.out.println("Error occured");
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        ChatSender c = new ChatSender();

    }


}