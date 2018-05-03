package com.kafka.consumer;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Created by xwz on 2018/3/29.
 */
public class KafkaConsumerSample2 extends Thread{

    private String topic;

    public KafkaConsumerSample2(String topic){
        super();
        this.topic = topic;
    }


    @Override
    public void run() {

        createConsumer();
    }

    private void createConsumer() {
        Properties props = new Properties();

        //props.put("bootstrap.servers", "192.168.0.112:9092,192.168.0.113:9092,192.168.0.114:9092");
        props.put("bootstrap.servers", "172.25.1.1:9092,172.25.1.101:9092,172.25.1.174:9092");
        props.put("group.id", "group-test-1");
        //props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("enable.auto.commit", "true");
        //props.put("session.timeout.ms", "6000");;
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


        KafkaConsumer consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList("testTopic"));
        boolean flag = true;

        while (flag) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            System.out.println(records.count());
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, partition = %s, value = %s%n", record.offset(), record.partition(), record.value());
            }
        }
        /*String topicName = "testTopic";
        KafkaConsumer<String, Object> consumer = new KafkaConsumer<String, Object>(props);
        Map<TopicPartition, OffsetAndMetadata> hashMaps = new HashMap<TopicPartition, OffsetAndMetadata>();
        hashMaps.put(new TopicPartition(topicName, 0), new OffsetAndMetadata(0));
        hashMaps.put(new TopicPartition(topicName, 1), new OffsetAndMetadata(0));
        hashMaps.put(new TopicPartition(topicName, 2), new OffsetAndMetadata(0));
        consumer.commitSync(hashMaps);
        consumer.subscribe(Arrays.asList(topicName));
        while (true) {
            ConsumerRecords<String, Object> records = consumer.poll(100);
            for (ConsumerRecord<String, Object> record : records){
                System.out.println(record.toString());
            }
        }*/
    }


    public static void main(String[] args) {
        new KafkaConsumerSample2("testTopic").start();// 使用kafka集群中创建好的主题 test

    }

}
