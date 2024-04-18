package cs4303.p4._util;

import lombok.AllArgsConstructor;

public class Colors {
    @AllArgsConstructor
    public static class Swatch {
        public final int lighter;
        public final int light;
        public final int primary;
        public final int dark;
        public final int darker;
    }

    public final static Swatch night = new Swatch(
        0xFF2F3437,
        0xFF26292C,
        0xFF1C1F21,
        0xFF131516,
        0xFF090A0B
    );

    public final static Swatch platinum = new Swatch(
        0xFFF4F5F6,
        0xFFE9EAEC,
        0xFFDEE0E3,
        0xFFD3D6D9,
        0xFFC8CBD0
    );

    public final static Swatch swamp = new Swatch(
        0xFF496E5C,
        0xFF416252,
        0xFF344E41,
        0xFF31493D,
        0xFF293D33
    );

    public final static Swatch forest = new Swatch(
        0xFF507C58,
        0xFF48704F,
        0xFF3A5A40,
        0xFF38573D,
        0xFF304B35
    );

    public final static Swatch fern = new Swatch(
        0xFF6E9C6D,
        0xFF649263,
        0xFF588157,
        0xFF537953,
        0xFF4B6D4A
    );

    public final static Swatch sage = new Swatch(
        0xFFBBC6A9,
        0xFFB2BE9D,
        0xFFA3B18A,
        0xFF9EAD85,
        0xFF95A578
    );

    public final static Swatch murrey = new Swatch(
        0xFFB50353,
        0xFFA1024A,
        0xFF89023E,
        0xFF790237,
        0xFF64022E
    );

    public final static Swatch yellow = new Swatch(
        0xFFFFCC33,
        0xFFFFC71F,
        0xFFFFC40C,
        0xFFF5B800,
        0xFFE0A800
    );

    public final static Swatch cerulean = new Swatch(
        0xFF00A5E0,
        0xFF0096CC,
        0xFF007BA7,
        0xFF0078A3,
        0xFF00698F
    );
}
