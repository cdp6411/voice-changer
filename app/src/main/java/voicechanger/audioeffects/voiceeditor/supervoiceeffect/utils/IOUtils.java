package voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtils {

    public static String readStringFromAssets(Context context, String str) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str)));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                str = bufferedReader.readLine();
                if (str == null) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(str);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
