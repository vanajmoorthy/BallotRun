package cs4303.p4._util;

public final class Constants {
    public static final class Screen {
        public static final int width = 1000;
        public static final int height = 600;

        public static final int minWidth = 500;
        public static final int minHeight = 500;

        public static final class Base {
            public static final int inventoryWidth = 1000;
            public static final int inventoryHeight = 600;
        }
    }

    public static final float gravity = 0.005F;
    public static final float airResistance = 0.005F;

    public static final int TILE_SIZE = 40;

    public enum PLAYER {
        INSTANCE;

        public final int TILE_COLOR_R = 50;
        public final int TILE_COLOR_G = 50;
        public final int TILE_COLOR_B = 50;

        public final float X_MOVE = 0.002F;
        public final float Y_MOVE = 0.002F;
        public final float WALL_IMPULSE = 25;

        public final float JUMP_IMPULSE = 10;
        public final int MASS = 10;
    }


}
