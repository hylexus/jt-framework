package io.github.hylexus.jt808.msg.req;

import io.github.hylexus.jt808.msg.AbstractRequestMsg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hylexus
 * createdAt 2018/12/28
 **/
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class AuthRequestMsg extends AbstractRequestMsg {
    private String authCode;

    public AuthRequestMsg() {
    }

    public AuthRequestMsg(AbstractRequestMsg other) {
        super(other);
    }
}
