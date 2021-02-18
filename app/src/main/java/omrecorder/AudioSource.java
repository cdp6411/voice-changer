package omrecorder;

import android.media.AudioRecord;

public interface AudioSource {

    public static class Smart implements AudioSource {
        private final int audioEncoding;
        private final AudioRecord audioRecord;
        private final int audioSource;
        private final int channelPositionMask;
        private final int frequency;
        private volatile boolean pull;

        public Smart(int i, int i2, int i3, int i4) {
            this.audioSource = i;
            this.audioEncoding = i2;
            this.channelPositionMask = i3;
            this.frequency = i4;
            this.audioRecord = new AudioRecord(i, i4, i3, i2, minimumBufferSize());
        }

        public AudioRecord audioRecorder() {
            return this.audioRecord;
        }

        public byte bitsPerSample() {
            return (this.audioEncoding != 2 && this.audioEncoding == 3) ? (byte) 8 : (byte) 16;
        }

        public int channelPositionMask() {
            return this.channelPositionMask;
        }

        public int frequency() {
            return this.frequency;
        }

        public void isEnableToBePulled(boolean z) {
            this.pull = z;
        }

        public boolean isEnableToBePulled() {
            return this.pull;
        }

        public int minimumBufferSize() {
            return AudioRecord.getMinBufferSize(this.frequency, this.channelPositionMask, this.audioEncoding);
        }
    }

    AudioRecord audioRecorder();

    byte bitsPerSample();

    int channelPositionMask();

    int frequency();

    void isEnableToBePulled(boolean z);

    boolean isEnableToBePulled();

    int minimumBufferSize();
}
