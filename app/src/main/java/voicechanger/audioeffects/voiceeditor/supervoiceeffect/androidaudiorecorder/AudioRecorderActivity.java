package voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity.MainActivity;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioChannel;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSampleRate;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSource;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport.Default;
import omrecorder.PullTransport.OnAudioChunkPulledListener;
import omrecorder.Recorder;

public class AudioRecorderActivity extends AppCompatActivity implements OnCompletionListener, OnAudioChunkPulledListener {
    private boolean autoStart;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd1;
    private AudioChannel channel;
    private int color;
    private RelativeLayout contentLayout;
    private String filePath;
    private boolean isRecording;
    private boolean keepDisplayOn;
    private ImageView playView;
    private ImageView btn_ok,ib_cancel;
    private MediaPlayer player;
    private int playerSecondsElapsed;
    private ImageView recordView;
    private Recorder recorder;
    private int recorderSecondsElapsed;
    private ImageView restartView;
    private AudioSampleRate sampleRate;
//    private MenuItem saveMenuItem;
    private AudioSource source;
    private TextView statusView;
    private Timer timer;
    private TextView timerView;
    private VisualizerHandler visualizerHandler;

    private boolean isPlaying() {
        boolean z = false;
        try {
            if (!(this.player == null || !this.player.isPlaying() || this.isRecording)) {
                z = true;
            }
        } catch (Exception unused) {
            return z;
        }
        return z;
    }

    private void pauseRecording() {
        this.isRecording = false;
        if (!isFinishing()) {
//            this.saveMenuItem.setVisible(true);
        }
        this.statusView.setText(R.string.aar_paused);
        this.statusView.setVisibility(View.VISIBLE);
        this.restartView.setVisibility(View.VISIBLE);
        this.btn_ok.setVisibility(View.VISIBLE);
//        this.playView.setVisibility(View.VISIBLE);
        this.recordView.setImageResource(R.drawable.i_microphone);
//        this.playView.setImageResource(R.drawable.ic_playyy);
        if (this.visualizerHandler != null) {
            this.visualizerHandler.stop();
        }
        if (this.recorder != null) {
            this.recorder.pauseRecording();
        }
        stopTimer();
    }

    private void resumeRecording() {
        this.isRecording = true;
//        this.saveMenuItem.setVisible(false);
        this.statusView.setText(R.string.aar_recording);
        this.statusView.setVisibility(View.VISIBLE);
        this.restartView.setVisibility(View.INVISIBLE);
//        this.playView.setVisibility(View.INVISIBLE);
        this.recordView.setImageResource(R.drawable.ic_pausee);
//        this.playView.setImageResource(R.drawable.ic_playyy);
        this.visualizerHandler = new VisualizerHandler();
        if (this.recorder == null) {
            this.timerView.setText("00:00:00");
            this.recorder = OmRecorder.wav(new Default(Util.getMic(this.source, this.channel, this.sampleRate), (OnAudioChunkPulledListener) this), new File(this.filePath));
        }
        this.recorder.resumeRecording();
        startTimer();
    }

    private void selectAudio() {
        stopRecording();
        setResult(-1);
        finish();
    }

