package voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity;

import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.core.content.FileProvider;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adapter.EffectAdapter;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adapter.EffectAdapter.OnEffectListener;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adclass.MyApplication;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.basseffect.DBMediaPlayer;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.basseffect.IDBMediaListener;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.constants.IVoiceChangerConstants;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.dataMng.JsonParsingUtils;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.dataMng.TotalDataManager;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.object.EffectObject;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.soundMng.SoundManager;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.task.DBTask;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.task.IDBCallback;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.task.IDBTaskListener;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.ApplicationUtils;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.DBLog;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.DirectionUtils;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.IOUtils;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.StringUtils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.un4seen.bass.BASS;

import java.io.File;
import java.util.ArrayList;

public class EffectActivity extends DBFragmentActivity implements OnEffectListener {
    private boolean isInit;
    ImageView ivBack;
    private DBMediaPlayer mDBMedia;
    private EffectAdapter mEffectApdater;
    private ArrayList<EffectObject> mListEffectObjects;
    private ListView mListViewEffects;
    private String mNameExportVoice;
    private String mPathAudio;
    private Uri uri;
    private AdView mAdView;

    class C09121 implements IDBCallback {
        C09121() {
        }

        public void onAction() {
            EffectActivity.this.setupInfo();
        }
    }

    class C09197 implements IDBMediaListener {
        C09197() {
        }

        public void onMediaCompletion() {
            TotalDataManager.getInstance().onResetState();
            EffectActivity.this.mEffectApdater.notifyDataSetChanged();
        }

        public void onMediaError() {
        }
    }

    private void backToHome() {
        deleteMainFile();
        onDestroyMedia();
        DirectionUtils.changeActivity(this, R.anim.slide_in_from_left, R.anim.slide_out_to_right, true, new Intent(this, ScanActivity.class));
    }

    private void createDBMedia() {
        if (!StringUtils.isEmptyString(this.mPathAudio)) {
            this.mDBMedia = new DBMediaPlayer(this.mPathAudio);
            this.mDBMedia.prepareAudio();
            this.mDBMedia.setOnDBMediaListener(new C09197());
        }
    }

