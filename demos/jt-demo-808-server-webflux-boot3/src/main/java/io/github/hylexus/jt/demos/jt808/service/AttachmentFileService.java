package io.github.hylexus.jt.demos.jt808.service;

import io.github.hylexus.jt.demos.jt808.configuration.pros.Jt808AppProps;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1210Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg30316364Alias;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AttachmentFileService {

    private final Jt808AppProps appProps;

    public AttachmentFileService(Jt808AppProps appProps) {
        this.appProps = appProps;
    }

    public void writeDataFragment(Jt808Session session, BuiltinMsg30316364Alias body, BuiltinMsg1210Alias group) {
        // 这里就瞎写了一个路径  看你需求随便改
        final String filePath = appProps.getAttachment().getTemporaryPath() + File.separator
                + session.terminalId() + File.separator
                + new SimpleDateFormat("yyyy-MM-dd HH").format(new Date()) + File.separator
                + group.getMessageType() + File.separator
                + group.getAlarmNo() + File.separator
                + body.getFileName().trim();

        final File tempFile = new File(filePath);
        if (!tempFile.exists() && !tempFile.getParentFile().exists()) {
            if (!tempFile.getParentFile().mkdirs()) {
                throw new RuntimeException("新建文件失败:" + tempFile);
            }
        }
        try (final RandomAccessFile file = new RandomAccessFile(filePath, "rws")) {
            file.seek(body.getDataOffset());
            file.write(body.getData(), 0, (int) body.getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
