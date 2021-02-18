package voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model;

public enum AudioChannel {
    STEREO,
    MONO;

    static /* synthetic */ class AnonymousClass1 {
        static /* synthetic */ int[] a = null;

        static {
            a = new int[AudioChannel.values().length];
            try {
                a[AudioChannel.MONO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public int getChannel() {
        return AnonymousClass1.a[ordinal()] != 1 ? 12 : 16;
    }
}
