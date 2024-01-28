package io.github.hylexus.jt.jt808.spec.builtin.msg.req.extra;

public interface ExtraItemSupport {
    // 附加项 ID
    int getId();

    // 附加信息长度
    int getContentLength();

    // 附加信息内容
    byte[] getContent();
}
