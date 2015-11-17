package de.number26;

import de.number26.configuration.CommandLineConfig;

/**
 * @author vasiliy
 */
public interface Application {
    void start(CommandLineConfig commandLineConfig) throws Exception;
    void stop();
}
