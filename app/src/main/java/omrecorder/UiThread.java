package omrecorder;

import android.os.Handler;
import android.os.Looper;

final class UiThread implements ThreadAction {
    private static final Handler handler = new Handler(Looper.getMainLooper());

    UiThread() {
    }

    public void execute(Runnable runnable) {
        handler.post(runnable);
    }
}
