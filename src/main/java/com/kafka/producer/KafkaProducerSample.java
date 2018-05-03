package com.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by xwz on 2018/3/29.
 */
public class KafkaProducerSample extends Thread {

    private String topic;

    public KafkaProducerSample(String topic){
        super();
        this.topic = topic;
    }

    @Override
    public void run(){
        Producer producer = createProducer();
        int i=0;
        while(true){
            producer.send(new ProducerRecord(topic, "message: " + i++));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Producer createProducer() {
        Properties properties = new Properties();

        properties.put("bootstrap.servers", "172.25.1.1:9092,172.25.1.101:9092,172.25.1.174:9092");
        //properties.put("bootstrap.servers", "192.168.0.112:9092,192.168.0.113:9092,192.168.0.114:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        /*properties.put("zookeeper.connect", "192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181");//声明zk
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("metadata.broker.list", "192.168.1.110:9092,192.168.1.111:9093,192.168.1.112:9094");// 声明kafka broker*/

        return producer;
    }


    public static void main(String[] args) {
        new KafkaProducerSample("testTopi").start();// 使用kafka集群中创建好的主题 test

    }

}
