package com.frederic.taskmanager.service;

import java.util.Random;

public class IdGeneratorService {
    private static final Random RANDOM = new Random();

    public static int generateId() {
        StringBuilder sb = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            sb.append(RANDOM.nextInt(10));
        }

        return Integer.parseInt(sb.toString());
    }
}
