package io.github.hylexus.jt808.session;

import org.springframework.beans.factory.FactoryBean;

/**
 * Created At 2020-06-20 16:28
 *
 * @author hylexus
 */
public class Jt808SessionManagerFactoryBean implements FactoryBean<Jt808SessionManager> {

    @Override
    public Jt808SessionManager getObject() throws Exception {
        return SessionManager.getInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return Jt808SessionManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
