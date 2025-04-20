package io.github.hylexus.jt.jt808.samples.attachment.boot2.service;

import io.github.hylexus.jt.jt808.spec.builtin.msg.extension.location.AlarmIdentifierAlias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg1210Alias;
import io.github.hylexus.jt.jt808.spec.builtin.msg.req.BuiltinMsg30316364Alias;
import io.github.hylexus.jt.jt808.spec.session.Jt808Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AttachmentFileService {

    private final String temporaryPath;

    public AttachmentFileService(@Value("${demo-config.attachment-file-temp-path}") String temporaryPath) {
        this.temporaryPath = temporaryPath;
        final File file = new File(temporaryPath);
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("创建临时目录失败:" + temporaryPath);
        }
    }

    public void writeDataFragment(Jt808Session session, BuiltinMsg30316364Alias body, BuiltinMsg1210Alias group) {
        final AlarmIdentifierAlias alarmIdentifier = group.getAlarmIdentifier();
        final LocalDateTime localDateTime = alarmIdentifier.getTime();
        // 这里就瞎写了一个路径  看你需求随便改
        final String filePath = this.temporaryPath + File.separator
                                + DateTimeFormatter.ofPattern("yyyyMMddHH").format(localDateTime) + File.separator
                                + session.terminalId() + File.separator
                                + group.getMessageType() + File.separator
                                // + group.getAlarmNo() + File.separator
                                + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(localDateTime) + "-" + group.getAlarmNo() + File.separator
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
