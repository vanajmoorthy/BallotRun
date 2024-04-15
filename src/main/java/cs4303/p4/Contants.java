package cs4303.p4;

public class Contants {
    public final float gravity = 0.005F;
    public final float airResistance = 0.005F;

    public enum PLAYER {
        PLAYER {
            public final int tile_Color_R = 50;
            public final int tile_Color_G = 50;
            public final int tile_Color_B = 50;

            public final float xMove = 0.002F;
            public final float yMove = 0.002F;

            public final float jumpImpulse = 0.005F;
        }
    }
}
