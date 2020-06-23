package io.github.hylexus.jt808.samples.annotation.entity.req;

import com.google.common.collect.Lists;
import io.github.hylexus.jt.annotation.msg.req.Jt808ReqMsgBody;
import io.github.hylexus.jt.annotation.msg.req.basic.BasicField;
import io.github.hylexus.jt.data.converter.req.entity.ReqMsgFieldConverter;
import io.github.hylexus.jt808.msg.RequestMsgBody;
import io.github.hylexus.jt808.msg.RequestMsgHeader;
import io.github.hylexus.jt808.support.entity.scan.RequestMsgHeaderAware;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

import static io.github.hylexus.jt.data.MsgDataType.*;

/**
 * Created At 2020-06-12 15:26
 *
 * @author hylexus
 */
@Data
@Accessors(chain = true)
@Jt808ReqMsgBody(msgType = 0x0704)
public class Msg0704 implements RequestMsgBody, RequestMsgHeaderAware {

    private RequestMsgHeader requestMsgHeader;
    @BasicField(startIndex = 0, dataType = WORD)
    private int itemCount;
    @BasicField(startIndex = 2, dataType = BYTE)
    private byte locationType;
    @BasicField(startIndex = 3, dataType = BYTES, byteCountMethod = "getLocationInfoByteCount")
    private byte[] locationInfoBytes;
    // 这部分List中每个元素的长度不固定，注解暂时无法支持，只能手动解析
    @BasicField(startIndex = 3, dataType = BYTES, byteCountMethod = "getLocationInfoByteCount", customerDataTypeConverterClass = XxxConverter.class)
    private List<LocationUploadRequestMsgBody> locationInfoByteList;

    public void setRequestMsgHeader(RequestMsgHeader requestMsgHeader) {
        this.requestMsgHeader = requestMsgHeader;
    }

    public int getLocationInfoByteCount() {
        return requestMsgHeader.getMsgBodyLength() - 3;
    }

    public static class XxxConverter implements ReqMsgFieldConverter<List<LocationUploadRequestMsgBody>> {
        @Override
        public List<LocationUploadRequestMsgBody> convert(byte[] bytes, byte[] subSeq) {
            // 这里循环解析subSeq
            return Lists.newArrayList();
        }
    }

}
