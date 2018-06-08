package org.web3j.codegen;

import org.slf4j.Logger;

/**
 * A reporter generation that outputs messages using a logger instance.
 */
class LogGenerationReporter implements GenerationReporter {

    private final Logger logger;

    public LogGenerationReporter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void report(String msg) {
        logger.warn(msg);
    }
}
