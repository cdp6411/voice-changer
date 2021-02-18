package voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constant {

    public static Boolean CheckNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
