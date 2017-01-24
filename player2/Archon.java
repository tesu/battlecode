package player2;

import battlecode.common.*;
import static player2.Utils.randomDirection;

public class Archon {
    public static void run(RobotController rc) {
        System.out.println("I'm an archon!");

        while (true) {
            try {
                Utils.dodgeBullets(rc);

                Utils.flee(rc);

                Direction dir = randomDirection();

                for (int i = 0; i < 25; i++) {
                    if (rc.canHireGardener(dir)) {
                        rc.hireGardener(dir);
                    }
                    dir.rotateLeftDegrees(15);
                }

                Utils.moveToSpace(rc);
                Utils.shakeTrees(rc);

                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 10000) {
                    rc.donate(10000);
                }

                Clock.yield();
            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }
}
