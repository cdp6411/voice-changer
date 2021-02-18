package voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder;

import com.cleveroad.audiovisualization.DbmHandler;

public class VisualizerHandler extends DbmHandler<Float> {
    protected void onDataReceivedImpl(Float f, int i, float[] fArr, float[] fArr2) {
        float f2 = 0;
        f = Float.valueOf(f.floatValue() / 100.0f);
        if (((double) f.floatValue()) <= 0.5d) {
            f2 = 0.0f;
        } else if (((double) f.floatValue()) > 0.5d && ((double) f.floatValue()) <= 0.6d) {
            f2 = 0.2f;
        } else if (((double) f.floatValue()) <= 0.6d || ((double) f.floatValue()) > 0.7d) {
            if (((double) f.floatValue()) > 0.7d) {
                f2 = 1.0f;
            }
            fArr[0] = f.floatValue();
            fArr2[0] = f.floatValue();
        } else {
            f2 = 0.6f;
        }
        f = Float.valueOf(f2);
        try {
            fArr[0] = f.floatValue();
            fArr2[0] = f.floatValue();
        } catch (Exception unused) {
        }
    }

    public void stop() {
        try {
            calmDownAndStopRendering();
        } catch (Exception unused) {
        }
    }
}
