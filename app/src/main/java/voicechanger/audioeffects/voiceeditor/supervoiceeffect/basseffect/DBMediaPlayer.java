package voicechanger.audioeffects.voiceeditor.supervoiceeffect.basseffect;

import android.os.Handler;
import android.os.Message;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.DBLog;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.StringUtils;
import com.un4seen.bass.BASS;
import com.un4seen.bass.BASS.BASS_CHANNELINFO;
import com.un4seen.bass.BASS.BASS_DX8_ECHO;
import com.un4seen.bass.BASS.BASS_DX8_FLANGER;
import com.un4seen.bass.BASS.BASS_DX8_PARAMEQ;
import com.un4seen.bass.BASS.BASS_DX8_REVERB;
import com.un4seen.bass.BASS_FX;
import com.un4seen.bass.BASSenc;
import java.nio.ByteBuffer;
import java.util.Locale;

public class DBMediaPlayer implements IDBMediaConstants {
    public static final int FLABUFLEN = 350;
    private static final String TAG = "DBMediaPlayer";
    private int currrentPostion = 0;
    private int duration = 0;
    private boolean isPausing = false;
    private boolean isPlaying = false;
    private boolean isReverse;
    private int mChanPlay;
    private IDBMediaListener mDBMediaListener;
    private int mFxEQEffect;
    private int mFxEchoEffect;
    private int mFxFlangerEffect;
    private int mFxReverbEffect;
    private Handler mHandler = new C02141();
    private String mMediaPath;

    class C02141 extends Handler {
        C02141() {
        }

        public void handleMessage(Message message) {
            DBMediaPlayer.this.currrentPostion = DBMediaPlayer.this.getChannelPosition();
            DBMediaPlayer.this.duration = DBMediaPlayer.this.getChannelLength();
            if (DBMediaPlayer.this.isReverse) {
                if (DBMediaPlayer.this.currrentPostion <= 0) {
                    removeMessages(0);
                    if (DBMediaPlayer.this.mDBMediaListener != null) {
                        DBMediaPlayer.this.mDBMediaListener.onMediaCompletion();
                    }
                    return;
                }
            } else if (DBMediaPlayer.this.currrentPostion >= DBMediaPlayer.this.duration) {
                removeMessages(0);
                if (DBMediaPlayer.this.mDBMediaListener != null) {
                    DBMediaPlayer.this.mDBMediaListener.onMediaCompletion();
                }
                return;
            }
            sendEmptyMessageDelayed(0, 50);
        }
    }

    public DBMediaPlayer(String str) {
        this.mMediaPath = str;
    }

    private int getChannelLength() {
        return this.mChanPlay != 0 ? (int) BASS.BASS_ChannelBytes2Seconds(this.mChanPlay, BASS.BASS_ChannelGetLength(this.mChanPlay, 0)) : 0;
    }

    private int getChannelPosition() {
        return this.mChanPlay != 0 ? (int) BASS.BASS_ChannelBytes2Seconds(this.mChanPlay, BASS.BASS_ChannelGetPosition(this.mChanPlay, 0)) : -1;
    }

