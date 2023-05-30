import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.*;

public class ChatReceiver implements Runnable{
    private static MulticastSocket s;
    private JTextArea area;
    public ChatReceiver(MulticastSocket s, JTextArea area) throws IOException {
        this.s = s;
        this.area = area;

    }

    public ChatReceiver() {

    }

    @Override
    public void run() {
        byte[] data = new byte[255];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        while (true) {
            try {
                s.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                area.append(msg + "\n");
            } catch (IOException e) {
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ChatReceiver r = new ChatReceiver();
    }




}
