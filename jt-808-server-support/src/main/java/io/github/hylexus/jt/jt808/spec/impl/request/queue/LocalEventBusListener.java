//package io.github.hylexus.jt.jt808.spec.impl.request.queue;
//
//import com.google.common.eventbus.Subscribe;
//import io.github.hylexus.jt.annotation.BuiltinComponent;
//import io.github.hylexus.jt.jt808.spec.Jt808Request;
//import io.github.hylexus.jt.jt808.spec.session.Jt808SessionManager;
//import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageEventListener;
//import io.github.hylexus.jt.jt808.support.codec.Jt808RequestSubPackageStorage;
//import io.github.hylexus.jt.jt808.support.dispatcher.Jt808DispatcherHandler;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//
///**
// * Created At 2019-08-24 16:44
// *
// * @author hylexus
// */
//@Slf4j
//@BuiltinComponent
//public class LocalEventBusListener extends AbstractJt808RequestMsgQueueListener<LocalEventBus> {
//
//    public LocalEventBusListener(
//            LocalEventBus queue, Jt808DispatcherHandler dispatcherHandler,
//            Jt808SessionManager sessionManager, Jt808RequestSubPackageStorage subPackageStorage,
//            Jt808RequestSubPackageEventListener requestSubPackageEventListener) {
//        super(queue, dispatcherHandler, sessionManager, subPackageStorage, requestSubPackageEventListener);
//    }
//
//    @PostConstruct
//    public void init() {
//        //noinspection UnstableApiUsage
//        queue.register(this);
//    }
//
//    @Subscribe
//    public void listen(Jt808Request request) throws IOException, InterruptedException {
//        consumeMsg(request);
//    }
//}
