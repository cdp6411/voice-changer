package voicechanger.audioeffects.voiceeditor.supervoiceeffect.task;

import android.os.AsyncTask;

public class DBTask extends AsyncTask<Void, Void, Void> {
    private IDBTaskListener mDownloadListener;

    public DBTask(IDBTaskListener iDBTaskListener) {
        this.mDownloadListener = iDBTaskListener;
    }

    protected Void doInBackground(Void... voidArr) {
        if (this.mDownloadListener != null) {
            this.mDownloadListener.onDoInBackground();
        }
        return null;
    }

    protected void onPostExecute(Void voidR) {
        if (this.mDownloadListener != null) {
            this.mDownloadListener.onPostExcute();
        }
    }

    protected void onPreExecute() {
        if (this.mDownloadListener != null) {
            this.mDownloadListener.onPreExcute();
        }
    }
}
