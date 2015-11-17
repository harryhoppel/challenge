package de.number26.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author vasiliy
 */
public class CommandLineConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineConfig.class);

    private static final int DEFAULT_PORT = 8080;

    private int port;

    public CommandLineConfig() {
        this.port = DEFAULT_PORT;
    }

    public CommandLineConfig(String[] args) {
        try {
            this.port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e) {
            LOGGER.warn("Can't parse command line arguments: " + Arrays.toString(args), e);
            this.port = DEFAULT_PORT;
        }
    }

    public int getPort() {
        return port;
    }
}
