package voicechanger.audioeffects.voiceeditor.supervoiceeffect.task;

public interface IDBTaskListener {
    void onDoInBackground();

    void onPostExcute();

    void onPreExcute();
}
