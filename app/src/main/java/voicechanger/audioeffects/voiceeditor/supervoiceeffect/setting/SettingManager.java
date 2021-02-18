package voicechanger.audioeffects.voiceeditor.supervoiceeffect.setting;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SettingManager implements ISettingConstants {
    public static final String DOBAO_SHARPREFS = "ypy_prefs";
    public static final String TAG = "SettingManager";

    public static int getCurrentAccentColor(Context context) {
        return Integer.parseInt(getSetting(context, ISettingConstants.KEY_COLOR_ACCENT, "0"));
    }



    public static String getSetting(Context context, String str, String str2) {
        return context.getSharedPreferences(DOBAO_SHARPREFS, 0).getString(str, str2);
    }

    public static void saveSetting(Context context, String str, String str2) {
        Editor edit = context.getSharedPreferences(DOBAO_SHARPREFS, 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }


}
