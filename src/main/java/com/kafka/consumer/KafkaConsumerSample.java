package com.kafka.consumer;

import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;

/**
 * Created by xwz on 2018/3/29.
 */
public class KafkaConsumerSample extends Thread{

    private String topic;

    public KafkaConsumerSample(String topic){
        super();
        this.topic = topic;
    }


    @Override
    public void run() {
        ConsumerConnector consumer = createConsumer();
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1); // 一次从主题中获取一个数据
        Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);// 获取每次接收到的这个数据
        ConsumerIterator<byte[], byte[]> iterator =  stream.iterator();
        while(iterator.hasNext()){
            String message = new String(iterator.next().message());
            System.out.println("接收到: " + message);
        }
    }

    private ConsumerConnector createConsumer() {
        Properties props = new Properties();

        props.put("zookeeper.connect","172.25.2.77:2181,172.25.2.99:2181,172.25.2.109:2181");
        //props.put("zookeeper.connect","192.168.0.112:2181,192.168.0.113:2181,192.168.0.114:2181");

        props.put("group.id","jd-group");//消费组是什么概念？

        props.put("zookeeper.session.timeout.ms","60000");
        props.put("zookeeper.sync.time.ms","200");
        props.put("auto.commit.interval.ms","1000");
        props.put("auto.offset.reset","smallest");

        props.put("serializer.class","kafka.serializer.StringEncoder");

        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        return consumerConnector;
    }


    public static void main(String[] args) {
        new KafkaConsumerSample("testTopic").start();// 使用kafka集群中创建好的主题 test

    }

}
