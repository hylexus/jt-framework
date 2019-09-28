package io.github.hylexus.jt.data.msg;

/**
 * @author hylexus
 * Created At 2019-09-29 12:00 上午
 */
public interface AdditionalInfo {

    void setId(int id);

    int getId();

    void setContentLength(int length);

    int getContentLength();

    void setContent(byte[] bytes);

    byte[] getContent();
}
