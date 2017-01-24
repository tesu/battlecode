package player2;

import battlecode.common.*;
import static player2.Utils.randomDirection;

public class Archon {
    public static void run(RobotController rc) {
        System.out.println("I'm an archon!");

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                //Dodge bullets
                Utils.dodgeBullets(rc);

                //Run away from enemies
                Utils.flee(rc);

                // Attempt to build a gardener
                Direction dir = randomDirection();

                for (int i = 0; i < 25; i++) {
                    if (rc.canHireGardener(dir)) {
                        rc.hireGardener(dir);
                    }
                    dir.rotateLeftDegrees(15);
                }

                //Try to move to empty space
                Utils.moveToSpace(rc);

                //shake trees
                Utils.shakeTrees(rc);

                // Move randomly
                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 10000) {
                    rc.donate(10000);
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }
}
