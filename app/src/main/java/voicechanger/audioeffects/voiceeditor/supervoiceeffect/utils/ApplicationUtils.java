package voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ApplicationUtils {

    public static void hiddenVirtualKeyboard(Context context, View view) {
        try {
            ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
