package ch.heigvd.amt.messaging;

import ch.heigvd.amt.entities.Probe;
import ch.heigvd.amt.services.ProbeService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.ObjectMessage;

@ApplicationScoped
public class ProbeConsumer {

    @Inject
    ProbeService probeService;

    @Inject
    ConnectionFactory connectionFactory;

    JMSContext context;

    void onStart(@Observes StartupEvent ev) {
        context = connectionFactory.createContext();
        var queue = context.createQueue("probe");
        var consumer = context.createConsumer(queue);
        consumer.setMessageListener(message -> {
            try {
                if (message instanceof ObjectMessage objectMessage
                        && objectMessage.getObject() instanceof Probe probe) {
                    probeService.executeProbe(probe);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    void onStop(@Observes ShutdownEvent ev) {
        context.close();
    }

}
