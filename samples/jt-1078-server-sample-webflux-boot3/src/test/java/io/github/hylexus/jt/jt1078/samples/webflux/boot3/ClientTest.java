package io.github.hylexus.jt.jt1078.samples.webflux.boot3;

import io.github.hylexus.jt.jt1078.spec.impl.subscription.BuiltinFfmpegTransfer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.*;

@Slf4j
public class ClientTest {

    @Test
    void test2() throws Exception {
        // String command = "ffmpeg -re -i - -vcodec libx264 -acodec copy -f flv rtmp://127.0.0.1:1935/live1/111";
        String command = "ffmpeg -i - -vcodec libx264 -acodec copy -f flv rtmp://127.0.0.1:1935/live1/111";
        final BuiltinFfmpegTransfer subscriber = new BuiltinFfmpegTransfer(command);

        final Thread thread = new Thread(subscriber);
        thread.start();
        new Thread(() -> {
            try (final FileInputStream in = new FileInputStream("/Users/hylexus/tmp/jtt/tem.dat2");) {
                // StreamUtils.copy(in, subscriber.getOutputStream());
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer, 0, 1024)) >= 0) {
                    subscriber.writeData(buffer, 0, read);
                }

                subscriber.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try (
                    final BufferedInputStream inputStream = new BufferedInputStream(subscriber.getErrorStream());
                    final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
            ) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        thread.join();
    }
}
