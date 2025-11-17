package fish.payara.support.jmstest;


import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(
            propertyName = "resourceAdapter", 
            propertyValue = "artemis-jakarta-rar-2.41.0"
        ),
        @ActivationConfigProperty(
            propertyName = "destinationLookup",
            propertyValue = "jms/demoQueue"
        ),
        @ActivationConfigProperty(
            propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue"
        )
    }
)
public class SomethingMDB implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SomethingMDB.class);

    // Inject the Core EJB to access the find and send logic
    @Inject
    private MessageEJB messageEJB; 

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;

        try {
            String messageText = textMessage.getText();
            LOGGER.info("RECEIVED: " + messageText);
            
            Long entityId = Long.valueOf(messageText);

            messageEJB.findById(entityId).ifPresentOrElse(
                something -> messageEJB.createSomethingNewAndSend(),
                () -> LOGGER.warn("Received ID could not be found: " + messageText)
            );

        } catch (JMSException e) {
            LOGGER.error("Error processing message: " + e.getMessage());
            throw new RuntimeException("Could not process message", e);
        }
    }
}
