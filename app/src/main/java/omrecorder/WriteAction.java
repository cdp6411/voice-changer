package omrecorder;

import java.io.IOException;
import java.io.OutputStream;

public interface WriteAction {

    public static final class Default implements WriteAction {
        public void execute(byte[] bArr, OutputStream outputStream) {
            try {
                outputStream.write(bArr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void execute(byte[] bArr, OutputStream outputStream);
}
