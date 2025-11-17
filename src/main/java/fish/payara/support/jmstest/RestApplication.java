package fish.payara.support.jmstest;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@ApplicationPath("/rest")
public class RestApplication extends Application {
    @Inject
    private MessageEJB messageEJB;

    @GET
    @Path("/send")
    @Produces(MediaType.TEXT_PLAIN)
    public String trigger() {
        messageEJB.createSomethingNewAndSend();
        return "Message sent.";
    }
}
