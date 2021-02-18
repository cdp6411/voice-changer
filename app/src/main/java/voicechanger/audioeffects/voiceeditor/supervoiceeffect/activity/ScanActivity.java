package voicechanger.audioeffects.voiceeditor.supervoiceeffect.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.provider.MediaStore.Audio.Media;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.R;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.adapter.SongAdapter;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.model.Song;
import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity {
    String[] STAR = new String[]{"*"};
    ImageView ivBack;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView my_recycler_view_song;
    SongAdapter songAdapter;
    ArrayList<Song> songs;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_scan);
        StrictMode.setVmPolicy(new Builder().build());
        this.songs = new ArrayList();
        this.my_recycler_view_song = (RecyclerView) findViewById(R.id.my_recycler_view_song);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.ivBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ScanActivity.this.onBackPressed();
            }
        });
        Cursor managedQuery = managedQuery(Media.EXTERNAL_CONTENT_URI, this.STAR, "is_music != 0", null, null);
        if (managedQuery == null || !managedQuery.moveToFirst()) {
            this.layoutManager = new LinearLayoutManager(this);
            this.songAdapter = new SongAdapter(this.songs, this);
            this.my_recycler_view_song.setLayoutManager(this.layoutManager);
            this.my_recycler_view_song.setAdapter(this.songAdapter);
        }
        do {
            this.songs.add(new Song(managedQuery.getString(managedQuery.getColumnIndex("_display_name")), managedQuery.getString(managedQuery.getColumnIndex("_data"))));
        } while (managedQuery.moveToNext());
        this.layoutManager = new LinearLayoutManager(this);
        this.songAdapter = new SongAdapter(this.songs, this);
        this.my_recycler_view_song.setLayoutManager(this.layoutManager);
        this.my_recycler_view_song.setAdapter(this.songAdapter);
    }
}
