package io.github.hylexus.jt808.samples.customized.session;

import io.github.hylexus.jt.jt808.spec.session.DefaultJt808Session;
import io.github.hylexus.jt.jt808.spec.session.Jt808FlowIdGenerator;

/**
 * @author hylexus
 */
public class MySession extends DefaultJt808Session {
    private String someField;

    public MySession(Jt808FlowIdGenerator idGenerator) {
        super(idGenerator);
    }

    public String getSomeField() {
        return someField;
    }

    public void setSomeField(String someField) {
        this.someField = someField;
    }
}
