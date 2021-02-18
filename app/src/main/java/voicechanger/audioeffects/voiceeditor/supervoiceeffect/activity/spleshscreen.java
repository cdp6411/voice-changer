package voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adclass.BaseActivity;


public class spleshscreen extends BaseActivity {
    private Handler handler;
    Runnable runnable;
    private LinearLayout mLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_spleshscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(spleshscreen.this, MainActivity.class);
                startActivity(i);
                finish();
                handler.removeCallbacks(this);
            }
        };
        handler.postDelayed(runnable, 3000);
        //for animation
        mLinear = findViewById(R.id.linear);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        mLinear.startAnimation(aniFade);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}
