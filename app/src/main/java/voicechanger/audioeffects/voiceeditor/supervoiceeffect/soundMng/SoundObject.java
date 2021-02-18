package voicechanger.audioeffects.voiceeditor.supervoiceeffect.soundMng;

public class SoundObject {
    public static final int RES_ID_ERROR = -1;
    private String nameResource;
    private int resId = -1;
    private int soundId;

    public SoundObject(int i, int i2) {
        this.resId = i;
        this.soundId = i2;
    }

    public SoundObject(String str, int i) {
        this.nameResource = str;
        this.soundId = i;
    }

    public String getNameResource() {
        return this.nameResource;
    }

    public int getResId() {
        return this.resId;
    }

    public int getSoundId() {
        return this.soundId;
    }

    public void setNameResource(String str) {
        this.nameResource = str;
    }

    public void setResId(int i) {
        this.resId = i;
    }

    public void setSoundId(int i) {
        this.soundId = i;
    }
}
