package com.springexample.irrigationagriculture.entity;

import com.springexample.irrigationagriculture.entity.enums.SensorType;

import java.util.ArrayDeque;
import java.util.Deque;

public class SensorBuffer {

        private static final int MAX_SIZE = 72;

        public static  Deque<Double> tempValues = new ArrayDeque<>(MAX_SIZE);
        public static  Deque<Double> humValues  = new ArrayDeque<>(MAX_SIZE);
        public static  Deque<Double> soilValues = new ArrayDeque<>(MAX_SIZE);
        public static  Deque<Double> rainValues = new ArrayDeque<>(MAX_SIZE);

        public static void add(SensorType type, double value) {
            Deque<Double> targetQueue = getQueue(type);

            if (targetQueue.size() == MAX_SIZE) {
                targetQueue.pollFirst();
            }
            targetQueue.addLast(value);
        }

        public static Deque<Double> getQueue(SensorType type) {
            return switch (type) {
                case TEMP  -> tempValues;
                case HUM   -> humValues;
                case SOIL  -> soilValues;
                case RAIN  -> rainValues;
            };
        }


    }


