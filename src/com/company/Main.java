package com.company;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttReceivedMessage;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {


    public static void main(String[] args) {

        String topic        = "temperature-topic/1";
        String topicTwo     = "temperatureCalc-topic/2";
        String content      = " ";
        int qos             = 2;
        String broker       = "tcp://broker.hivemq.com:1883";
        String clientId     = "clientId-Jesper";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+ broker);
            client.connect(connOpts);
            System.out.println("Connected");
            FileWriter log = new FileWriter("log.txt");
            client.subscribe(topic, (topic1, message) -> {
                System.out.println(message.toString());
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                log.write("\n" + date + " - " + topic + " - " + message.toString());
                //log.write("\n" + date + " - " + topicTwo + " - " + message.toString()+ "\n");
                log.flush();
            });
            client.subscribe(topicTwo, (topic1, message) -> {
                System.out.println(message.toString());
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                //log.write("\n" + date + " - " + topic + " - " + message.toString());
                log.write("\n" + date + " - " + topicTwo + " - " + message.toString()+ "\n");
                log.flush();
            });
            MqttMessage response = new MqttMessage(content.getBytes());
            response.setQos(qos);
            client.publish(topic, response);
            System.out.println("Message published");


            //sampleClient.disconnect();
            //System.out.println("Disconnected");
            //System.exit(0);
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