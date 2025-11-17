package fish.payara.support.jmstest;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
@DependsOn("PersistenceUnitInitializer") 
public class MessageEJB {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEJB.class);

    @PersistenceContext(unitName = "SomethingPU")
    private EntityManager em;

    @Resource(lookup = "jms/demoConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/demoQueue")
    private Queue workflowQueue;

    @PostConstruct
    public void onStartup() {
        LOGGER.info("Starting up and sending initial message.");
        startInitialMessageLoop();
    }

    @Asynchronous
    public void startInitialMessageLoop() {
        createSomethingNewAndSend();
    }

    public void createSomethingNewAndSend() {

        SomethingPersistent somethingPersistent = new SomethingPersistent();
        em.persist(somethingPersistent);
        em.flush(); 
        Long savedId = somethingPersistent.getId();
        

        try (JMSContext context = connectionFactory.createContext(JMSContext.SESSION_TRANSACTED)) {
            String messageText = String.valueOf(savedId);  
            context.createProducer()
                   .setDeliveryMode(jakarta.jms.DeliveryMode.PERSISTENT)
                   .send(workflowQueue, messageText);
            LOGGER.info("SENT (ID=" + savedId + "): " + messageText);
        }
    }
    
    public Optional<SomethingPersistent> findById(Long id) {
        return Optional.ofNullable(em.find(SomethingPersistent.class, id));
    }
}