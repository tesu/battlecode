package player2;

import battlecode.common.*;
import static player2.Utils.randomDirection;

public class Lumberjack {
    public static void run(RobotController rc) {
        System.out.println("I'm a lumberjack!");
        Team enemy = rc.getTeam().opponent();

        while (true) {
            try {
                RobotInfo[] robots = rc.senseNearbyRobots(RobotType.LUMBERJACK.bodyRadius+GameConstants.LUMBERJACK_STRIKE_RADIUS, enemy);

                if(robots.length > 0 && !rc.hasAttacked()) {
                    rc.strike();
                } else {
                    robots = rc.senseNearbyRobots(-1,enemy);

                    if(robots.length > 0) {
                        MapLocation myLocation = rc.getLocation();
                        MapLocation enemyLocation = robots[0].getLocation();
                        Direction toEnemy = myLocation.directionTo(enemyLocation);

                        Utils.tryMove(rc, toEnemy);
                    } else {
                        Utils.tryMove(rc, randomDirection());
                    }
                }

                Clock.yield();
            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }
    }
}
