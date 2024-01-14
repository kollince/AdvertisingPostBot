package com.advertisingpost.bot.service.processing;

import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.processing.interfaces.MapAction;
import com.advertisingpost.bot.service.processing.interfaces.PreparingMessages;
import com.advertisingpost.bot.service.processing.interfaces.ProcessingUsersMessages;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
@Component
@Log4j
public class PreparingMessagesImpl implements PreparingMessages {
    @Override
    public SendPhoto sendingPhoto(Update update, String key, Map<String, Action> map, long chatId, ArrayList<String> arrayList, MapAction mapAction) {
        SendPhoto msg = new SendPhoto();
//        try {
//            msg = map.get(key).handlePhoto(update, arrayList);
//        } catch (MalformedURLException | URISyntaxException e) {
//            log.debug(e);
//        }
//        mapAction.bindingByPut(chatId, key);
//        log.debug("sendingPhoto");
        return msg;
    }

    @Override
    public SendMessage sendingMessage(Update update, String key, Map<String, Action> map, long chatId,
                                      ArrayList<String> readMessage, MapAction mapAction) {
        SendMessage msg = new SendMessage();
        try {
            msg = map.get(key).handleText(update, readMessage);
        } catch (MalformedURLException | URISyntaxException e) {
            log.debug(e);
        }
        mapAction.bindingByPut(chatId, key);
        log.debug(msg.getText());
        return msg;
    }
 //TODO
    @Override
    public SendMessage collectingMessages(Update update, Map<String, Action> map, long chatId, MapAction mapAction, ProcessingUsersMessages processingUsersMessages, String token, String sendFile) {
        SendMessage msg = new SendMessage();
        try {
            msg = map.get(mapAction.bindingByRead().get(chatId)).callback(update);
            log.debug("notMapContainsKey"+map.get(mapAction.bindingByRead().get(chatId)).callback(update));
        } catch (MalformedURLException | URISyntaxException e) {
            log.debug(e);
        }
        if (update.getMessage().hasPhoto()){
            log.debug(update.getMessage().getPhoto());
            try {
                URL url = new URI("https://api.telegram.org/file/bot" + token + "/" + sendFile).toURL();
                processingUsersMessages.addAPathImage(String.valueOf(url));
                log.debug(url);
            } catch (IOException | URISyntaxException e) {
                log.debug(e);
            }
        } else {
            processingUsersMessages.addArticle(update.getMessage().getText());
        }
        log.debug("до "+mapAction);
        mapAction.bindingByRemove(chatId);
        log.debug("после "+mapAction);
        return msg;
    }
}
