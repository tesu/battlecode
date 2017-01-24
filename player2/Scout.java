package player2;

import battlecode.common.*;
import static player2.Utils.randomDirection;

public class Scout {
    public static void run(RobotController rc) {

        System.out.println("I'm a scout!");
        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                //Dodge bullets
                Utils.dodgeBullets(rc);

                //attack
                Utils.attack(rc);

                //shake trees
                Utils.shakeTrees(rc);

                // Move randomly
                if (rc.getMoveCount() == 0) {
                    Utils.tryMove(rc, randomDirection());
                }

                if (rc.getTeamBullets() >= 200){
                    rc.donate(50);
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
            }
        }
    }
}
