package voicechanger.audioeffects.voiceeditor.supervoiceeffect.soundMng;

import android.content.Context;
import android.media.SoundPool;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.DBLog;
import java.io.IOException;
import java.util.ArrayList;

public class SoundManager {
    private static final int MAX_STREAM = 100;
    private static final int NORMAL_PIORITY = 1;
    private static final int QUALITY = 0;
    private static final String TAG = "SoundManager";
    private static ArrayList<SoundObject> mListSoundObjects = null;
    private static SoundManager mSingletonSoundMng = null;
    private static SoundPool mSoundPool = null;
    private static float mVolume = 1.0f;

    private SoundManager() {
        if (mSoundPool == null) {
            mSoundPool = new SoundPool(100, 3, 0);
            mListSoundObjects = new ArrayList();
        }
    }

    public static SoundManager getInstance() {
        if (mSingletonSoundMng == null) {
            mSingletonSoundMng = new SoundManager();
        }
        return mSingletonSoundMng;
    }

    private SoundObject getSound(int i) {
        if (mSoundPool == null || mListSoundObjects == null) {
            return null;
        }
        int size = mListSoundObjects.size();
        if (size == 0) {
            return null;
        }
        for (int i2 = 0; i2 < size; i2++) {
            SoundObject soundObject = (SoundObject) mListSoundObjects.get(i2);
            if (soundObject.getResId() != -1 && i == soundObject.getResId()) {
                return soundObject;
            }
        }
        return null;
    }

    private SoundObject getSound(String str) {
        if (mSoundPool == null || mListSoundObjects == null) {
            return null;
        }
        int size = mListSoundObjects.size();
        if (size == 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            SoundObject soundObject = (SoundObject) mListSoundObjects.get(i);
            if (soundObject.getNameResource() != null && soundObject.getNameResource().equals(str)) {
                return soundObject;
            }
        }
        return null;
    }

    public void addSound(Context context, int i) {
        if (mSoundPool != null && mListSoundObjects != null && getSound(i) == null) {
            mListSoundObjects.add(new SoundObject(i, mSoundPool.load(context, i, 1)));
        }
    }

    public void addSound(Context context, String str) {
        if (!(mSoundPool == null || mListSoundObjects == null || getSound(str) != null)) {
            try {
                mListSoundObjects.add(new SoundObject(str, mSoundPool.load(context.getResources().getAssets().openFd(str), 1)));
            } catch (IOException e) {
                DBLog.m12e(TAG, "ErrorWhenLoadSoundFromAsset");
                e.printStackTrace();
            }
        }
    }

    public void pauseSounds() {
        if (mSoundPool != null) {
            mSoundPool.autoPause();
        }
    }

    public void play(Context context, int i) {
        if (mSoundPool != null) {
            SoundObject sound = getSound(i);
            if (sound != null) {
                mSoundPool.play(sound.getSoundId(), mVolume, mVolume, 1, 0, 1.0f);
            }
        }
    }

    public void play(Context context, int i, float f) {
        if (mSoundPool != null) {
            SoundObject sound = getSound(i);
            if (sound != null) {
                mSoundPool.play(sound.getSoundId(), mVolume, mVolume, 1, 0, f);
            }
        }
    }

    public void play(Context context, int i, int i2) {
        if (mSoundPool != null) {
            SoundObject sound = getSound(i);
            if (sound != null) {
                mSoundPool.play(sound.getSoundId(), mVolume, mVolume, 1, i2, 1.0f);
            }
        }
    }

    public void play(Context context, String str) {
        if (mSoundPool != null) {
            SoundObject sound = getSound(str);
            if (sound != null) {
                mSoundPool.play(sound.getSoundId(), mVolume, mVolume, 1, 0, 1.0f);
            }
        }
    }

    public void play(Context context, String str, float f) {
        if (mSoundPool != null) {
            SoundObject sound = getSound(str);
            if (sound != null) {
                mSoundPool.play(sound.getSoundId(), mVolume, mVolume, 1, 0, f);
            }
        }
    }

    public void releaseSound() {
        if (mSoundPool != null) {
            DBLog.m11d(TAG, "------>DadestroySOund");
            mSoundPool.release();
            mSoundPool = null;
        }
        if (mListSoundObjects != null) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("------>DadestroySOundObject=");
            stringBuilder.append(mListSoundObjects.size());
            DBLog.m11d(str, stringBuilder.toString());
            mListSoundObjects.clear();
            mListSoundObjects = null;
        }
        mSingletonSoundMng = null;
    }

    public void resumeSounds() {
        if (mSoundPool != null) {
            mSoundPool.autoResume();
        }
    }

    public void setVolumne(float f) {
        mVolume = f;
    }
}
