package com.advertisingpost.bot.service.messaging;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

@Log4j
@AllArgsConstructor
@Component
public class PostPreviewTextAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        String text = "" ;
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        text = textCreatePost.get(0)+"\n"+textCreatePost.get(1);
        String link = textCreatePost.get(3);
        URL url = new URI(textCreatePost.get(2)).toURL();
        log.debug(text);
        String nameButton = StringDataMessage.POST_PREVIEW_ACTION_LINK_BUTTON.getMessage();
        String callbackName = StringDataMessage.POST_PREVIEW_ACTION_LINK_BUTTON.getMessage();
        return inputData.transmission(chatId,text, nameButton, callbackName, link, url);
    }
    @Override
    public SendMessage callback(Update update) throws MalformedURLException, URISyntaxException {
        return handleText(update,null);
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        String text = "" ;
        String chatId;
        String[] textLink = new String[0];
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
//        text = textCreatePost.get(0)+"\n"+textCreatePost.get(1);
        log.debug(textCreatePost);
        text = textCreatePost.get(0);
        log.debug(textCreatePost.size());
        if(textCreatePost.size() == 3) {
            textLink = textCreatePost.get(2).split(":");
        } else if(textCreatePost.size() == 2){
            textLink = textCreatePost.get(1).split(":");
        }
        String textButton = textLink[0].trim();
        String link = textLink[1].trim();
        URL url = new URI(textCreatePost.get(1)).toURL();
//        String nameButton = StringDataMessage.POST_PREVIEW_ACTION_LINK_BUTTON.getMessage();
//        String callbackName = StringDataMessage.POST_PREVIEW_ACTION_LINK_BUTTON.getMessage();
        String nameButton = textButton;
        String callbackName = textButton;
        return inputData.photoTransmission(chatId,text, nameButton, callbackName, link, url);
    }
}
