package voicechanger.audioeffects.voiceeditor.supervoiceeffect.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.abtractclass.DBBaseAdapter;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.object.EffectObject;
import java.util.ArrayList;

public class EffectAdapter extends DBBaseAdapter {
    public static final String TAG = "EffectAdapter";
    public int[] imgs = new int[]{R.drawable.penguin1, R.drawable.santaclaus, R.drawable.robotics, R.drawable.cave1, R.drawable.monster1, R.drawable.nervous1, R.drawable.drink, R.drawable.chipmunk, R.drawable.babyboy, R.drawable.deceased, R.drawable.reverse1, R.drawable.alps1};
    private OnEffectListener onEffectListener;

    public interface OnEffectListener {
        void onPlayEffect(EffectObject effectObject);

        void onSaveEffect(EffectObject effectObject);

        void onShareEffect(EffectObject effectObject);
    }

    private static class ViewHolder {
        public ImageView ivThumb;
        public ImageView mBtnPlay;
        public ImageView mBtnSave;
        public ImageView mBtnShare;
        public TextView mTvName;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(String anonymousClass1) {
            this();
        }
    }

    public EffectAdapter(Activity activity, ArrayList<? extends Object> arrayList, Typeface typeface) {
        super(activity, arrayList);
    }

    public View getAnimatedView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    public View getNormalView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View inflate;
        if (view == null) {
            viewHolder = new ViewHolder();
            inflate = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.item_effects, null);
            inflate.setTag(viewHolder);
        } else {
            inflate = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTvName = (TextView) inflate.findViewById(R.id.tv_name_effect);
        viewHolder.mBtnPlay = (ImageView) inflate.findViewById(R.id.btn_play);
        viewHolder.mBtnSave = (ImageView) inflate.findViewById(R.id.btn_save);
        viewHolder.mBtnShare = (ImageView) inflate.findViewById(R.id.btn_share);
        viewHolder.ivThumb = (ImageView) inflate.findViewById(R.id.ivThumb);
        viewHolder.ivThumb.setImageResource(this.imgs[i]);
        final EffectObject effectObject = (EffectObject) this.mListObjects.get(i);
        viewHolder.mBtnPlay.setImageResource(effectObject.isPlaying() ? R.drawable.pause_t : R.drawable.play_t);
        viewHolder.mTvName.setText(effectObject.getName());
        viewHolder.mBtnPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (EffectAdapter.this.onEffectListener != null) {
                    EffectAdapter.this.onEffectListener.onPlayEffect(effectObject);
                }
            }
        });
        viewHolder.mBtnSave.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (EffectAdapter.this.onEffectListener != null) {
                    EffectAdapter.this.onEffectListener.onShareEffect(effectObject);
                }
            }
        });
        viewHolder.mBtnShare.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (EffectAdapter.this.onEffectListener != null) {
                    EffectAdapter.this.onEffectListener.onSaveEffect(effectObject);
                }
            }
        });
        return inflate;
    }

    public void setOnEffectListener(OnEffectListener onEffectListener) {
        this.onEffectListener = onEffectListener;
    }
}