    private void deleteMainFile() {
        if (!StringUtils.isEmptyString(this.mPathAudio)) {
            try {
                new File(this.mPathAudio).delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onDestroyMedia() {
        try {
            if (this.mDBMedia != null) {
                this.mDBMedia.releaseAudio();
            }
            BASS.BASS_PluginFree(0);
            BASS.BASS_Free();
            TotalDataManager.getInstance().onResetState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetStateAudio() {
        TotalDataManager.getInstance().onResetState();
        if (this.mEffectApdater != null) {
            this.mEffectApdater.notifyDataSetChanged();
        }
        if (this.mDBMedia != null && this.mDBMedia.isPlaying()) {
            this.mDBMedia.pauseAudio();
        }
    }

    private void setupInfo() {
        this.mListEffectObjects = TotalDataManager.getInstance().getListEffectObjects();
        if (this.mListEffectObjects == null || this.mListEffectObjects.size() <= 0) {
            startLoad(new C09121());
            return;
        }
        this.mEffectApdater = new EffectAdapter(this, this.mListEffectObjects, this.mTypefaceAvenir);
        this.mEffectApdater.setOnEffectListener(this);
        this.mListViewEffects.setAdapter(this.mEffectApdater);
        onInitAudioDevice();
        createDBMedia();
    }

    private void shareFile(String str) {
        File file = new File(new File(this.mPathAudio).getParentFile(), str);
        if (file.exists() && file.isFile()) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(str.endsWith(IVoiceChangerConstants.AUDIO_RECORDER_FILE_EXT_MP3) ? "audio/mp3" : "audio/*");
            this.uri = VERSION.SDK_INT >= 24 ? FileProvider.getUriForFile(getApplicationContext(), "voicechanger.audioeffects.voiceeditor.supervoiceeffect.provider", file) : Uri.fromFile(file);
            intent.putExtra("android.intent.extra.STREAM", this.uri);
            startActivity(Intent.createChooser(intent, "Share Via"));
        }
    }

    private void showDialogEnterName(final IDBCallback iDBCallback) {
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        new Builder(this).setTitle(getString(R.string.title_enter_name)).setView(editText).setPositiveButton(getString(R.string.title_ok), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ApplicationUtils.hiddenVirtualKeyboard(EffectActivity.this, editText);
                String obj = editText.getText().toString();
                if (!StringUtils.isEmptyString(obj)) {
                    if (StringUtils.isContainsSpecialCharacter(obj)) {
                        EffectActivity.this.showToast((int) R.string.info_your_name_error);
                        return;
                    }
                    EffectActivity effectActivity = EffectActivity.this;
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(obj));
                    stringBuilder.append(IVoiceChangerConstants.AUDIO_RECORDER_FILE_EXT_WAV);
                    effectActivity.mNameExportVoice = stringBuilder.toString();
                }
                if (iDBCallback != null) {
                    iDBCallback.onAction();
                }
            }
        }).setNegativeButton(getString(R.string.title_skip), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (iDBCallback != null) {
                    iDBCallback.onAction();
                }
            }
        }).create().show();
    }

    private void startLoad(final IDBCallback iDBCallback) {
        new DBTask(new IDBTaskListener() {
            public ArrayList<EffectObject> mListEffects;

            public void onDoInBackground() {
                this.mListEffects = JsonParsingUtils.parsingListEffectObject(IOUtils.readStringFromAssets(EffectActivity.this, "effects.dat"));
                String str = EffectActivity.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("===============>Size=");
                stringBuilder.append(this.mListEffects.size());
                DBLog.m11d(str, stringBuilder.toString());
                if (this.mListEffects != null && this.mListEffects.size() > 0) {
                    EffectActivity.this.mTotalMng.setListEffectObjects(this.mListEffects);
                }
            }

            public void onPostExcute() {
                EffectActivity.this.dimissProgressDialog();
                if (this.mListEffects == null || this.mListEffects.size() == 0) {
                    EffectActivity.this.backToHome();
                } else if (iDBCallback != null) {
                    iDBCallback.onAction();
                }
            }

            public void onPreExcute() {
                EffectActivity.this.showProgressDialog();
            }
        }).execute(new Void[0]);
    }

    private void startSaveEffect(String str, EffectObject effectObject, IDBCallback iDBCallback) {
        final File file = new File(new File(this.mPathAudio).getParentFile(), str);
        final DBMediaPlayer dBMediaPlayer = new DBMediaPlayer(this.mPathAudio);
         final EffectObject effectObject2 = effectObject;
        final IDBCallback iDBCallback2 = iDBCallback;
        new DBTask(new IDBTaskListener() {
            public void onDoInBackground() {
                if (dBMediaPlayer != null && dBMediaPlayer.initMediaToSave()) {
                    dBMediaPlayer.setReverse(effectObject2.isReverse());
                    dBMediaPlayer.setAudioPitch(effectObject2.getPitch());
                    dBMediaPlayer.setAudioRate(effectObject2.getRate());
                    dBMediaPlayer.setAudioReverb(effectObject2.getReverb());
                    dBMediaPlayer.setFlangerEffect(effectObject2.isFlanger());
                    dBMediaPlayer.setAudioEcho(effectObject2.isEcho());
                    dBMediaPlayer.setAudioEQ(effectObject2.getEq());
                    dBMediaPlayer.saveToFile(file.getAbsolutePath());
                    dBMediaPlayer.releaseAudio();
                }
            }

            public void onPostExcute() {
                EffectActivity.this.dimissProgressDialog();
                if (iDBCallback2 != null) {
                    iDBCallback2.onAction();
                }
            }

            public void onPreExcute() {
                EffectActivity.this.showProgressDialog();
            }
        }).execute(new Void[0]);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_effect);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EffectActivity.this.onBackPressed();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            this.mPathAudio = intent.getStringExtra(IVoiceChangerConstants.KEY_PATH_AUDIO);
        }
        this.mListViewEffects = (ListView) findViewById(R.id.list_effects);
        if (StringUtils.isEmptyString(this.mPathAudio)) {
            backToHome();
            return;
        }
        File file = new File(this.mPathAudio);
        if (file.exists() && file.isFile()) {
            setupInfo();
            return;
        }
        showToast("File not found exception");
        backToHome();
    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    public void onInitAudioDevice() {
        if (!this.isInit) {
            this.isInit = true;
            StringBuilder stringBuilder;
            if (BASS.BASS_Init(-1, IVoiceChangerConstants.RECORDER_SAMPLE_RATE, 0)) {
                String str = getApplicationInfo().nativeLibraryDir;
                try {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append("/libbass_fx.so");
                    BASS.BASS_PluginLoad(stringBuilder.toString(), 0);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append("/libbassenc.so");
                    BASS.BASS_PluginLoad(stringBuilder.toString(), 0);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append("/libbassmix.so");
                    BASS.BASS_PluginLoad(stringBuilder.toString(), 0);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append("/libbasswv.so");
                    BASS.BASS_PluginLoad(stringBuilder.toString(), 0);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(TAG);
            stringBuilder.append(" Can't initialize device");
            new Exception(stringBuilder.toString()).printStackTrace();
            this.isInit = false;
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mDBMedia != null) {
            resetStateAudio();
        }
    }

    public void onPlayEffect(EffectObject effectObject) {
        if (effectObject.isPlaying()) {
            effectObject.setPlaying(false);
            if (this.mDBMedia != null) {
                this.mDBMedia.pauseAudio();
            }
        } else {
            TotalDataManager.getInstance().onResetState();
            effectObject.setPlaying(true);
            if (this.mDBMedia != null) {
                this.mDBMedia.setReverse(effectObject.isReverse());
                this.mDBMedia.setAudioPitch(effectObject.getPitch());
                this.mDBMedia.setAudioRate(effectObject.getRate());
                this.mDBMedia.setAudioReverb(effectObject.getReverb());
                this.mDBMedia.setAudioEQ(effectObject.getEq());
                this.mDBMedia.setFlangerEffect(effectObject.isFlanger());
                this.mDBMedia.setAudioEcho(effectObject.isEcho());
                if (this.mDBMedia.isPlaying()) {
                    if (effectObject.isReverse()) {
                        this.mDBMedia.seekTo(this.mDBMedia.getDuration());
                    } else {
                        this.mDBMedia.seekTo(0);
                    }
                }
                this.mDBMedia.startAudio();
            }
        }
        this.mEffectApdater.notifyDataSetChanged();
    }

    public void onSaveEffect(EffectObject effectObject) {
        if (this.mDBMedia != null) {
            resetStateAudio();
        }
        SoundManager.getInstance().play((Context) this, (int) R.raw.click);
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(String.valueOf(System.currentTimeMillis() / 1000)));
        stringBuilder.append("tempshare");
        stringBuilder.append(IVoiceChangerConstants.AUDIO_RECORDER_FILE_EXT_WAV);
        final String stringBuilder2 = stringBuilder.toString();
        if (this.mDBMedia != null) {
            startSaveEffect(stringBuilder2, effectObject, new IDBCallback() {
                public void onAction() {
                    String file = Environment.getExternalStorageDirectory().toString();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(file);
                    stringBuilder.append("/CallVoiceChanger");
                    File file2 = new File(stringBuilder.toString(), stringBuilder2);
                    if (file2.exists() && file2.isFile()) {
                        EffectActivity.this.showToast(String.format(EffectActivity.this.getString(R.string.info_save_voice), new Object[]{file2.getAbsolutePath()}));
                        EffectActivity.this.shareFile(stringBuilder2);
                    }
                }
            });
        }
    }

    public void onShareEffect(final EffectObject effectObject) {
        if (this.mDBMedia != null) {
            resetStateAudio();
        }
        SoundManager.getInstance().play((Context) this, (int) R.raw.click);
        this.mNameExportVoice = String.format(IVoiceChangerConstants.FORMAT_NAME_VOICE, new Object[]{String.valueOf(System.currentTimeMillis() / 1000)});
        showDialogEnterName(new IDBCallback() {
            public void onAction() {
                if (EffectActivity.this.mDBMedia != null) {
                    EffectActivity effectActivity = EffectActivity.this;
                    String access$500 = EffectActivity.this.mNameExportVoice;
                    EffectObject effectObject2 = effectObject;
                    effectActivity.startSaveEffect(access$500, effectObject, new IDBCallback() {
                        public void onAction() {
                            String file = Environment.getExternalStorageDirectory().toString();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(file);
                            stringBuilder.append("/CallVoiceChanger");
                            File file2 = new File(stringBuilder.toString(), EffectActivity.this.mNameExportVoice);
                            if (!file2.exists()) {
                                file2.mkdirs();
                            }
                            if (file2.exists() && file2.isFile()) {
                                EffectActivity.this.showToast(String.format(EffectActivity.this.getString(R.string.info_save_voice), new Object[]{file2.getAbsolutePath()}));
                            }
                        }
                    });
                }
            }
        });
    }


}
