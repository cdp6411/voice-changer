package voicechanger.audioeffects.voiceeditor.supervoiceeffect.object;

public class EffectObject {
    private float[] eq;
    private String id;
    private boolean isEcho;
    private boolean isFlanger;
    private boolean isPlaying;
    private boolean isReverse;
    private String name;
    private int pitch;
    private float rate;
    private float[] reverb;

    public EffectObject(String str, String str2) {
        this.id = str;
        this.name = str2;
    }

    public EffectObject(String str, String str2, int i, float f) {
        this.id = str;
        this.name = str2;
        this.pitch = i;
        this.rate = f;
    }

    public float[] getEq() {
        return this.eq;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPitch() {
        return this.pitch;
    }

    public float getRate() {
        return this.rate;
    }

    public float[] getReverb() {
        return this.reverb;
    }

    public boolean isEcho() {
        return this.isEcho;
    }

    public boolean isFlanger() {
        return this.isFlanger;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public boolean isReverse() {
        return this.isReverse;
    }

    public void setEcho(boolean z) {
        this.isEcho = z;
    }

    public void setEq(float[] fArr) {
        this.eq = fArr;
    }

    public void setFlanger(boolean z) {
        this.isFlanger = z;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPitch(int i) {
        this.pitch = i;
    }

    public void setPlaying(boolean z) {
        this.isPlaying = z;
    }

    public void setRate(float f) {
        this.rate = f;
    }

    public void setReverb(float[] fArr) {
        this.reverb = fArr;
    }

    public void setReverse(boolean z) {
        this.isReverse = z;
    }
}
