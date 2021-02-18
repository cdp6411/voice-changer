package voicechanger.audioeffects.voiceeditor.supervoiceeffect.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity.MyStudioActivity;

import java.io.File;
import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class MyStudioAdapter extends RecyclerView.Adapter<MyStudioAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<String> myList;
    String pth;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMore;
        public LinearLayout ll;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.tvName);
            this.ll = (LinearLayout) view.findViewById(R.id.ll);
            this.ivMore = (ImageView) view.findViewById(R.id.ivMore);
            String file = Environment.getExternalStorageDirectory().toString();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(file);
            stringBuilder.append("/CallVoiceChanger");
            File file2 = new File(stringBuilder.toString());
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(file2);
            stringBuilder2.append("");
            MyStudioAdapter.this.pth = stringBuilder2.toString();
        }
    }

    public MyStudioAdapter(ArrayList<String> arrayList, Context context) {
        this.myList = arrayList;
        this.context = context;
    }

    public int getItemCount() {
        return this.myList.size();
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        myViewHolder.title.setText((CharSequence) this.myList.get(i));
        myViewHolder.ll.setOnClickListener(new OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MyStudioAdapter.this.pth);
                stringBuilder.append("/");
                stringBuilder.append((String) MyStudioAdapter.this.myList.get(i));
                File file = new File(stringBuilder.toString());
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setFlags(268435456);
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
                MyStudioAdapter.this.context.startActivity(intent);
            }
        });
        myViewHolder.ivMore.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MyStudioAdapter.this.context, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(MyStudioAdapter.this.pth);
                        stringBuilder.append("/");
                        stringBuilder.append((String) MyStudioAdapter.this.myList.get(i));

                        if (itemId == R.id.share_item) {

                            File file = new File(stringBuilder.toString());
                            if (file.exists() && file.isFile()) {
                                Intent intent = new Intent("android.intent.action.SEND");
                                intent.setType("audio/*");
                                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                                ((MyStudioActivity) MyStudioAdapter.this.context).startActivity(Intent.createChooser(intent, "Share Via"));
                            }
                        } else if (itemId == R.id.delete_item) {
                            File fD = new File(stringBuilder.toString());
                            if (fD.exists()) {
                                fD.delete();
                            }
                            myList.remove(i);
                            notifyDataSetChanged();
                        }
                        else if (itemId==R.id.Ringtone)
                        {
                            File file = new File(stringBuilder.toString());
                            if (file.isFile()) {
                                startActivity(context, new Intent(Settings.ACTION_SOUND_SETTINGS), null);
                            }

                        }
                        else if (itemId==R.id.notification)
                        {

                            File file = new File(stringBuilder.toString());
                            if (file.isFile()) {
                                startActivity(context, new Intent(Settings.ACTION_SOUND_SETTINGS), null);
                            }
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_list_row, viewGroup, false));
    }
}
