package player2;

import battlecode.common.*;
import static player2.Utils.randomDirection;

public class Soldier {
    public static void run(RobotController rc) {
        System.out.println("I'm a soldier!");

        while (true) {
            try {
                Utils.dodgeBullets(rc);
                Utils.attack(rc);
                Utils.shakeTrees(rc);

                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 200){
                    rc.donate(50);
                }

                Clock.yield();
            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
            }
        }
    }
}
