package fish.payara.support.jmstest;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

@Singleton
@Startup
public class PersistenceUnitInitializer {
    @PersistenceContext(unitName = "SomethingPU")
    private EntityManager em;
}
