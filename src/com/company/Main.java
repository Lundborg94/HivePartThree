package com.company;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        String topic        = "JoshShJe/#";
        String broker       = "tcp://broker.hivemq.com:1883";
        String clientId     = "clientId-Jesper";
        try {
            MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Subscribing to topics: " + topic);
            FileWriter log = new FileWriter("Log.txt", true);

            client.subscribe(topic, 2, (t, message) -> {
                System.out.println("Adding to log: " + message.toString() + ", from " + topic);
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                log.write(date + " - " + t + " - " + message.toString() + "\n");
                log.flush();
            });

        } catch(MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        } catch (IOException e ){
            System.out.println("fel");
        }
    }
}