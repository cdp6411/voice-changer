package omrecorder;

import android.media.AudioRecord;
import java.io.OutputStream;
import omrecorder.AudioChunk.Bytes;
import omrecorder.AudioChunk.Shorts;
import omrecorder.Recorder.OnSilenceListener;

public interface PullTransport {

    public interface OnAudioChunkPulledListener {
        void onAudioChunkPulled(AudioChunk audioChunk);
    }

    public static abstract class AbstractPullTransport implements PullTransport {
        final AudioSource audioRecordSource;
        final OnAudioChunkPulledListener onAudioChunkPulledListener;
        private final UiThread uiThread = new UiThread();

        AbstractPullTransport(AudioSource audioSource, OnAudioChunkPulledListener onAudioChunkPulledListener) {
            this.audioRecordSource = audioSource;
            this.onAudioChunkPulledListener = onAudioChunkPulledListener;
        }

        void postPullEvent(final AudioChunk audioChunk) {
            this.uiThread.execute(new Runnable() {
                public void run() {
                    AbstractPullTransport.this.onAudioChunkPulledListener.onAudioChunkPulled(audioChunk);
                }
            });
        }

        void postSilenceEvent(final OnSilenceListener onSilenceListener, final long j) {
            this.uiThread.execute(new Runnable() {
                public void run() {
                    onSilenceListener.onSilence(j);
                }
            });
        }

        AudioRecord preparedSourceToBePulled() {
            AudioRecord audioRecorder = this.audioRecordSource.audioRecorder();
            audioRecorder.startRecording();
            this.audioRecordSource.isEnableToBePulled(true);
            return audioRecorder;
        }

        public AudioSource source() {
            return this.audioRecordSource;
        }

        public void start(OutputStream outputStream) {
            startPoolingAndWriting(preparedSourceToBePulled(), this.audioRecordSource.minimumBufferSize(), outputStream);
        }

        void startPoolingAndWriting(AudioRecord audioRecord, int i, OutputStream outputStream) {
        }

        public void stop() {
            this.audioRecordSource.isEnableToBePulled(false);
            this.audioRecordSource.audioRecorder().stop();
        }
    }

    public static final class Default extends AbstractPullTransport {
        private final WriteAction writeAction;

        public Default(AudioSource audioSource) {
            this(audioSource, null, new WriteAction.Default());
        }

        public Default(AudioSource audioSource, OnAudioChunkPulledListener onAudioChunkPulledListener) {
            this(audioSource, onAudioChunkPulledListener, new WriteAction.Default());
        }

        public Default(AudioSource audioSource, OnAudioChunkPulledListener onAudioChunkPulledListener, WriteAction writeAction) {
            super(audioSource, onAudioChunkPulledListener);
            this.writeAction = writeAction;
        }

        public Default(AudioSource audioSource, WriteAction writeAction) {
            this(audioSource, null, writeAction);
        }

        void startPoolingAndWriting(AudioRecord audioRecord, int i, OutputStream outputStream) {
            while (this.audioRecordSource.isEnableToBePulled()) {
                AudioChunk bytes = new Bytes(new byte[i]);
                if (-3 != audioRecord.read(bytes.toBytes(), 0, i)) {
                    if (this.onAudioChunkPulledListener != null) {
                        postPullEvent(bytes);
                    }
                    this.writeAction.execute(bytes.toBytes(), outputStream);
                }
            }
        }
    }

    public static final class Noise extends AbstractPullTransport {
        private final Shorts audioChunk;
        private long firstSilenceMoment;
        private int noiseRecordedAfterFirstSilenceThreshold;
        private final OnSilenceListener silenceListener;
        private final long silenceTimeThreshold;
        private final WriteAction writeAction;

        public Noise(AudioSource audioSource) {
            this(audioSource, null, new WriteAction.Default(), null, 200);
        }

        public Noise(AudioSource audioSource, OnAudioChunkPulledListener onAudioChunkPulledListener, OnSilenceListener onSilenceListener, long j) {
            this(audioSource, onAudioChunkPulledListener, new WriteAction.Default(), onSilenceListener, j);
        }

        public Noise(AudioSource audioSource, OnAudioChunkPulledListener onAudioChunkPulledListener, WriteAction writeAction, OnSilenceListener onSilenceListener, long j) {
            super(audioSource, onAudioChunkPulledListener);
            this.firstSilenceMoment = 0;
            this.noiseRecordedAfterFirstSilenceThreshold = 0;
            this.writeAction = writeAction;
            this.silenceListener = onSilenceListener;
            this.silenceTimeThreshold = j;
            this.audioChunk = new Shorts(new short[audioSource.minimumBufferSize()]);
        }

        public Noise(AudioSource audioSource, OnSilenceListener onSilenceListener) {
            this(audioSource, null, new WriteAction.Default(), onSilenceListener, 200);
        }

        public Noise(AudioSource audioSource, OnSilenceListener onSilenceListener, long j) {
            this(audioSource, null, new WriteAction.Default(), onSilenceListener, j);
        }

        public Noise(AudioSource audioSource, WriteAction writeAction, OnSilenceListener onSilenceListener, long j) {
            this(audioSource, null, writeAction, onSilenceListener, j);
        }

        public void start(OutputStream outputStream) {
            AudioRecord audioRecorder = this.audioRecordSource.audioRecorder();
            audioRecorder.startRecording();
            this.audioRecordSource.isEnableToBePulled(true);
            while (this.audioRecordSource.isEnableToBePulled()) {
                this.audioChunk.numberOfShortsRead = audioRecorder.read(this.audioChunk.shorts, 0, this.audioChunk.shorts.length);
                if (-3 != this.audioChunk.numberOfShortsRead) {
                    if (this.onAudioChunkPulledListener != null) {
                        postPullEvent(this.audioChunk);
                    }
                    if (this.audioChunk.peakIndex() > -1) {
                        this.writeAction.execute(this.audioChunk.toBytes(), outputStream);
                        this.firstSilenceMoment = 0;
                        this.noiseRecordedAfterFirstSilenceThreshold++;
                    } else {
                        if (this.firstSilenceMoment == 0) {
                            this.firstSilenceMoment = System.currentTimeMillis();
                        }
                        long currentTimeMillis = System.currentTimeMillis() - this.firstSilenceMoment;
                        if (this.firstSilenceMoment == 0 || currentTimeMillis <= this.silenceTimeThreshold) {
                            this.writeAction.execute(this.audioChunk.toBytes(), outputStream);
                        } else if (currentTimeMillis > 1000 && this.noiseRecordedAfterFirstSilenceThreshold >= 3) {
                            this.noiseRecordedAfterFirstSilenceThreshold = 0;
                            if (this.silenceListener != null) {
                                postSilenceEvent(this.silenceListener, currentTimeMillis);
                            }
                        }
                    }
                }
            }
        }
    }

    AudioSource source();

    void start(OutputStream outputStream);

    void stop();
}
