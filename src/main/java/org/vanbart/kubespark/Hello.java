package org.vanbart.kubespark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

public class Hello {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(Hello.class);

    private static Configuration configuration;

    public static void main(String[] args) {
        log.info("main({})", (Object[]) args);
        try {
            configuration = getConfiguration();
        } catch (IOException e) {
            log.error("Failed to read configuration", e);
            System.exit(1);
        }
        Spark.get("/hello", (req, resp) -> "Hello, World from Spark");
        Spark.get("/greeting", (req, resp) -> {
            log.info("handle GET, will return \"{}\"", configuration.getGreeting());
            return configuration.getGreeting();
        });
    }

    private static Configuration getConfiguration() throws IOException {
        log.info("getConfiguration()");
        Configuration configuration = null;
        String location = System.getenv("CONFIGLOC");
        if (location == null) {
            log.warn("CONFIGLOC not set, falling back to default configuration");
            configuration = objectMapper.readValue(Hello.class.getClassLoader().getResource("configuration.json"), Configuration.class);
        } else {
            log.info("Attempt to read config from {}", location);
            configuration = objectMapper.readValue(Paths.get(location).toFile(), Configuration.class);
        }
        return configuration;
    }
}
