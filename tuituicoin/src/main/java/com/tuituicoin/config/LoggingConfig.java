package com.tuituicoin.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/* Configuration for logging to file */
public class LoggingConfig {
    public static void configure() throws IOException {
        LogManager.getLogManager().reset();

        LocalDate now = LocalDate.now();

        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));
        String day = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Path logDir = Paths.get("logs", year, month);
        Files.createDirectories(logDir);

        String logFile = logDir.resolve("tuiranode-" + day + ".log").toString();

        Logger rootLogger = Logger.getLogger("TuiTuiCoin");
        rootLogger.setUseParentHandlers(false); // Disable console logging

        FileHandler fileHandler = new FileHandler(logFile, true);
        fileHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
            // Custom log format: [timestamp] - [className.methodName] - [logLevel] - [message]
            return String.format("[%s] - [%s.%s] - [%s] - [%s]",
                record.getMillis(),
                record.getSourceClassName(),
                record.getSourceMethodName(),
                record.getLevel(),
                record.getMessage());
            }
        });

        rootLogger.addHandler(fileHandler);
        rootLogger.setLevel(Level.INFO);
    }
}
