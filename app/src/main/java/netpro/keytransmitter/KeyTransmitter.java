package netpro.keytransmitter;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public enum KeyTransmitter {
    INSTANCE;

    public void send(List<String> keyStringList) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(keyStringList);
            oos.close();
            byte[] sendByte = baos.toByteArray();

            final DatagramPacket sendPacket;
            sendPacket = new DatagramPacket(sendByte, sendByte.length, InetAddress.getByName("192.168.0.4"), 8088);
            final DatagramSocket sendSocket = new DatagramSocket();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendSocket.send(sendPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sendSocket.close();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
