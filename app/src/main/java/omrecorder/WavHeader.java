package omrecorder;

final class WavHeader {
    private final AudioSource audioRecordSource;
    private final long totalAudioLength;

    WavHeader(AudioSource audioSource, long j) {
        this.audioRecordSource = audioSource;
        this.totalAudioLength = j;
    }

    private byte[] wavFileHeader(long j, long j2, long j3, int i, long j4, byte b) {
        int i2 = i;
        return new byte[]{(byte) 82, (byte) 73, (byte) 70, (byte) 70, (byte) ((int) (j2 & 255)), (byte) ((int) ((j2 >> 8) & 255)), (byte) ((int) ((j2 >> 16) & 255)), (byte) ((int) ((j2 >> 24) & 255)), (byte) 87, (byte) 65, (byte) 86, (byte) 69, (byte) 102, (byte) 109, (byte) 116, (byte) 32, (byte) 16, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) i2, (byte) 0, (byte) ((int) (j3 & 255)), (byte) ((int) ((j3 >> 8) & 255)), (byte) ((int) ((j3 >> 16) & 255)), (byte) ((int) ((j3 >> 24) & 255)), (byte) ((int) (j4 & 255)), (byte) ((int) ((j4 >> 8) & 255)), (byte) ((int) ((j4 >> 16) & 255)), (byte) ((int) ((j4 >> 24) & 255)), (byte) ((b / 8) * i2), (byte) 0, b, (byte) 0, (byte) 100, (byte) 97, (byte) 116, (byte) 97, (byte) ((int) (j & 255)), (byte) ((int) ((j >> 8) & 255)), (byte) ((int) ((j >> 16) & 255)), (byte) ((int) ((j >> 24) & 255))};
    }

    public byte[] toBytes() {
        long frequency = (long) this.audioRecordSource.frequency();
        int i = this.audioRecordSource.channelPositionMask() == 16 ? 1 : 2;
        byte bitsPerSample = this.audioRecordSource.bitsPerSample();
        return wavFileHeader(this.totalAudioLength, this.totalAudioLength + 36, frequency, i, ((((long) bitsPerSample) * frequency) * ((long) i)) / 8, bitsPerSample);
    }
}
