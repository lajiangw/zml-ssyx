package com.zml;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-18 16:20
 */
public class T {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("主线程开始");
        CompletableFuture<Integer> uCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程：" + Thread.currentThread().getName());
            int i = 2 / 0;
            System.out.println(2);
            return i;
        }, executorService).whenComplete((rs, e) -> {
            System.out.println("whenComplete" + rs);
            System.out.println("e:" + e);
        });
        Integer integer = uCompletableFuture.get();
        System.out.println(integer);
    }
}
