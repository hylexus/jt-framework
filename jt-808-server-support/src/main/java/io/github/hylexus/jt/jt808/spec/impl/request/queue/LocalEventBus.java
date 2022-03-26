//package io.github.hylexus.jt.jt808.spec.impl.request.queue;
//
//import com.google.common.eventbus.AsyncEventBus;
//import com.google.common.eventbus.SubscriberExceptionHandler;
//import io.github.hylexus.jt.annotation.BuiltinComponent;
//import io.github.hylexus.jt.jt808.spec.Jt808Request;
//import io.github.hylexus.jt.jt808.spec.Jt808RequestMsgQueue;
//
//import java.util.concurrent.Executor;
//
///**
// * @author hylexus
// */
//@BuiltinComponent
//public class LocalEventBus extends AsyncEventBus implements Jt808RequestMsgQueue {
//
//    public LocalEventBus(String identifier, Executor executor) {
//        super(identifier, executor);
//    }
//
//    public LocalEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
//        super(executor, subscriberExceptionHandler);
//    }
//
//    public LocalEventBus(Executor executor) {
//        super(executor);
//    }
//
//    @Override
//    public void postMsg(Jt808Request metadata) throws Throwable {
//        super.post(metadata);
//    }
//}
