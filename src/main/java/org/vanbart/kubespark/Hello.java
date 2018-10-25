package org.vanbart.kubespark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Hello {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(Hello.class);

    private static Configuration configuration;

    public static void main(String[] args) {
        log.debug("main({})", (Object[]) args);
        String configloc = System.getenv("CONFIGLOC");
        configuration = getConfiguration();
        Spark.get("/hello", (req, resp) -> "Hello, World from Spark");
        Spark.get("/greeting", (req, resp) -> configuration.getGreeting());
    }

    private static Configuration getConfiguration() {
        Configuration configuration = null;
        String location = System.getenv("CONFIGLOC");
        if (location == null) {
            log.warn("CONFIGLOC not set, falling back to default configuration");
            URL url = Hello.class.getClassLoader().getResource("configuration.json");
            location = url.getFile();
        }

        try {
            log.info("read configuration:{}", location);
            configuration = objectMapper.readValue(new File(location), Configuration.class);
        } catch (IOException e) {
            log.error("Failed to read '{}'", location, e);
            System.exit(2);
        }
        return configuration;
    }
}
