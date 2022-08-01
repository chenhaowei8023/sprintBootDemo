package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * XXX
 *
 * @author SixHwChen
 * @date 2022/7/28 15:29
 */
@Controller
public class ControllerDemo {

    private Object obj1 = new Object();
    private Object obj2 = new Object();

    @RequestMapping("/cpuTest")
    public String cpuTest() {
        new Thread(() -> {
            while (true) {
                String str = UUID.randomUUID().toString().replaceAll("-", "");
                System.out.println(str);
            }
        }, "cpuTest thread").start();

        new Thread(() -> {
            while (true) {
                String str = UUID.randomUUID().toString().replaceAll("-", "");
                System.out.println(str);
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "cpuTest sleep thread").start();
        System.out.println("cupTest finished");
        return "hello.html";
    }

    @RequestMapping("/cpu2Test")
    public String cpu2Test() {
        new Thread(() -> {
            synchronized (obj1) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    synchronized (obj2) {
                        System.out.println("thread 1执行到此");
                    }
                }
            }
        }, "cpu2Test 1").start();

        new Thread(() -> {
            synchronized (obj2) {
                synchronized (obj1) {
                    System.out.println("thread 2执行到此");
                }
            }
        }, "cpu2Test 2").start();
        System.out.println("cupTest2 finished");
        return "hello.html";
    }

    @RequestMapping("/heapTest")
    public String heapTest() throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();

        int i = 0;
        while (true) {
            TimeUnit.MILLISECONDS.sleep(1);
            list.add(new OOMObject());
            System.out.println("执行到了:" + i);
            i++;
        }
    }

    @RequestMapping("/stackTest")
    public static void stackTest() {
        while(true) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.HOURS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            thread.start();
        }
    }


    static class OOMObject {
    }
}
