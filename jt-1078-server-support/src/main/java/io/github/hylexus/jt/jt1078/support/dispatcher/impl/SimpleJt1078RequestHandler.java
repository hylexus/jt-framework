// package io.github.hylexus.jt.jt1078.support.dispatcher.impl;
//
// import io.github.hylexus.jt.jt1078.spec.Jt1078Request;
// import io.github.hylexus.jt.jt1078.spec.Jt1078Session;
// import io.github.hylexus.jt.jt1078.spec.Jt1078SessionManager;
// import io.github.hylexus.jt.jt1078.support.codec.Jt1078Collector;
// import io.github.hylexus.jt.jt1078.support.dispatcher.Jt1078RequestHandler;
// import lombok.extern.slf4j.Slf4j;
//
// import java.util.Collection;
//
// @Slf4j
// public class SimpleJt1078RequestHandler implements Jt1078RequestHandler {
//
//     private final Jt1078SessionManager sessionManager;
//
//     public SimpleJt1078RequestHandler(Jt1078SessionManager sessionManager) {
//         this.sessionManager = sessionManager;
//     }
//
//     @Override
//     public boolean support(Jt1078Request request) {
//         return true;
//     }
//
//     @Override
//     public void handle(Jt1078Request request) {
//         final Jt1078Session session = this.sessionManager.findBySimOrThrow(request.sim());
//         final Collection<Jt1078Collector> converterList = session.getChannelConverters(request.channelNumber());
//
//         boolean notFound = true;
//         for (Jt1078Collector converter : converterList) {
//             if (converter.support(request)) {
//                 notFound = false;
//                 converter.collect(request);
//             }
//         }
//
//         if (notFound) {
//             log.error("No collector found, sim={},channelNumber={}, pt={}", request.sim(), request.channelNumber(), request.payloadType());
//         }
//     }
// }
