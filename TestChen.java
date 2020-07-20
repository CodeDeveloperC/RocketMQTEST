package com.example.test.arraylistrocketmq;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen
 * @version 1.0
 * @date 2020/7/20 9:20
 * @description:
 */
public class TestChen {


    // 初始化一个List，存放每个NameServer注册结果的
    final List<RegisterBrokerResult> registerBrokerResultList =
            Lists.newArrayList();
    // 获取 NameServer 地址列表
    List<String> nameServerAddressList = new ArrayList<>();

    {
        for (int i = 0; i < 10; i++) {
            nameServerAddressList.add("192.168.0." + i);
        }
    }





    private BrokerFixedThreadPoolExecutor brokerOuterExecutor = new BrokerFixedThreadPoolExecutor(4, 1000, 1, TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(32), new ThreadFactoryImpl("brokerOutApi_thread_", true));


    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            deal(i);
            nameServerAddressList.clear();
            for (int j = 0; j < 10; j++) {
                nameServerAddressList.add("192.168.0." + j);
            }
            Thread.sleep(1000);
        }
    }

    public void deal(int index) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        // 执行其他逻辑
        final CountDownLatch countDownLatch = new CountDownLatch(nameServerAddressList.size());
        // 遍历NameServer 地址列表，使用线程池去注册
        for (final String namesrvAddr : nameServerAddressList) {
            brokerOuterExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 调用 registerBroker 真正执行注册
                        RegisterBrokerResult result = new RegisterBrokerResult();
                        result.setHaServerAddr("chen" + atomicInteger.getAndIncrement());
                        Thread.sleep(1000);
                        if (result != null) {
                            // 注册成功结果放到一个List里去
                            registerBrokerResultList.add(result);
                        }

                    } catch (Exception e) {
                        System.out.println("-----------wei---------------> " + index);
                        System.out.println(e);
                        System.out.println("-----------wei---------------> " + index);
                    } finally {
                        // 注册完，执行 countDownLatch.countDown(); 同步计数器 -1
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            // 等待所有 NameServer 都注册完，才返回注册结果
            countDownLatch.await(1000000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }

        System.out.println(registerBrokerResultList.size());
        System.out.println(registerBrokerResultList);
        System.out.println("-----------chen---------------------------------------> ");
    }
}
