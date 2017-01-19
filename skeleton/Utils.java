package skeleton;

import battlecode.common.*;

public class Utils {
    public class RobotAnalysis {
        int archons = 0;
        int gardeners = 0;
        int lumberjacks = 0;
        int scouts = 0;
        int soldiers = 0;
        int tanks = 0;

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

        public int archons() {
            return archons;
        }

        public int gardeners() {
            return gardeners;
        }

        public int lumberjacks() {
            return lumberjacks;
        }

        public int scouts() {
            return scouts;
        }

        public int soldiers() {
            return soldiers;
        }

        public int tanks() {
            return tanks;
        }
    }
}
