package com.frederic.taskmanager.service;

import java.util.Random;

/**

 * Génère des identifiants numériques aléatoires pour les tâches.
 */
public class IdGeneratorService {
    private static final Random RANDOM = new Random();

    /**

     * Génère un identifiant aléatoire composé de cinq chiffres.
     *
     * @return identifiant aléatoire à cinq chiffres
     */
    public static int generateId() {
        StringBuilder sb = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            sb.append(RANDOM.nextInt(1,10));
        }

        return Integer.parseInt(sb.toString());
    }
}
