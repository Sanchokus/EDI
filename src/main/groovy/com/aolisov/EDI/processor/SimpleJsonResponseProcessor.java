package com.aolisov.EDI.processor;

import com.aolisov.EDI.dto.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Generate text response based on lower-case text message from user.
public class SimpleJsonResponseProcessor implements ResponseProcessor {

    Logger logger = LoggerFactory.getLogger(SimpleJsonResponseProcessor.class);

    private static final String GREETING_TEXT = "Hi! I represent my creator, software developer Aleksandr Olisov.";
    private static final String DONT_UNDERSTAND_TEXT  = "Sorry, I don't understand you.";
    private static final String COMMANDS_LIST_PREFIX_TEXT = "You can talk to me with these commands:";
    private static final String SERVER_ERROR_TEXT  = "I have some problems on the server side... Sorry.";

    private static final String TELEGRAM_HELP_MESSAGE_TEXT = "/help";
    private static final String TELEGRAM_START_MESSAGE_TEXT = "/start";

    private static final String MAP_FILE_PATH = "commands.json";
    private static Map<String, String> COMMANDS_MAP = new HashMap<>();

    private boolean initialized = false;

    @PostConstruct
    public void loadProperties() throws IOException {
        logger.info("Loading properties for response processor...");
        Resource resource = new ClassPathResource(MAP_FILE_PATH);
        InputStream inputStream = resource.getInputStream();

        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");

        String commandsString = writer.toString();

        COMMANDS_MAP = new ObjectMapper().readValue(commandsString, Map.class);
        initialized = true;
        logger.info("Properties for response processor are successfully loaded.");
    }

    @Override
    public String getResponse(Message request) {
        String requestString = request.getText().trim().toLowerCase();
        try {
            if(!initialized) {
                loadProperties();
            }
            if(TELEGRAM_HELP_MESSAGE_TEXT.equals(requestString)) {
                return createHelpMessageText();
            }
            if(TELEGRAM_START_MESSAGE_TEXT.equals(requestString)) {
                return createStartMessageText();
            }
            String response = COMMANDS_MAP.get(requestString);
            if(response != null) {
                return response;
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(DONT_UNDERSTAND_TEXT)
                        .append("\n")
                        .append(COMMANDS_LIST_PREFIX_TEXT)
                        .append("\n")
                        .append(getAllCommandsString());

                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error with response to: {}" + request);
            return SERVER_ERROR_TEXT;
        }
    }

    private String createHelpMessageText() {
        return createStartMessageText();
    }

    private String createStartMessageText() {
        StringBuilder sb = new StringBuilder();
        sb.append(GREETING_TEXT)
                .append("\n")
                .append(COMMANDS_LIST_PREFIX_TEXT)
                .append("\n")
                .append(getAllCommandsString());
        return sb.toString();
    }

    private String getAllCommandsString() {
        StringBuilder sb = new StringBuilder();
        List<String> commands = COMMANDS_MAP.entrySet().stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        Collections.sort(commands);
        for(String command: commands) {
            sb.append(" - " ).append(command).append("\n");
        }
        return sb.toString();
    }
}
