package org.acme;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class KafkaController {

    private FlowableEmitter<KafkaMessage<String, String>> emitter;

    private Flowable<KafkaMessage<String, String>> outgoingStream;

    @PostConstruct
    void init() {
        outgoingStream = Flowable.create(emitter -> this.emitter = emitter, BackpressureStrategy.BUFFER);
    }

    public void produce(String id,String message) {
        System.out.println(id+"::"+message);
        emitter.onNext(KafkaMessage.of(id, "{\"data\": {"+ message + "}"));
    }

    @PreDestroy
    void dispose() {
        emitter.onComplete();
    }

    @Outgoing("var-calc-request")
    Publisher<KafkaMessage<String, String>> produceKafkaMessage() {
        return outgoingStream;
    }


}