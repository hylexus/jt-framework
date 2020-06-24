package io.github.hylexus.jt808.samples.customized.session;

import io.github.hylexus.jt808.session.Session;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created At 2020-06-24 15:07
 *
 * @author hylexus
 */
@Getter
@Setter
@ToString
public class MySession extends Session {
    private String someField;
}
