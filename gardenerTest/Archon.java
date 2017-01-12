package gardenerTest;

import battlecode.common.*;

import java.util.Random;

public class Archon {
    public static void run(RobotController rc) {
        try {
            Random rand = new Random(rc.getID());
            int gardeners = 0;

            while (true) {
                Direction d = new Direction(rand.nextFloat()*(float)Math.PI*2);
                if (gardeners == 0) {
                    if (rc.canHireGardener(d)) {
                        rc.hireGardener(d);
                        gardeners++;
                    }
                } else {
                    rc.disintegrate();
                }
                Clock.yield();
            }
        } catch (Exception e) {

        }
    }
}
