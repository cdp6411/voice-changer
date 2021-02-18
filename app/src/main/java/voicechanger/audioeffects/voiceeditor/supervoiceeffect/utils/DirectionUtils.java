package voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils;

import android.app.Activity;
import android.content.Intent;

public class DirectionUtils {
    public static void changeActivity(Activity activity, int i, int i2, boolean z, Intent intent) {
        if (activity != null && intent != null) {
            activity.startActivity(intent);
            activity.overridePendingTransition(i, i2);
            if (z) {
                activity.finish();
            }
        }
    }
}
