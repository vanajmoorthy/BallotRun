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

    public final static int black = 0xff030712;
    public final static int white = 0xfff9fafb;

    public final static Swatch darkGray = new Swatch(
        0xff52525b,
        0xff3f3f46,
        0xff27272a,
        0xff18181b,
        0xff09090b
    );

    public final static Swatch slate = new Swatch(
        0xffcbd5e1,
        0xff94a3b8,
        0xff64748b,
        0xff475569,
        0xff334155
    );

    public final static Swatch gray = new Swatch(
        0xffd1d5db,
        0xff9ca3af,
        0xff6b7280,
        0xff4b5563,
        0xff374151
    );

    public final static Swatch zink = new Swatch(
        0xffd4d4d8,
        0xffa1a1aa,
        0xff71717a,
        0xff52525b,
        0xff3f3f46
    );

    public final static Swatch neutral = new Swatch(
        0xffd4d4d4,
        0xffa3a3a3,
        0xff737373,
        0xff525252,
        0xff404040
    );

    public final static Swatch stone = new Swatch(
        0xffd6d3d1,
        0xffa8a29e,
        0xff78716c,
        0xff57534e,
        0xff44403c
    );

    public final static Swatch red = new Swatch(
        0xfffca5a5,
        0xfff87171,
        0xffef4444,
        0xffdc2626,
        0xffb91c1c
    );

    public final static Swatch orange = new Swatch(
        0xfffdba74,
        0xfffb923c,
        0xfff97316,
        0xffea580c,
        0xffc2410c
    );

    public final static Swatch amber = new Swatch(
        0xfffcd34d,
        0xfffbbf24,
        0xfff59e0b,
        0xffd97706,
        0xffb45309
    );

    public final static Swatch yellow = new Swatch(
        0xfffde047,
        0xfffacc15,
        0xffeab308,
        0xffca8a04,
        0xffa16207
    );

    public final static Swatch lime = new Swatch(
        0xffbef264,
        0xffa3e635,
        0xff84cc16,
        0xff65a30d,
        0xff4d7c0f
    );

    public final static Swatch green = new Swatch(
        0xff86efac,
        0xff4ade80,
        0xff22c55e,
        0xff16a34a,
        0xff15803d
    );

    public final static Swatch emerald = new Swatch(
        0xff6ee7b7,
        0xff34d399,
        0xff10b981,
        0xff059669,
        0xff047857
    );

    public final static Swatch teal = new Swatch(
        0xff5eead4,
        0xff2dd4bf,
        0xff14b8a6,
        0xff0d9488,
        0xff0f766e
    );

    public final static Swatch cyan = new Swatch(
        0xff67e8f9,
        0xff22d3ee,
        0xff06b6d4,
        0xff0891b2,
        0xff0e7490
    );

    public final static Swatch sky = new Swatch(
        0xff7dd3fc,
        0xff38bdf8,
        0xff0ea5e9,
        0xff0284c7,
        0xff0369a1
    );

    public final static Swatch blue = new Swatch(
        0xff93c5fd,
        0xff60a5fa,
        0xff3b82f6,
        0xff2563eb,
        0xff1d4ed8
    );

    public final static Swatch indigo = new Swatch(
        0xffa5b4fc,
        0xff818cf8,
        0xff6366f1,
        0xff4f46e5,
        0xff4338ca
    );

    public final static Swatch violet = new Swatch(
        0xffc4b5fd,
        0xffa78bfa,
        0xff8b5cf6,
        0xff7c3aed,
        0xff6d28d9
    );

    public final static Swatch purple = new Swatch(
        0xffd8b4fe,
        0xffc084fc,
        0xffa855f7,
        0xff9333ea,
        0xff7e22ce
    );

    public final static Swatch fuschia = new Swatch(
        0xfff0abfc,
        0xffe879f9,
        0xffd946ef,
        0xffc026d3,
        0xffa21caf
    );

    public final static Swatch pink = new Swatch(
        0xfff9a8d4,
        0xfff472b6,
        0xffec4899,
        0xffdb2777,
        0xffbe185d
    );

    public final static Swatch rose = new Swatch(
        0xfffda4af,
        0xfffb7185,
        0xfff43f5e,
        0xffe11d48,
        0xffbe123c
    );
}
