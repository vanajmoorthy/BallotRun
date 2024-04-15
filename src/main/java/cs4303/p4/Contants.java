package cs4303.p4;

public class Contants {
    public static final float gravity = 0.005F;
    public static final float airResistance = 0.005F;



    public enum PLAYER {
        INSTANCE;

        public final int TILE_COLOR_R = 50;
        public final int TILE_COLOR_G = 50;
        public final int TILE_COLOR_B = 50;

        public final float X_MOVE = 0.002F;
        public final float Y_MOVE = 0.002F;

        public final float JUMP_IMPULSE = 0.005F;
        public final int MASS = 10;
    }
}
