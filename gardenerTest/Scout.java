package gardenerTest;

import battlecode.common.*;
import skeleton.Utils;

import java.util.Random;

public class Scout {
    public static void run(RobotController rc) {
        Random rand = new Random(rc.getID());

        Direction face = null;
        for (RobotInfo robot : rc.senseNearbyRobots(2, rc.getTeam())) {
            if (robot.getType() == RobotType.GARDENER) {
                face = robot.getLocation().directionTo(rc.getLocation());
            }
        }
        if (face == null) face = new Direction(2 * (float)Math.PI * rand.nextFloat());

        while (true) {
            try {
                Utils.alwaysRun(rc);

                if (rc.canMove(face)) {
                    rc.move(face);
                }

                Clock.yield();
            } catch (GameActionException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
