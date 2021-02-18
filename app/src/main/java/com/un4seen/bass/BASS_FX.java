package com.un4seen.bass;

public class BASS_FX {
    public static final int BASS_ATTRIB_REVERSE_DIR = 69632;

    static {
        System.loadLibrary("bass_fx");
    }

    public static native int BASS_FX_ReverseCreate(int i, float f, int i2);

    public static native int BASS_FX_TempoCreate(int i, int i2);

    public static native int BASS_FX_TempoGetSource(int i);
}
