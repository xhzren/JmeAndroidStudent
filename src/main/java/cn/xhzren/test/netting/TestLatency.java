package cn.xhzren.test.netting;

import cn.xhzren.game.localrpg.entity.netting.MovingAverage;
import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

import java.sql.Time;

public class TestLatency {

    private static long startTime;
    private static Client client;
    private static MovingAverage average = new MovingAverage(100);

    static  {
        startTime = System.currentTimeMillis();
    }

    private static long getTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Serializable
    public static class TimestampMessage extends AbstractMessage {
        long timeSent = 0;
        long timeReceived = 0;

        TimestampMessage() {
            setReliable(false);
        }
        public TimestampMessage(long timeSent, long timeReceived) {
            setReliable(false);
            this.timeSent = timeSent;
            this.timeReceived = timeReceived;
        }
    }

    public static void main(String[] args) throws Exception{
        Serializer.registerClass(TimestampMessage.class);

        Server server = Network.createServer(5110);
        server.start();

        Client client = Network.connectToServer("localhost", 5110);
        client.addMessageListener(new MessageListener<Client>() {
            @Override
            public void messageReceived(Client client, Message m) {
                TimestampMessage timeMsg = (TimestampMessage)m;

                long curTime = getTime();

                long latency = (curTime  - timeMsg.timeSent);
                System.out.println("Latency: " + (latency)  + " ms");
            }
        }, TimestampMessage.class);
    }
}
