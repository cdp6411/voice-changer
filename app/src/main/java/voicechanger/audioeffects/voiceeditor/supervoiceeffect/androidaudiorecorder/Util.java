package voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder;

import android.graphics.Color;
import android.os.Handler;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioChannel;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSampleRate;
import omrecorder.AudioSource;
import omrecorder.AudioSource.Smart;

public class Util {
    private static final Handler HANDLER = new Handler();

    private Util() {
    }

    public static String formatSeconds(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTwoDecimalsValue(i / 3600));
        stringBuilder.append(":");
        stringBuilder.append(getTwoDecimalsValue(i / 60));
        stringBuilder.append(":");
        stringBuilder.append(getTwoDecimalsValue(i % 60));
        return stringBuilder.toString();
    }

    public static int getDarkerColor(int i) {
        return Color.argb(Color.alpha(i), Math.max((int) (((float) Color.red(i)) * 0.8f), 0), Math.max((int) (((float) Color.green(i)) * 0.8f), 0), Math.max((int) (((float) Color.blue(i)) * 0.8f), 0));
    }

    public static AudioSource getMic(voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSource audioSource, AudioChannel audioChannel, AudioSampleRate audioSampleRate) {
        return new Smart(audioSource.getSource(), 2, audioChannel.getChannel(), audioSampleRate.getSampleRate());
    }

    private static String getTwoDecimalsValue(int i) {
        StringBuilder stringBuilder;
        if (i < 0 || i > 9) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(i);
            stringBuilder.append("");
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("0");
            stringBuilder.append(i);
        }
        return stringBuilder.toString();
    }

    public static boolean isBrightColor(int i) {
        if (17170445 == i) {
            return true;
        }
        int[] iArr = new int[]{Color.red(i), Color.green(i), Color.blue(i)};
        return ((int) Math.sqrt(((((double) (iArr[0] * iArr[0])) * 0.241d) + (((double) (iArr[1] * iArr[1])) * 0.691d)) + (((double) (iArr[2] * iArr[2])) * 0.068d))) >= 200;
    }

    public static void wait(int i, Runnable runnable) {
        HANDLER.postDelayed(runnable, (long) i);
    }
}
