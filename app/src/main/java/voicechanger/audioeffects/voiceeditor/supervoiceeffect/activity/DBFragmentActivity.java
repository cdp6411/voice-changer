package voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.abtractclass.fragment.DBFragment;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adclass.BaseActivity;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.constants.IVoiceChangerConstants;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.dataMng.TotalDataManager;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.IDialogFragmentListener;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.setting.SettingManager;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.soundMng.SoundManager;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.task.IDBCallback;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.task.IDBConstantURL;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.ResolutionUtils;

import java.util.ArrayList;
import java.util.Random;

public class DBFragmentActivity extends BaseActivity implements IVoiceChangerConstants, IDialogFragmentListener, IDBConstantURL {
    public static final String TAG = "DBFragmentActivity";
    private int countToExit;
    private boolean isAllowPressMoreToExit;
    public int mAccentColor;
    public int mBgColor;
    private ImageView mBlurredView;
    public ArrayList<Fragment> mListFragments;
    private Dialog mProgressDialog;
    private Random mRandom;
    public SoundManager mSoundMng;
    public int mTextColor;
    public TotalDataManager mTotalMng;
    public Typeface mTypefaceAvenir;
    public Typeface mTypefaceBold;
    public Typeface mTypefaceItalic;
    public Typeface mTypefaceLight;
    public Typeface mTypefaceLogo;
    public Typeface mTypefaceNormal;
    private long pivotTime;
    private int screenHeight;
    private int screenWidth;

    class C02005 implements OnKeyListener {
        C02005() {
        }

        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
            return i == 4;
        }
    }


    private void createProgressDialog() {
        this.mProgressDialog = new Dialog(this);
        this.mProgressDialog.requestWindowFeature(1);
        this.mProgressDialog.setContentView(R.layout.item_progress_bar);
        ((TextView) this.mProgressDialog.findViewById(R.id.tv_message)).setTypeface(this.mTypefaceLight);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.setOnKeyListener(new C02005());
    }

    private int getObjectColor(int i) {
        try {
            TypedValue typedValue = new TypedValue();
            if (getTheme().resolveAttribute(i, typedValue, true)) {
                return typedValue.data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void dimissProgressDialog() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFormat(1);
        getWindow().setSoftInputMode(3);
        getWindow().setFlags(1024, 1024);
        createProgressDialog();
        this.mTypefaceNormal = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        this.mTypefaceLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        this.mTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        this.mTypefaceItalic = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf");
        this.mTypefaceLogo = Typeface.createFromAsset(getAssets(), "fonts/Satisfy-Regular.ttf");
        this.mTypefaceAvenir = Typeface.createFromAsset(getAssets(), "fonts/Avenir-Oblique.ttf");
        int[] deviceResolution = ResolutionUtils.getDeviceResolution(this);
        if (deviceResolution != null && deviceResolution.length == 2) {
            this.screenWidth = deviceResolution[0];
            this.screenHeight = deviceResolution[1];
        }
        this.mTextColor = getObjectColor(16842904);
        this.mAccentColor = SettingManager.getCurrentAccentColor(this) != 0 ? SettingManager.getCurrentAccentColor(this) : getObjectColor(R.attr.colorAccent);
        this.mTotalMng = TotalDataManager.getInstance();
        this.mSoundMng = SoundManager.getInstance();
        this.mBgColor = getObjectColor(16842836);
        this.mRandom = new Random();


    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }


    public void showProgressDialog() {
        showProgressDialog((int) R.string.info_loading);
    }

    public void showProgressDialog(int i) {
        showProgressDialog(getString(i));
    }

    public void showProgressDialog(String str) {
        if (this.mProgressDialog != null) {
            ((TextView) this.mProgressDialog.findViewById(R.id.tv_message)).setText(str);
            if (!this.mProgressDialog.isShowing()) {
                this.mProgressDialog.show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public void showToast(int i) {
        showToast(getString(i));
    }

    public void showToast(String str) {
        @SuppressLint("WrongConstant") Toast makeText = Toast.makeText(this, str, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
