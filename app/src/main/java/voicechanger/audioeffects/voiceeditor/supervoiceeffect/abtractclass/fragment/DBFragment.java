package voicechanger.audioeffects.voiceeditor.supervoiceeffect.abtractclass.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.StringUtils;

public abstract class DBFragment extends Fragment implements IDBFragmentConstants {
    public static final String TAG = "DBFragment";
    private boolean isAllowFindViewContinous;
    private boolean isExtractData;
    public int mIdFragment;
    public String mNameFragment;
    public View mRootView;

    public void backToHome(FragmentActivity fragmentActivity) {
        FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        beginTransaction.remove(this);
        Fragment fragmentHome = getFragmentHome(fragmentActivity);
        if (fragmentHome != null) {
            beginTransaction.show(fragmentHome);
        }
        beginTransaction.commit();
    }

    public abstract void findView();

    public Fragment getFragmentHome(FragmentActivity fragmentActivity) {
        return this.mIdFragment > 0 ? fragmentActivity.getSupportFragmentManager().findFragmentById(this.mIdFragment) : StringUtils.isEmptyString(this.mNameFragment) ? null : fragmentActivity.getSupportFragmentManager().findFragmentByTag(this.mNameFragment);
    }

    public boolean isAllowFindViewContinous() {
        return this.isAllowFindViewContinous;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mRootView = onInflateLayout(layoutInflater, viewGroup, bundle);
        return this.mRootView;
    }

    public void onExtractData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mNameFragment = arguments.getString(IDBFragmentConstants.KEY_NAME_FRAGMENT);
            this.mIdFragment = arguments.getInt(IDBFragmentConstants.KEY_ID_FRAGMENT);
        }
    }

    public abstract View onInflateLayout(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    public void onStart() {
        super.onStart();
        if (!this.isExtractData) {
            this.isExtractData = true;
            onExtractData();
        } else if (!this.isAllowFindViewContinous) {
            return;
        }
        findView();
    }

    public void setAllowFindViewContinous(boolean z) {
        this.isAllowFindViewContinous = z;
    }
}
