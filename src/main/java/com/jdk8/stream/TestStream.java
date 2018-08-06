package com.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by xwz on 2018/7/3.
 */
public class TestStream {

    public static void main(String[] args) {
        /*List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");

        System.out.println(strings);
        System.out.println(strings.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList()));

        Stream stream = strings.stream();
        stream.forEach(s-> System.out.println(s));*/

        /*List<Integer> ints = Arrays.asList(1,9,3,4,7,6,5,8,2);
        System.out.println(ints.stream().map(i -> i*i).sorted().collect(Collectors.toList()));*/


        CompletableFuture<Void> future = CompletableFuture.runAsync(()-> System.out.println("hello runAsync"));
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("complete");


        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello";
        });
        try{
            System.out.println(future1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
