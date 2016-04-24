package com.aolisov.EDI.mvc;

import com.aolisov.EDI.dto.Message;
import com.aolisov.EDI.dto.ResponseMessage;
import com.aolisov.EDI.dto.UpdateFromTelegram;
import com.aolisov.EDI.processor.ResponseProcessor;
import com.aolisov.EDI.properties.PropertiesHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
public class BotController {

    private final Logger logger = LoggerFactory.getLogger(BotController.class);

    @Autowired
    ResponseProcessor responseProcessor;

    @Autowired
    PropertiesHandler propertiesHandler;

    @RequestMapping("ping")
    @ResponseBody
    public String ping() {
        return "Hi!";
    }

    @RequestMapping(value = "/message{token}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage respond(@RequestBody String requestString,
                                   @PathVariable("token") String token) {

        try {
            if(!token.equals(propertiesHandler.getBotToken())) {
                logger.warn(
                        "Somebody tried to use your bot with wrong token! Token: {}; request string: {}",
                        token,
                        requestString
                );
                return new ResponseMessage();
            }

            logger.info("New request: {}", requestString);

            UpdateFromTelegram update = new ObjectMapper().readValue(requestString, UpdateFromTelegram.class);
            Message requestMessage = update.getMessage();

            String responseMessage = responseProcessor.getResponse(requestMessage);
            ResponseMessage responseObject = new ResponseMessage();
            responseObject.setChatId(requestMessage.getChat().getId());
            responseObject.setText(responseMessage);
            sendResponse(responseObject);

            return responseObject;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseMessage();
        }
    }

    private void sendResponse(ResponseMessage responseObject) {
        HttpPost post = new HttpPost(String.format(
                propertiesHandler.getTelegramApiFormatUrl(),
                propertiesHandler.getBotToken())
        );
        try {
            StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(responseObject));
            entity.setContentType("application/json;charset=UTF-8");
            post.setEntity(entity);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != 200) {
                logger.error("Couldn't send message to Telegram! status code - {}; message - {}", statusCode, responseObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error with sending response to Telegram!");
        }
    }
}
