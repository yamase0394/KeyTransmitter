package netpro.keytransmitter;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * キーの名前をレシーバーに送信するためのクラス
 */
public enum KeyTransmitter {
    INSTANCE;

    private String ip;
    private int port;

    public void send(List<String> keyStringList) {
        if (keyStringList.isEmpty()) {
            return;
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(keyStringList);
            oos.close();
            final byte[] sendByte = baos.toByteArray();

            final DatagramSocket sendSocket = new DatagramSocket();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(sendByte, sendByte.length, InetAddress.getByName(ip), port);
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

    public void setIp(String ip) {
        this.ip = ip;
        Log.d("ip", String.valueOf(ip));
    }

    public void setPort(int port) {
        this.port = port;
        Log.d("port", String.valueOf(port));
    }
}
