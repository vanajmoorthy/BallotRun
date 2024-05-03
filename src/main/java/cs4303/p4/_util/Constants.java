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

    public static final float gravity = 0.004F;
    public static final float airResistance = 0.1F;

    public static final int TILE_SIZE = 40;

    public enum PLAYER {
        INSTANCE;
        public final float X_MOVE = 0.005F;

        public final float JUMP_IMPULSE = 0.4F;

        public final float MAX_SPEED = 10;
        public final int MASS = 10;
    }


}
