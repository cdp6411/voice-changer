package voicechanger.audioeffects.voiceeditor.supervoiceeffect.abtractclass;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.ResolutionUtils;
import java.util.ArrayList;

public abstract class DBBaseAdapter extends BaseAdapter {
    public static final long ANIM_DEFAULT_MIN_SPEED = 300;
    public static final long ANIM_DEFAULT_SPEED = 800;
    public static final String TAG = "DBBaseAdapter";
    private Interpolator interpolator;
    private boolean isAnimate;
    public Context mContext;
    public ArrayList<? extends Object> mListObjects;
    private SparseBooleanArray mPositionsMapper;
    private int screenHeight;
    private int screenWidth;

    public DBBaseAdapter(Activity activity, ArrayList<? extends Object> arrayList) {
        this.mContext = activity;
        this.mListObjects = arrayList;
        this.mPositionsMapper = new SparseBooleanArray(arrayList.size());
        int[] deviceResolution = ResolutionUtils.getDeviceResolution(activity);
        if (deviceResolution != null) {
            this.screenWidth = deviceResolution[0];
            this.screenHeight = deviceResolution[1];
        }
    }

    public void addPositionMapper(int i, boolean z) {
        this.mPositionsMapper.put(i, z);
    }

    public boolean checkPositionMapper(int i) {
        return this.mPositionsMapper.get(i);
    }

    public abstract View getAnimatedView(int i, View view, ViewGroup viewGroup);

    public int getCount() {
        return this.mListObjects != null ? this.mListObjects.size() : 0;
    }

    public Interpolator getInterpolator() {
        return this.interpolator;
    }

    public Object getItem(int i) {
        if (this.mListObjects != null && this.mListObjects.size() > 0) {
            int size = this.mListObjects.size();
            if (i > 0 && i < size) {
                return this.mListObjects.get(i);
            }
        }
        return null;
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public ArrayList<? extends Object> getListObjects() {
        return this.mListObjects;
    }

    public abstract View getNormalView(int i, View view, ViewGroup viewGroup);

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        return this.isAnimate ? getAnimatedView(i, view, viewGroup) : getNormalView(i, view, viewGroup);
    }

    public boolean isAnimate() {
        return this.isAnimate;
    }

    public void onDestroy(boolean z) {
        if (this.mListObjects != null) {
            this.mListObjects.clear();
            if (z) {
                this.mListObjects = null;
            }
        }
    }

    public void setAnimate(boolean z) {
        this.isAnimate = z;
        notifyDataSetChanged();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setListObjects(ArrayList<? extends Object> arrayList, boolean z) {
        if (arrayList != null) {
            if (this.mListObjects != null && z) {
                this.mListObjects.clear();
                this.mListObjects = null;
            }
            this.mPositionsMapper = null;
            this.mPositionsMapper = new SparseBooleanArray(arrayList.size());
            this.mListObjects = arrayList;
            notifyDataSetChanged();
        }
    }
}
