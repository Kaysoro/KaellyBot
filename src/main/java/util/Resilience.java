package util;

import java.util.Random;

public class Resilience {

    private final static Random RAND = new Random();

    public static long nextExponentialBackoff(long retries, long interval) {
        switch ((int)retries) {
            case 0:
                return Math.abs(RAND.nextLong() % interval);
            case 1:
                return RAND.nextInt(7) * interval;
            default:
                if (retries >= 2 && retries < 15) {
                    return RAND.nextInt((1 << retries) - 1) * interval;
                } else {
                    return RAND.nextInt(1 << 15) * interval;
                }
        }
    }
}
