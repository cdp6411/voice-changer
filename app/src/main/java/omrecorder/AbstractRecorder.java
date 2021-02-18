package omrecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

abstract class AbstractRecorder implements Recorder {
    protected final PullTransport a;
    protected final File b;
    private final OutputStream outputStream;

    protected AbstractRecorder(PullTransport pullTransport, File file) {
        this.a = pullTransport;
        this.b = file;
        this.outputStream = outputStream(file);
    }

    private OutputStream outputStream(File file) {
        if (file == null) {
            throw new RuntimeException("file is null !");
        }
        try {
            return new FileOutputStream(file);
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("could not build OutputStream from this file");
            stringBuilder.append(file.getName());
            throw new RuntimeException(stringBuilder.toString(), e);
        }
    }

    public void pauseRecording() {
        this.a.source().isEnableToBePulled(false);
    }

    public void resumeRecording() {
        this.a.source().isEnableToBePulled(true);
        startRecording();
    }

    public void startRecording() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    AbstractRecorder.this.a.start(AbstractRecorder.this.outputStream);
                } catch (Throwable e) {
                    RuntimeException runtimeException = new RuntimeException(e);
                }
            }
        }).start();
    }

    public void stopRecording() {
        this.a.stop();
    }
}
