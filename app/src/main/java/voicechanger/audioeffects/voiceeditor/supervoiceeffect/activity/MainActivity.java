package voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.u.securekeys.SecureEnvironment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.BuildConfig;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adclass.BaseActivity;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adclass.MyApplication;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.AndroidAudioRecorder;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioChannel;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSampleRate;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.androidaudiorecorder.model.AudioSource;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.constants.IVoiceChangerConstants;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.StringUtils;


import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQ_CODE = 1;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd mInterstitialAd1;
//    private ImageView iv_start, iv_creation;
    private LinearLayout iv_start, iv_creation;
    private Uri urishare;
    private Dialog dialog;
    int color;
    public static String AUDIO_FILE_PATH;
    Shimmer shimmer;
    ShimmerTextView myShimmerTextView,myShimmerTextView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main1);

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

        myShimmerTextView =  findViewById(R.id.shimmer_tv);
        myShimmerTextView1 =  findViewById(R.id.shimmer_tv1);
        shimmer = new Shimmer();
        shimmer.start(myShimmerTextView);
        shimmer.start(myShimmerTextView1);

        String file = Environment.getExternalStorageDirectory().toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(file);
        stringBuilder.append("/CallVoiceChanger");
        File file2 = new File(stringBuilder.toString());
        if (!file2.exists()) {
            file2.mkdirs();
        }
        file = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        stringBuilder = new StringBuilder();
        stringBuilder.append(file2);
        stringBuilder.append("/Audio-");
        stringBuilder.append(file);
        stringBuilder.append(IVoiceChangerConstants.AUDIO_RECORDER_FILE_EXT_MP3);
        AUDIO_FILE_PATH = stringBuilder.toString();

        if (Build.VERSION.SDK_INT >= 23) {
            checkMultiplePermissions();
        }

        inIT();
