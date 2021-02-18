package omrecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

final class Wav extends AbstractRecorder {
    private final RandomAccessFile wavFile;

    public Wav(PullTransport pullTransport, File file) {
        super(pullTransport, file);
        this.wavFile = randomAccessFile(file);
    }

    private RandomAccessFile randomAccessFile(File file) {
        try {
            return new RandomAccessFile(file, "rw");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void writeWavHeader() {
        long size = 0;
        try {
            size = new FileInputStream(this.b).getChannel().size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.wavFile.seek(0);
            this.wavFile.write(new WavHeader(this.a.source(), size).toBytes());
            this.wavFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        super.stopRecording();
        writeWavHeader();
    }
}
