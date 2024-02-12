package com.advertisingpost.bot.service.processing;

import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.processing.interfaces.MapAction;
import com.advertisingpost.bot.service.processing.interfaces.ModeParsing;
import com.advertisingpost.bot.service.processing.interfaces.PreparingMessages;
import com.advertisingpost.bot.service.processing.interfaces.ProcessingUsersMessages;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
@AllArgsConstructor
@Component
@Log4j
public class PreparingMessagesImpl implements PreparingMessages {
    private final ModeParsing modeParsing;
    @Override
    public SendMessage sendingMessage(Update update, String key, Map<String, Action> map, long chatId,
                                      ArrayList<String> readMessage, MapAction mapAction) {
        SendMessage msg = new SendMessage();
        try {
            msg = map.get(key).handleText(update, readMessage, false);
        } catch (MalformedURLException | URISyntaxException e) {
            log.debug(e);
        }
        mapAction.bindingByPut(chatId, key);
        return msg;
    }
    @Override
    public SendMessage collectingMessagesMedia(Update update, Map<String, Action> map, long chatId, MapAction mapAction, ProcessingUsersMessages processingUsersMessages, String token, String sendFile) {
        SendMessage msg = new SendMessage();
        try {
            msg = map.get(mapAction.bindingByRead().get(chatId)).callback(update);
            log.debug(msg);
        } catch (MalformedURLException | URISyntaxException e) {
            log.debug(e);
        }
        if (update.getMessage().hasPhoto() || update.getMessage().hasVideo() || update.getMessage().hasAnimation()) {
            try {
                URL url = new URI("https://api.telegram.org/file/bot" + token + "/" + sendFile).toURL();
                processingUsersMessages.addAPathImage(String.valueOf(url));
            } catch (IOException | URISyntaxException e) {
                log.debug(e);
            }
        } else if (!update.getMessage().hasPhoto() && !update.getMessage().hasVideo() && !update.getMessage().hasAnimation()) {
            try {
                log.debug("Неверный формат файла");
            } catch (Exception e){
                log.debug(e);
            }


    } else {
            processingUsersMessages.addArticle(update.getMessage().getText());
        }
        mapAction.bindingByRemove(chatId);
        return msg;
    }
    @Override
    public SendMessage collectingMessages(Update update, Map<String, Action> map, long chatId, MapAction mapAction, ProcessingUsersMessages processingUsersMessages, String token) {
        SendMessage msg = new SendMessage();
        try {
            msg = map.get(mapAction.bindingByRead().get(chatId)).callback(update);
        } catch (MalformedURLException | URISyntaxException e) {
            log.debug(e);
        }
        processingUsersMessages.addArticle(update.getMessage().getText());
        mapAction.bindingByRemove(chatId);
        return msg;
    }

    @Override
    public SendPhoto sendCallbackDataPhoto(Update update, Map<String, Action> map, ArrayList<String> readMessage,
                                           MapAction mapAction, long chatId, String callbackData, boolean isPublished) {
        SendPhoto msg = new SendPhoto();
        try {
            msg = map.get(callbackData).handlePhoto(update, readMessage, false);
        } catch (MalformedURLException | URISyntaxException e){
            log.debug(e);
        }
        mapAction.bindingByPut(chatId, callbackData);
        return msg;
    }

    @Override
    public SendVideo sendCallbackDataVideo(Update update, Map<String, Action> map, ArrayList<String> readMessage,
                                           MapAction mapAction, long chatId, String callbackData, boolean isPublished) {
        SendVideo msg = new SendVideo();
        try {
            msg = map.get(callbackData).handleVideo(update, readMessage, false);
        } catch (MalformedURLException | URISyntaxException e){
            log.debug(e);
        }
        mapAction.bindingByPut(chatId, callbackData);
        return msg;
    }
    @Override
    public SendAnimation sendCallbackDataAnimation(Update update, Map<String, Action> map, ArrayList<String> readMessage,
                                                   MapAction mapAction, long chatId, String callbackData, boolean isPublished) {
        SendAnimation msg = new SendAnimation();
        try {
            msg = map.get(callbackData).handleAnimation(update, readMessage, false);
        } catch (MalformedURLException | URISyntaxException e){
            log.debug(e);
        }
        mapAction.bindingByPut(chatId, callbackData);
        return msg;
    }

    //Загрузите изображение
    @Override
    public SendMessage sendCallbackData(Update update, Map<String, Action> map, ArrayList<String> readMessage,
                                        MapAction mapAction, long chatId, String callbackData, boolean isPublished) {
        SendMessage msg = new SendMessage();
        try {
            msg = map.get(callbackData).handleText(update, readMessage, isPublished);
            modeParsing.ParsingMessage(msg);
        } catch (MalformedURLException | URISyntaxException e) {
            log.debug(e);
        }
        mapAction.bindingByPut(chatId, callbackData);
        return msg;
    }
}