    private void startPlaying() {
        try {
            stopRecording();
            this.player = new MediaPlayer();
            this.player.setDataSource(this.filePath);
            this.player.prepare();
            this.player.start();
            this.timerView.setText("00:00:00");
            this.statusView.setText(R.string.aar_playing);
            this.statusView.setVisibility(View.VISIBLE);
//            this.playView.setImageResource(R.drawable.ic_stop);
            this.playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {
        stopTimer();
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                AudioRecorderActivity.this.updateTimer();
            }
        }, 0, 1000);
    }

    private void stopPlaying() {
        this.statusView.setText("");
        this.statusView.setVisibility(View.INVISIBLE);
//        this.playView.setImageResource(R.drawable.ic_playyy);
        if (this.visualizerHandler != null) {
            this.visualizerHandler.stop();
        }
        if (this.player != null) {
            try {
                this.player.stop();
                this.player.reset();
            } catch (Exception unused) {
                stopTimer();
            }
        }
    }

    private void stopRecording() {
        if (this.visualizerHandler != null) {
            this.visualizerHandler.stop();
        }
        this.recorderSecondsElapsed = 0;
        if (this.recorder != null) {
            this.recorder.stopRecording();
            this.recorder = null;
        }
        stopTimer();
    }

    private void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }
    }

    private void updateTimer() {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView access$800;
                int access$700;
                if (AudioRecorderActivity.this.isRecording) {
                    AudioRecorderActivity.this.recorderSecondsElapsed = AudioRecorderActivity.this.recorderSecondsElapsed + 1;
                    access$800 = AudioRecorderActivity.this.timerView;
                    access$700 = AudioRecorderActivity.this.recorderSecondsElapsed;
                } else if (AudioRecorderActivity.this.isPlaying()) {
                    AudioRecorderActivity.this.playerSecondsElapsed = AudioRecorderActivity.this.playerSecondsElapsed + 1;
                    access$800 = AudioRecorderActivity.this.timerView;
                    access$700 = AudioRecorderActivity.this.playerSecondsElapsed;
                } else {
                    return;
                }
                access$800.setText(Util.formatSeconds(access$700));
            }
        });
    }

    public void onAudioChunkPulled(AudioChunk audioChunk) {
        if (this.isRecording) {
            audioChunk.maxAmplitude();
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlaying();
    }

    protected void onCreate(Bundle bundle) {
        boolean z;
        super.onCreate(bundle);
        setContentView((int) R.layout.aar_activity_audio_recorder);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd1.loadAd(new AdRequest.Builder().build());


        if (bundle != null) {
            this.filePath = bundle.getString("filePath");
            this.source = (AudioSource) bundle.getSerializable("source");
            this.channel = (AudioChannel) bundle.getSerializable("channel");
            this.sampleRate = (AudioSampleRate) bundle.getSerializable("sampleRate");
            this.color = bundle.getInt("color");
            this.autoStart = bundle.getBoolean("autoStart");
            z = bundle.getBoolean("keepDisplayOn");
        } else {
            this.filePath = getIntent().getStringExtra("filePath");
            this.source = (AudioSource) getIntent().getSerializableExtra("source");
            this.channel = (AudioChannel) getIntent().getSerializableExtra("channel");
            this.sampleRate = (AudioSampleRate) getIntent().getSerializableExtra("sampleRate");
            this.color = getIntent().getIntExtra("color", ViewCompat.MEASURED_STATE_MASK);
            this.autoStart = getIntent().getBooleanExtra("autoStart", false);
            z = getIntent().getBooleanExtra("keepDisplayOn", false);
        }
        this.keepDisplayOn = z;
        if (this.keepDisplayOn) {
            getWindow().addFlags(128);
        }
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setElevation(0.0f);
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ba4374")));
//
//            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.aar_ic_clear));
//        }
        this.contentLayout = (RelativeLayout) findViewById(R.id.content);
        this.statusView = (TextView) findViewById(R.id.status);
        this.timerView = (TextView) findViewById(R.id.timer);
        this.restartView = (ImageView) findViewById(R.id.restart);
        this.recordView = (ImageView) findViewById(R.id.record);
        this.btn_ok=(ImageView)findViewById(R.id.btn_ok);
        this.ib_cancel=(ImageView)findViewById(R.id.ib_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAudio();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        ib_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioRecorderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                if (mInterstitialAd1.isLoaded()) {
                    mInterstitialAd1.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
//        this.playView = (ImageView) findViewById(R.id.play);
        this.restartView.setVisibility(View.INVISIBLE);
//        this.playView.setVisibility(View.INVISIBLE);
//        if (Util.isBrightColor(this.color)) {
//            ContextCompat.getDrawable(this, R.drawable.aar_ic_clear).setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_ATOP);
//            ContextCompat.getDrawable(this, R.drawable.aar_ic_check).setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_ATOP);
//            this.statusView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
//            this.timerView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
//            this.restartView.setColorFilter(ViewCompat.MEASURED_STATE_MASK);
//            this.recordView.setColorFilter(ViewCompat.MEASURED_STATE_MASK);
////            this.playView.setColorFilter(ViewCompat.MEASURED_STATE_MASK);
//        }
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.aar_audio_recorder, menu);
//        this.saveMenuItem = menu.findItem(R.id.action_save);
//        this.saveMenuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.aar_ic_check));
//        return super.onCreateOptionsMenu(menu);
//    }

    protected void onDestroy() {
        restartRecording(null);
        setResult(0);
        super.onDestroy();
    }

//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        int itemId = menuItem.getItemId();
//        if (itemId == 16908332) {
//            finish();
//        } else if (itemId == R.id.action_save) {
//            selectAudio();
//        }
//        return super.onOptionsItemSelected(menuItem);
//    }

    protected void onPause() {
        restartRecording(null);
        super.onPause();
    }

    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        if (this.autoStart && !this.isRecording) {
            toggleRecording(null);
        }
    }

    public void onResume() {
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("filePath", this.filePath);
        bundle.putInt("color", this.color);
        super.onSaveInstanceState(bundle);
    }

    public void restartRecording(View view) {
        if (this.isRecording) {
            stopRecording();
        } else if (isPlaying()) {
            stopPlaying();
        } else {
            this.visualizerHandler = new VisualizerHandler();
            if (this.visualizerHandler != null) {
                this.visualizerHandler.stop();
            }
        }
        this.statusView.setVisibility(View.INVISIBLE);
        this.restartView.setVisibility(View.INVISIBLE);
//        this.playView.setVisibility(View.INVISIBLE);
//        this.recordView.setImageResource(R.drawable.i_microphone);
        this.timerView.setText("00:00:00");
        this.recorderSecondsElapsed = 0;
        this.playerSecondsElapsed = 0;
    }

    public void togglePlaying(View view) {
        pauseRecording();
        Util.wait(100, new Runnable() {
            public void run() {
                if (AudioRecorderActivity.this.isPlaying()) {
                    AudioRecorderActivity.this.stopPlaying();
                } else {
                    AudioRecorderActivity.this.startPlaying();
                }
            }
        });
    }

    public void toggleRecording(View view) {
        stopPlaying();
        Util.wait(100, new Runnable() {
            public void run() {
                if (AudioRecorderActivity.this.isRecording) {
                    AudioRecorderActivity.this.pauseRecording();
                } else {
                    AudioRecorderActivity.this.resumeRecording();
                }
            }
        });
    }
}
