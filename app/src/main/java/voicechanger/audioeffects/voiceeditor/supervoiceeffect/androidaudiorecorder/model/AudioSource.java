package voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model;

public enum AudioSource {
    MIC,
    CAMCORDER;

    static /* synthetic */ class AnonymousClass1 {
        static /* synthetic */ int[] a = null;

        static {
            a = new int[AudioSource.values().length];
            try {
                a[AudioSource.CAMCORDER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public int getSource() {
        return AnonymousClass1.a[ordinal()] != 1 ? 1 : 5;
    }
}
