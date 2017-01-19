package skeleton;

import battlecode.common.*;

public class Utils {
    public static class RobotAnalysis {
        public int archons = 0;
        public int gardeners = 0;
        public int lumberjacks = 0;
        public int scouts = 0;
        public int soldiers = 0;
        public int tanks = 0;

        public RobotAnalysis(RobotInfo[] robots) {
            for (RobotInfo robot : robots) {
                switch (robot.getType()) {
                    case ARCHON:
                        archons++;
                        break;
                    case GARDENER:
                        gardeners++;
                        break;
                    case LUMBERJACK:
                        lumberjacks++;
                        break;
                    case SCOUT:
                        scouts++;
                        break;
                    case SOLDIER:
                        soldiers++;
                        break;
                    case TANK:
                        tanks++;
                        break;
                }
            }
        }
    }
}
