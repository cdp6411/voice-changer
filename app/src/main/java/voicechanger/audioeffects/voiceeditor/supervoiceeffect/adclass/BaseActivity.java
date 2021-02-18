package voicechanger.audioeffects.voiceeditor.supervoiceeffect.adclass;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.u.securekeys.SecureEnvironment;
import com.u.securekeys.annotation.SecureKey;
import com.u.securekeys.annotation.SecureKeys;

import androidx.appcompat.app.AppCompatActivity;


@SecureKeys({

        @SecureKey(key = "privacy", value = ""),
        @SecureKey(key = "account", value = ""),

        //banner
        @SecureKey(key = "creation btm 1 fb banner", value = ""),
        @SecureKey(key = "creation btm 2 fb banner", value = ""),

        @SecureKey(key = "effectact btm 1 fb banner", value = ""),
        @SecureKey(key = "effectact btm 2 fb banner", value = ""),

        //interstitial
        @SecureKey(key = "2 sec 1 fb int", value = ""),
        @SecureKey(key = "2 sec 2 fb int", value = ""),

        @SecureKey(key = "mainact start clk 1 fb int", value = ""),
        @SecureKey(key = "mainact start clk 2 fb int", value = ""),

        @SecureKey(key = "mainact caretion clk 1 fb int", value = ""),
        @SecureKey(key = "mainact caretion clk 2 fb int", value = ""),

        @SecureKey(key = "effect back clk 1 fb int", value = ""),
        @SecureKey(key = "effect back clk 2 fb int", value = ""),

        // native
        @SecureKey(key = "mainact btm 1 fb native", value = ""),
        @SecureKey(key = "mainact btm 2 fb native", value = ""),

        @SecureKey(key = "splash dialog 1 fb native", value = ""),
        @SecureKey(key = "splash dialog 2 fb native", value = ""),



})

public class BaseActivity extends AppCompatActivity {


    public interface ShowMethod {
        void callmethodLoaded();

        void callmethodFailed();
    }

}
