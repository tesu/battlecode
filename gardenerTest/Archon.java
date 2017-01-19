package gardenerTest;

import battlecode.common.*;
import skeleton.*;
import skeleton.Utils;

import java.util.Random;

public class Archon {
    public static void run(RobotController rc) {
        try {
            Random rand = new Random(rc.getID());
            Direction face = new Direction(2 * (float)Math.PI * rand.nextFloat());

            while (true) {
                Utils.RobotAnalysis R = new Utils.RobotAnalysis(rc.senseNearbyRobots());
                if (R.gardeners() < 2) {
                    if (rc.canHireGardener(face.opposite())) {
                        rc.hireGardener(face.opposite());
                    }
                }

                if (rc.canMove(face)) rc.move(face);
                else {
                    while (!rc.canMove(face)) {
                        face = new Direction(2 * (float)Math.PI * rand.nextFloat());
                    }
                    rc.move(face);
                }

                Clock.yield();
            }
        } catch (Exception e) {

        }
    }
}