    private void initMedia() {
        Exception exception;
        BASS.BASS_StreamFree(this.mChanPlay);
        if (!StringUtils.isEmptyString(this.mMediaPath)) {
            this.mChanPlay = BASS.BASS_StreamCreateFile(this.mMediaPath, 0, 0, 2097152);
        }
        StringBuilder stringBuilder;
        if (this.mChanPlay != 0) {
            this.mChanPlay = BASS_FX.BASS_FX_ReverseCreate(this.mChanPlay, 2.0f, 2162688);
            if (this.mChanPlay != 0) {
                BASS.BASS_ChannelGetInfo(this.mChanPlay, new BASS_CHANNELINFO());
                this.mChanPlay = BASS_FX.BASS_FX_TempoCreate(this.mChanPlay, 65536);
                if (this.mChanPlay == 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(TAG);
                    stringBuilder.append(" Couldnt create a resampled stream!");
                    exception = new Exception(stringBuilder.toString());
                } else {
                    return;
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(TAG);
            stringBuilder.append(" Couldnt create a resampled stream!");
            exception = new Exception(stringBuilder.toString());
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append(TAG);
            stringBuilder.append(" Couldnt create a resampled stream!");
            exception = new Exception(stringBuilder.toString());
        }
        exception.printStackTrace();
        BASS.BASS_StreamFree(this.mChanPlay);
    }

    public int getCurrrentPostion() {
        return this.currrentPostion;
    }

    public int getDuration() {
        if (this.mChanPlay != 0) {
            this.duration = getChannelLength();
        }
        return this.duration;
    }

    public boolean initMediaToSave() {
        BASS.BASS_StreamFree(this.mChanPlay);
        this.mChanPlay = BASS.BASS_StreamCreateFile(this.mMediaPath, 0, 0, 2097152);
        if (this.mChanPlay != 0) {
            this.mChanPlay = BASS_FX.BASS_FX_ReverseCreate(this.mChanPlay, 2.0f, 2097152);
            StringBuilder stringBuilder;
            if (this.mChanPlay != 0) {
                this.mChanPlay = BASS_FX.BASS_FX_TempoCreate(this.mChanPlay, 2097152);
                if (this.mChanPlay != 0) {
                    return true;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(TAG);
                stringBuilder.append(" Couldnt create a resampled stream!");
                new Exception(stringBuilder.toString()).printStackTrace();
                BASS.BASS_StreamFree(this.mChanPlay);
                return false;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(TAG);
            stringBuilder.append(" Couldnt create a resampled stream!");
            new Exception(stringBuilder.toString()).printStackTrace();
            BASS.BASS_StreamFree(this.mChanPlay);
        }
        return false;
    }

    public boolean isPausing() {
        return this.isPausing ^ true;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void pauseAudio() {
        if (this.isPlaying) {
            this.isPausing = true;
            if (this.mChanPlay != 0) {
                BASS.BASS_ChannelPause(this.mChanPlay);
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAG);
        stringBuilder.append(" pauseAudio:HanetMediaPlayer not init");
        new Exception(stringBuilder.toString()).printStackTrace();
    }

    public boolean prepareAudio() {
        if (!StringUtils.isEmptyString(this.mMediaPath)) {
            if (this.mMediaPath.toLowerCase(Locale.getDefault()).endsWith(IDBMediaConstants.TYPE_MP3) || this.mMediaPath.toLowerCase(Locale.getDefault()).endsWith(IDBMediaConstants.TYPE_WAV) || this.mMediaPath.toLowerCase(Locale.getDefault()).endsWith(IDBMediaConstants.TYPE_OGG) || this.mMediaPath.toLowerCase(Locale.getDefault()).endsWith(IDBMediaConstants.TYPE_FLAC)) {
                initMedia();
                return true;
            }
            new Exception("DBMidiPlayer:can not support file format").printStackTrace();
        }
        return false;
    }

    public void releaseAudio() {
        this.mHandler.removeMessages(0);
        if (this.mMediaPath != null) {
            if (this.mFxReverbEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxReverbEffect);
            }
            if (this.mFxFlangerEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxFlangerEffect);
            }
            if (this.mFxEchoEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxEchoEffect);
            }
            if (this.mFxEQEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxEQEffect);
            }
            this.isPlaying = false;
            this.isPausing = false;
            BASS.BASS_StreamFree(this.mChanPlay);
        }
    }

    public void resumeAudio() {
        if (this.isPausing) {
            this.isPausing = false;
            if (this.mChanPlay != 0) {
                BASS.BASS_ChannelPlay(this.mChanPlay, false);
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAG);
        stringBuilder.append(" resumeAudio:HanetMediaPlayer is playing");
        new Exception(stringBuilder.toString()).printStackTrace();
    }

    public void saveToFile(String str) {
        if (!(StringUtils.isEmptyString(str) || this.mChanPlay == 0 || BASSenc.BASS_Encode_Start(this.mChanPlay, str, 262208, null, Integer.valueOf(0)) == 0)) {
            try {
                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(20000);
                int BASS_ChannelGetData;
                do {
                    BASS_ChannelGetData = BASS.BASS_ChannelGetData(this.mChanPlay, allocateDirect, allocateDirect.capacity());
                    if (BASS_ChannelGetData == -1) {
                        return;
                    }
                } while (BASS_ChannelGetData != 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void seekChannelTo(int i) {
        if (this.mChanPlay != 0) {
            BASS.BASS_ChannelSetPosition(this.mChanPlay, BASS.BASS_ChannelSeconds2Bytes(this.mChanPlay, (double) i), 0);
        }
    }

    public void seekTo(int i) {
        if (this.isPlaying) {
            this.currrentPostion = i;
            seekChannelTo(i);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAG);
        stringBuilder.append(" seekTo:HanetMediaPlayer is not playing");
        new Exception(stringBuilder.toString()).printStackTrace();
    }

    public void setAudioEQ(float[] fArr) {
        if (this.mChanPlay != 0) {
            if (fArr != null) {
                if (this.mFxEQEffect == 0) {
                    this.mFxEQEffect = BASS.BASS_ChannelSetFX(this.mChanPlay, 7, 0);
                }
                if (this.mFxEQEffect != 0) {
                    BASS_DX8_PARAMEQ bass_dx8_parameq = new BASS_DX8_PARAMEQ();
                    BASS.BASS_FXGetParameters(this.mFxEQEffect, bass_dx8_parameq);
                    bass_dx8_parameq.fCenter = fArr[0];
                    bass_dx8_parameq.fBandwidth = fArr[1];
                    bass_dx8_parameq.fGain = fArr[2];
                    BASS.BASS_FXSetParameters(this.mFxEQEffect, bass_dx8_parameq);
                }
            } else if (this.mFxEQEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxEQEffect);
                this.mFxEQEffect = 0;
            }
        }
    }

    public void setAudioEcho(boolean z) {
        if (this.mChanPlay != 0) {
            if (z) {
                if (this.mFxEchoEffect == 0) {
                    this.mFxEchoEffect = BASS.BASS_ChannelSetFX(this.mChanPlay, 3, 0);
                }
                if (this.mFxEchoEffect != 0) {
                    BASS_DX8_ECHO bass_dx8_echo = new BASS_DX8_ECHO();
                    bass_dx8_echo.fLeftDelay = 1000.0f;
                    bass_dx8_echo.fRightDelay = 1000.0f;
                    bass_dx8_echo.fFeedback = 50.0f;
                    BASS.BASS_FXSetParameters(this.mChanPlay, Integer.valueOf(this.mFxEchoEffect));
                }
            } else if (this.mFxEchoEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxEchoEffect);
                this.mFxEchoEffect = 0;
            }
        }
    }

    public void setAudioPitch(int i) {
        if (this.mChanPlay != 0) {
            BASS.BASS_ChannelSetAttribute(this.mChanPlay, 65537, (float) i);
        }
    }

    public void setAudioRate(float f) {
        if (this.mChanPlay != 0) {
            BASS.BASS_ChannelSetAttribute(this.mChanPlay, 65536, f);
        }
    }

    public void setAudioReverb(float[] fArr) {
        if (this.mChanPlay != 0) {
            if (fArr != null) {
                if (this.mFxReverbEffect == 0) {
                    this.mFxReverbEffect = BASS.BASS_ChannelSetFX(this.mChanPlay, 8, 0);
                }
                if (this.mFxReverbEffect != 0) {
                    BASS_DX8_REVERB bass_dx8_reverb = new BASS_DX8_REVERB();
                    BASS.BASS_FXGetParameters(this.mFxReverbEffect, bass_dx8_reverb);
                    bass_dx8_reverb.fReverbMix = fArr[0];
                    bass_dx8_reverb.fReverbTime = fArr[1];
                    bass_dx8_reverb.fHighFreqRTRatio = fArr[2];
                    BASS.BASS_FXSetParameters(this.mFxReverbEffect, bass_dx8_reverb);
                }
            } else if (this.mFxReverbEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxReverbEffect);
                this.mFxReverbEffect = 0;
            }
        }
    }

    public void setChannelVolumne(float f) {
        if (this.mChanPlay != 0) {
            BASS.BASS_ChannelSetAttribute(this.mChanPlay, 2, f);
        }
    }

    public void setFlangerEffect(boolean z) {
        if (this.mChanPlay != 0) {
            if (z) {
                if (this.mFxFlangerEffect == 0) {
                    this.mFxFlangerEffect = BASS.BASS_ChannelSetFX(this.mChanPlay, 4, 0);
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===========>mFxFlangerEffect=");
                stringBuilder.append(this.mFxFlangerEffect);
                DBLog.m11d(str, stringBuilder.toString());
                if (this.mFxFlangerEffect != 0) {
                    BASS_DX8_FLANGER bass_dx8_flanger = new BASS_DX8_FLANGER();
                    BASS.BASS_FXGetParameters(this.mFxFlangerEffect, bass_dx8_flanger);
                    bass_dx8_flanger.fWetDryMix = 50.0f;
                    bass_dx8_flanger.fDepth = 100.0f;
                    bass_dx8_flanger.fFeedback = 80.0f;
                    bass_dx8_flanger.fDelay = 10.0f;
                    bass_dx8_flanger.lPhase = 3;
                    BASS.BASS_FXSetParameters(this.mFxFlangerEffect, bass_dx8_flanger);
                }
            } else if (this.mFxFlangerEffect != 0) {
                BASS.BASS_ChannelRemoveFX(this.mChanPlay, this.mFxFlangerEffect);
                this.mFxFlangerEffect = 0;
            }
        }
    }

    public void setOnDBMediaListener(IDBMediaListener iDBMediaListener) {
        this.mDBMediaListener = iDBMediaListener;
    }

    public void setReverse(boolean z) {
        this.isReverse = z;
        if (this.mChanPlay != 0) {
            int BASS_FX_TempoGetSource = BASS_FX.BASS_FX_TempoGetSource(this.mChanPlay);
            BASS.BASS_ChannelGetAttribute(BASS_FX_TempoGetSource, BASS_FX.BASS_ATTRIB_REVERSE_DIR, Float.valueOf(0.0f));
            BASS.BASS_ChannelSetAttribute(BASS_FX_TempoGetSource, BASS_FX.BASS_ATTRIB_REVERSE_DIR, z ? -1.0f : 1.0f);
        }
    }

    public void startAudio() {
        this.isPlaying = true;
        if (this.mChanPlay != 0) {
            BASS.BASS_ChannelPlay(this.mChanPlay, false);
        }
        this.mHandler.sendEmptyMessage(0);
    }
}
