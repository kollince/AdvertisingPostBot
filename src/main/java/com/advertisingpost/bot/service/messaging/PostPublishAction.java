package com.advertisingpost.bot.service.messaging;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

@Log4j
@AllArgsConstructor
@Component
public class PostPublishAction implements Action {
    private InputData inputData;
    private String[] getData(Update update, ArrayList<String> textCreatePost, boolean isTextAttach) {
        String text = "" ;
        String chatId;
        String[] textLink = new String[0];
        String url = null;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        try {
            if (textCreatePost.size() == 3) {
                textLink = textCreatePost.get(1).split(":");
                if (isTextAttach){
                    text = textCreatePost.get(0);
                } else {
                    url = textCreatePost.get(0);
                }
            }  else if (textCreatePost.size() == 4) {
                text = textCreatePost.get(0);
                textLink = textCreatePost.get(2).split(":");
                url = textCreatePost.get(1);
            }
        } catch (Exception e) {
            log.debug(e);
        }
        for (int i = 0; i < textCreatePost.size(); i++) {
            log.debug(textCreatePost.get(i));
        }
        String nameButton = textLink[0].trim();
        //TODO добавить новое action и исправить StringDataMessage.CREATE_POST.getMessage() на VIEW_POST
        String callbackName = StringDataMessage.VIEW_POST.getMessage();
        String link = textLink[1].trim();
        return new String[] {chatId, text, nameButton, callbackName, link, url};
    }
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = true;
        String[] data = getData(update, textCreatePost, isTextAttach);
        URL url = null;
        try {
            if (data[5] != null) url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        String nameButtonPublished = StringDataMessage.CREATE_POST_CHANNEL.getMessage();
        String textPublished = StringDataMessage.POST_PUBLISHED_CHANNEL.getMessage();
        String linkPublished = "http://t.me/"+textCreatePost.get(textCreatePost.size()-1);
        String text = data[1];
        String nameButton = data[2];
        String link = data[4];
        if (isPublished){
            text = textPublished;
            nameButton = nameButtonPublished;
            link = linkPublished;
        }
        log.debug(textCreatePost.get(textCreatePost.size()-1));
        log.debug(textCreatePost.size());
        return inputData.transmission(data[0], text, nameButton, data[3], link, url);
//        return inputData.transmission(data[0], data[1], nameButton, data[3], data[4], url);
    }

    @Override
    public SendMessage callback(Update update) {
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        }else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        String nameButton = StringDataMessage.POST_ADD_LINK_ACTION_OPEN_ADV_POST.getMessage();
        String callbackName = StringDataMessage.CREATE_ONLY_TEXT.getMessage();
        var text = StringDataMessage.CREATE_ADD_CHANNEL.getMessage();
        log.debug(callbackName);
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = false;
        URL url = null;
        String[] data = getData(update, textCreatePost, isTextAttach);
        try {
            url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        String nameButtonPublished = StringDataMessage.CREATE_POST_CHANNEL.getMessage();
        String textPublished = StringDataMessage.POST_PUBLISHED_CHANNEL.getMessage();
        String linkPublished = "http://t.me/"+textCreatePost.get(textCreatePost.size()-1);
        String text = data[1];
        String nameButton = data[2];
        String link = data[4];
        if (isPublished){
            text = textPublished;
            nameButton = nameButtonPublished;
            link = linkPublished;
        }
        log.debug("handlePhoto");
        return inputData.photoTransmission(data[0], text, nameButton, data[3], link, url);
    }
    @Override
    public SendVideo handleVideo(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = false;
        URL url = null;
        String[] data = getData(update, textCreatePost, isTextAttach);
        try {
            url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        return inputData.videoTransmission(data[0], data[1], data[2], data[3], data[4], url);
    }
    @Override
    public SendAnimation handleAnimation(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = false;
        URL url = null;
        String[] data = getData(update, textCreatePost, isTextAttach);
        try {
            url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        return inputData.animationTransmission(data[0], data[1], data[2], data[3], data[4], url);
    }
}
