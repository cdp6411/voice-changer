package voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adapter.MyStudioAdapter;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adclass.MyApplication;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.constants.IVoiceChangerConstants;
import java.io.File;
import java.util.ArrayList;

public class MyStudioActivity extends AppCompatActivity {
    ImageView ivBack;
    private AdView mAdView;
    RecyclerView.LayoutManager layoutManager;
    private RelativeLayout ll_Ad_Progress;
    ArrayList<String> myStudio;
    MyStudioAdapter myStudioAdapter;
    RecyclerView my_recycler_view_my;
    private LinearLayout native_ad_container;
    String path;



    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_studio);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        StrictMode.setVmPolicy(new Builder().build());
        int i = 0;
        this.my_recycler_view_my = (RecyclerView) findViewById(R.id.my_recycler_view_my);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.ivBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MyStudioActivity.this.onBackPressed();
            }
        });
        this.myStudio = new ArrayList();
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(externalStorageDirectory.getAbsolutePath());
        stringBuilder.append("/CallVoiceChanger/");
        File file = new File(stringBuilder.toString());
        if (file.exists() && file.listFiles() != null) {
            File[] listFiles = file.listFiles();
            int length = listFiles.length;
            while (i < length) {
                File file2 = listFiles[i];
                if (file2.isFile()) {
                    this.path = file2.getName();
                }
                if (this.path.contains(IVoiceChangerConstants.AUDIO_RECORDER_FILE_EXT_MP3) || this.path.contains(IVoiceChangerConstants.AUDIO_RECORDER_FILE_EXT_WAV)) {
                    this.myStudio.add(this.path);
                }
                i++;
            }
        }
        this.layoutManager = new LinearLayoutManager(this);
        this.myStudioAdapter = new MyStudioAdapter(this.myStudio, this);
        this.my_recycler_view_my.setLayoutManager(this.layoutManager);
        this.my_recycler_view_my.setAdapter(this.myStudioAdapter);
    }
}
