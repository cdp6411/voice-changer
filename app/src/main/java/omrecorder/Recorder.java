package omrecorder;

public interface Recorder {

    public interface OnSilenceListener {
        void onSilence(long j);
    }

    void pauseRecording();

    void resumeRecording();

    void startRecording();

    void stopRecording();
}
