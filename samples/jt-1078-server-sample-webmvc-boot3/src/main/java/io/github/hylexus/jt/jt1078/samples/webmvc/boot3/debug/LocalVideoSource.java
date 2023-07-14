package io.github.hylexus.jt.jt1078.samples.webmvc.boot3.debug;

import io.github.hylexus.jt.utils.HexStringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LocalVideoSource {
    public static void main(String[] args) throws Exception {
        try (
                final Socket socket = new Socket("localhost", 61078);
                final OutputStream outputStream = socket.getOutputStream();
                final InputStream inputStream = Files.newInputStream(Paths.get("/Users/hylexus/Desktop/jtt/1078/1.dat"));
        ) {

            int len;
            final byte[] buffer = new byte[2048];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
                Thread.sleep(10);
                System.out.println("send data " + HexStringUtils.bytes2HexString(buffer));
            }
        }
    }
}
