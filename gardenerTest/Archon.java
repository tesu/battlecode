package gardenerTest;

import battlecode.common.*;
import skeleton.Utils;

import java.util.Random;

public class Archon {
    public static void run(RobotController rc) {
        Random rand = new Random(rc.getID());
        Direction face = new Direction(2 * (float)Math.PI * rand.nextFloat());

        while (true) {
            try {
                Utils.alwaysRun(rc);

                Utils.RobotAnalysis R = new Utils.RobotAnalysis(rc.senseNearbyRobots());
                if (R.gardeners < 2) {
                    if (rc.canHireGardener(face.opposite())) {
                        rc.hireGardener(face.opposite());
                    }
                }

                Utils.dodgeBullets(rc);
                while (!rc.hasMoved() && !Utils.moveTowards(rc, face)) {
                    face = new Direction(2 * (float) Math.PI * rand.nextFloat());
                }

                Clock.yield();
            } catch (Exception e) {
                System.out.println("EXCEPTION");
                e.printStackTrace();
            }
        }
    }
}
