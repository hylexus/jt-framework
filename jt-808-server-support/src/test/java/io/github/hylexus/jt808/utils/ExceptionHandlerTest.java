package io.github.hylexus.jt808.utils;

import io.github.hylexus.jt.exception.AbstractJtException;
import org.junit.Test;

/**
 * @author hylexus
 * Created At 2020-02-08 2:48 下午
 */
public class ExceptionHandlerTest {

    @Test
    public void testExceptionDepth() {
        int depth = getDepth(Throwable.class, AbstractJtException.class, 0);
        System.out.println(depth);
    }

    private int getDepth(Class<?> declaredException, Class<?> exceptionToMatch, int depth) {
        if (exceptionToMatch.equals(declaredException)) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionToMatch == Throwable.class) {
            return Integer.MAX_VALUE;
        }
        return getDepth(declaredException, exceptionToMatch.getSuperclass(), depth + 1);
    }
}