//        exitDialog();
    }

    private void inIT() {

//        checkPermission();
        iv_start = findViewById(R.id.iv_start);
        iv_creation =  findViewById(R.id.iv_creation);

        iv_start.setOnClickListener(this);
        iv_creation.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_start:
                recordAudio();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;

            case R.id.iv_creation:
                this.startActivity(new Intent(MainActivity.this, MyStudioActivity.class));
                if (mInterstitialAd1.isLoaded()) {
                    mInterstitialAd1.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;

        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0 && i2 == -1) {
            if (StringUtils.isEmptyString(AUDIO_FILE_PATH)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(AUDIO_FILE_PATH);
            } else {
                Intent intent2 = new Intent(this, EffectActivity.class);
                intent2.putExtra(IVoiceChangerConstants.KEY_PATH_AUDIO, AUDIO_FILE_PATH);
                startActivity(intent2);

            }
        }
    }
    private void checkMultiplePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList();
            List<String> permissionsList = new ArrayList();
            if (!addPermission(permissionsList, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                permissionsNeeded.add("Write Storage");
            }
            if (!addPermission(permissionsList, "android.permission.READ_EXTERNAL_STORAGE")) {
                permissionsNeeded.add("Read Storage");
            }
            if (!addPermission(permissionsList, "android.permission.RECORD_AUDIO")) {
                permissionsNeeded.add("Recoer Audio");
            }
            if (permissionsList.size() > 0) {
                requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), REQ_CODE);
                return;
            }
        }
    }private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE:
                Map<String, Integer> perms = new HashMap();
                perms.put("android.permission.WRITE_EXTERNAL_STORAGE", Integer.valueOf(0));
                perms.put("android.permission.READ_EXTERNAL_STORAGE", Integer.valueOf(0));
                perms.put("android.permission.RECORD_AUDIO", Integer.valueOf(0));
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], Integer.valueOf(grantResults[i]));
                }

                if (((Integer) perms.get("android.permission.READ_EXTERNAL_STORAGE")).intValue() == 0
                        && ((Integer) perms.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() == 0
                        && ((Integer) perms.get("android.permission.RECORD_AUDIO")).intValue() == 0) {
                    break;
                } else if (Build.VERSION.SDK_INT >= 23) {
                    Toast.makeText(getApplicationContext(), "My App cannot run without Storage Permissions.\nRelaunch My App or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                } else {
                    break;
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }


//    public void checkPermission(){
//
//        PermissionListener permissionListener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//
//            }
//
//            @Override
//            public void onPermissionDenied(List<String> deniedPermissions) {
//                Toast.makeText(MainActivity.this, "Please Allow All Permission to Use Application", Toast.LENGTH_SHORT).show();
//            }
//        };
//        TedPermission.with(MainActivity.this).setPermissionListener(permissionListener)
//                .setPermissions(Manifest.permission.RECORD_AUDIO,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE).check();
//    }
//    private void checkMultiplePermissions() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            List<String> permissionsNeeded = new ArrayList();
//            List<String> permissionsList = new ArrayList();
//            if (!addPermission(permissionsList, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//                permissionsNeeded.add("Write Storage");
//            }
//            if (!addPermission(permissionsList, "android.permission.READ_EXTERNAL_STORAGE")) {
//                permissionsNeeded.add("Read Storage");
//            }
//            if (!addPermission(permissionsList, "android.permission.RECORD_AUDIO")) {
//                permissionsNeeded.add("Recoer Audio");
//            }
//            if (permissionsList.size() > 0) {
//                requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), REQ_CODE);
//                return;
//            }
//        }
//    }

//    private boolean addPermission(List<String> permissionsList, String permission) {
//        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
//            permissionsList.add(permission);
//            if (!shouldShowRequestPermissionRationale(permission)) {
//                return false;
//            }
//        }
//        return true;
//    }

//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQ_CODE:
//                Map<String, Integer> perms = new HashMap();
//                perms.put("android.permission.WRITE_EXTERNAL_STORAGE", Integer.valueOf(0));
//                perms.put("android.permission.READ_EXTERNAL_STORAGE", Integer.valueOf(0));
//                perms.put("android.permission.RECORD_AUDIO", Integer.valueOf(0));
//                for (int i = 0; i < permissions.length; i++) {
//                    perms.put(permissions[i], Integer.valueOf(grantResults[i]));
//                }
//
//                if (((Integer) perms.get("android.permission.READ_EXTERNAL_STORAGE")).intValue() == 0
//                        && ((Integer) perms.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() == 0
//                        && ((Integer) perms.get("android.permission.RECORD_AUDIO")).intValue() == 0) {
//                    break;
//                } else if (Build.VERSION.SDK_INT >= 23) {
//                    Toast.makeText(getApplicationContext(), "My App cannot run without Storage Permissions.\nRelaunch My App or allow permissions in Applications Settings", Toast.LENGTH_LONG).show();
//                    finish();
//                    break;
//                } else {
//                    break;
//                }
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                return;
//        }
//    }

    public void recordAudio() {
        AndroidAudioRecorder.with((Activity) this).setFilePath(AUDIO_FILE_PATH).setColor(this.color).setRequestCode(0).setSource(AudioSource.MIC).setChannel(AudioChannel.STEREO).setSampleRate(AudioSampleRate.HZ_48000).setAutoStart(false).setKeepDisplayOn(true).record();
    }

//    private void exitDialog() {
//        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.dailog_exit);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
//                    return false;
//                }
//                return true;
//            }
//        });
//
//
//
//        TextView iv_rateus = (TextView) dialog.findViewById(R.id.iv_rateus);
//        iv_rateus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
//                Intent inrate = new Intent(Intent.ACTION_VIEW, uri1);
//
//                try {
//                    startActivity(inrate);
//                } catch (ActivityNotFoundException e) {
//                    Toast.makeText(MainActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
//        tv_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finishAffinity();
//            }
//        });
//
//        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
//        tv_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                exitDialog();
//            }
//        });
//
//
//    }





    //Fb OnCreate Start



    //Fb OnCreate End

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
        final View dialogView= inflater.inflate(R.layout.customdialog, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")

                .setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                        ImageView img = (ImageView)findViewById(R.id.imageofspleshscreen);
//                        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
//                        img.startAnimation(aniFade);
//                        img.setVisibility(View.VISIBLE);
//                        img.startAnimation(aniFade);

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setNeutralButton("Rate us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                Intent inrate = new Intent(Intent.ACTION_VIEW, uri1);

                try {
                    startActivity(inrate);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "You don't have Google Play installed", Toast.LENGTH_LONG).show();
                }
            }
        });
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdLoader.Builder builder1 = new AdLoader.Builder(
                this, "ca-app-pub-3940256099942544/2247696110");
        builder1.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template =dialogView.findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });
        AdLoader adLoader = builder1.build();
        adLoader.loadAd(new AdRequest.Builder().build());

        builder.create();
        builder.show();
    }
    }

