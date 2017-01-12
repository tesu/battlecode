package gardenerTest;

import battlecode.common.*;

import java.util.Random;

public class Gardener {
    public static void run(RobotController rc) {
        try {
            Random rand = new Random(rc.getID());
            int offset = rand.nextInt(6);

            while (true) {
                TreeInfo[] myTrees = rc.senseNearbyTrees(1);
                if (myTrees.length < 5) {
                    for (int i = 0; i < 5; i++) {
                        Direction d = new Direction((float) Math.PI * (i + offset) / 3);
                        if (rc.canPlantTree(d)) {
                            rc.plantTree(d);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
