package ch.heigvd.amt.messaging;

import ch.heigvd.amt.entities.Probe;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ProbeProducer {

    @Inject
    EntityManager entityManager;

    @Inject
    ConnectionFactory connectionFactory;

    @Scheduled(every = "1s")
    public void checkProbes() {
        try(var context = connectionFactory.createContext()) {
            var queue = context.createQueue("probe");
            var producer = context.createProducer();
            for (var probe : entityManager.createQuery("SELECT p FROM Probe p", Probe.class).getResultList()) {
                var objectMessage = context.createObjectMessage();
                objectMessage.setObject(probe);
                producer.send(queue, objectMessage);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }


}
