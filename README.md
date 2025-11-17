# Jakarta JMS and Artemis test
The reproducer aims to recreate the functionality of the reproducer attached to [Payara Community Issue #7481](https://github.com/payara/Payara/issues/7481)

# Additional Configuration
This reproducer assumes Artemis Docker image is ran with the following configuration:

```
docker run --detach --name artemis --restart always -p 61616:61616 -p 8161:8161  apache/activemq-artemis:latest-alpine
```

The reproducer comes with a `domain.xml` file (located in the extras folder)  that should be copied in `${payara_install_folder}/glassfish/domains/domain1/config` and replace the original `domain.xml`.

In addition to that you will have to deploy the `artemis-jakarta-rar-2.41.0.rar`, found in the same `extras` folder as the `domain.xml`.


