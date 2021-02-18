package omrecorder;

import java.io.File;

final class Pcm extends AbstractRecorder {
    public Pcm(PullTransport pullTransport, File file) {
        super(pullTransport, file);
    }
}
