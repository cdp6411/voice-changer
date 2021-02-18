package voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;

import androidx.fragment.app.Fragment;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioChannel;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSampleRate;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSource;

public class AndroidAudioRecorder {
    protected static final String EXTRA_AUTO_START = "autoStart";
    protected static final String EXTRA_CHANNEL = "channel";
    protected static final String EXTRA_COLOR = "color";
    protected static final String EXTRA_FILE_PATH = "filePath";
    protected static final String EXTRA_KEEP_DISPLAY_ON = "keepDisplayOn";
    protected static final String EXTRA_SAMPLE_RATE = "sampleRate";
    protected static final String EXTRA_SOURCE = "source";
    private Activity activity;
    private boolean autoStart = false;
    private AudioChannel channel = AudioChannel.STEREO;
    private int color = Color.parseColor("#546E7A");
    private String filePath;
    private Fragment fragment;
    private boolean keepDisplayOn = false;
    private int requestCode = 0;
    private AudioSampleRate sampleRate = AudioSampleRate.HZ_44100;
    private AudioSource source = AudioSource.MIC;

    private AndroidAudioRecorder(Activity activity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/recorded_audio.wav");
        this.filePath = stringBuilder.toString();
        this.activity = activity;
    }

    private AndroidAudioRecorder(Fragment fragment) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/recorded_audio.wav");
        this.filePath = stringBuilder.toString();
        this.fragment = fragment;
    }

    public static AndroidAudioRecorder with(Activity activity) {
        return new AndroidAudioRecorder(activity);
    }

    public static AndroidAudioRecorder with(Fragment fragment) {
        return new AndroidAudioRecorder(fragment);
    }

    public void record() {
        Intent intent = new Intent(this.activity, AudioRecorderActivity.class);
        intent.putExtra(EXTRA_FILE_PATH, this.filePath);
        intent.putExtra("color", this.color);
        intent.putExtra("source", this.source);
        intent.putExtra(EXTRA_CHANNEL, this.channel);
        intent.putExtra(EXTRA_SAMPLE_RATE, this.sampleRate);
        intent.putExtra(EXTRA_AUTO_START, this.autoStart);
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, this.keepDisplayOn);
        this.activity.startActivityForResult(intent, this.requestCode);
    }

    public AndroidAudioRecorder setAutoStart(boolean z) {
        this.autoStart = z;
        return this;
    }

    public AndroidAudioRecorder setChannel(AudioChannel audioChannel) {
        this.channel = audioChannel;
        return this;
    }

    public AndroidAudioRecorder setColor(int i) {
        this.color = i;
        return this;
    }

    public AndroidAudioRecorder setFilePath(String str) {
        this.filePath = str;
        return this;
    }

    public AndroidAudioRecorder setKeepDisplayOn(boolean z) {
        this.keepDisplayOn = z;
        return this;
    }

    public AndroidAudioRecorder setRequestCode(int i) {
        this.requestCode = i;
        return this;
    }

    public AndroidAudioRecorder setSampleRate(AudioSampleRate audioSampleRate) {
        this.sampleRate = audioSampleRate;
        return this;
    }

    public AndroidAudioRecorder setSource(AudioSource audioSource) {
        this.source = audioSource;
        return this;
    }
}
