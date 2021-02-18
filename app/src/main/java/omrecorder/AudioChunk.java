package omrecorder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import androidx.core.view.MotionEventCompat;

public interface AudioChunk {

    public static final class Bytes implements AudioChunk {
        private static final double REFERENCE = 0.6d;
        final byte[] bytes;
        int numberOfBytesRead;

        Bytes(byte[] bArr) {
            this.bytes = bArr;
        }

        public double maxAmplitude() {
            short[] toShorts = toShorts();
            int length = toShorts.length;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                if (toShorts[i2] >= i) {
                    i = toShorts[i2];
                }
            }
            return (double) ((int) (Math.log10(((double) i) / REFERENCE) * 20.0d));
        }

        public byte[] toBytes() {
            return this.bytes;
        }

        public short[] toShorts() {
            short[] sArr = new short[(this.bytes.length / 2)];
            ByteBuffer.wrap(this.bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(sArr);
            return sArr;
        }
    }

    public static final class Shorts implements AudioChunk {
        private static final double REFERENCE = 0.6d;
        private static final short SILENCE_THRESHOLD = (short) 2700;
        int numberOfShortsRead;
        final short[] shorts;

        Shorts(short[] sArr) {
            this.shorts = sArr;
        }

        public double maxAmplitude() {
            int length = this.shorts.length;
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                if (this.shorts[i2] >= i) {
                    i = this.shorts[i2];
                }
            }
            return (double) ((int) (Math.log10(((double) i) / REFERENCE) * 20.0d));
        }

        int peakIndex() {
            int length = this.shorts.length;
            int i = 0;
            while (i < length) {
                if (this.shorts[i] >= SILENCE_THRESHOLD || this.shorts[i] <= (short) -2700) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public byte[] toBytes() {
            byte[] bArr = new byte[(this.numberOfShortsRead * 2)];
            int i = 0;
            int i2 = 0;
            while (i != this.numberOfShortsRead) {
                bArr[i2] = (byte) (this.shorts[i] & 255);
                bArr[i2 + 1] = (byte) ((this.shorts[i] & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
                i++;
                i2 += 2;
            }
            return bArr;
        }

        public short[] toShorts() {
            return this.shorts;
        }
    }

    double maxAmplitude();

    byte[] toBytes();

    short[] toShorts();
}
