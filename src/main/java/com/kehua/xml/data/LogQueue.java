package com.kehua.xml.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class LogQueue {

    private static final List<Consumer<String>> register = new ArrayList<>();

    public static void register(Consumer<String> consumer) {
        register.add(consumer);
    }

    public static LinkedBlockingQueue<String> logQueue = new LinkedBlockingQueue<String>() {
        @Override
        public boolean add(String s) {
            register.forEach(consumer -> {
                try {
                    consumer.accept(s);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            return super.add(s);
        }
    };
}
