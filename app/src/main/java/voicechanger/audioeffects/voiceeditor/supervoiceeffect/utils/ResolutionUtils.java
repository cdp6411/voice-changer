package voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

public class ResolutionUtils {

    public static int[] getDeviceResolution(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        int width = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();
        int i = activity.getResources().getConfiguration().orientation;
        int[] iArr;
        int i2;
        if (i == 1) {
            iArr = new int[2];
            i2 = height >= width ? width : height;
            if (height <= width) {
                height = width;
            }
            iArr[0] = i2;
            iArr[1] = height;
            return iArr;
        } else if (i != 2) {
            return null;
        } else {
            iArr = new int[2];
            i2 = height <= width ? width : height;
            if (height >= width) {
                height = width;
            }
            iArr[0] = i2;
            iArr[1] = height;
            return iArr;
        }
    }
}
