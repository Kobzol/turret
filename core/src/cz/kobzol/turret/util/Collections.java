package cz.kobzol.turret.util;

import java.util.Random;

/**
 * Utility class for collections.
 */
public class Collections {
    public static <T> void  shuffleArray(T[] array) {
        Random random = new Random();

        for (int i = array.length - 1; i >= 0; i--) {
            int j = random.nextInt(i + 1);

            T tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
        }
    }
}
