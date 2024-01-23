package com.advertisingpost.bot.service.messaging;

import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
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
public class PostPreviewAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        String text;
        String[] textLink;
        String nameButton;
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        text = textCreatePost.get(0);
        String link;
        URL url = null;
        //if(textCreatePost.size() == 2){
            textLink = textCreatePost.get(1).split(":");
            nameButton = textLink[0].trim();
            link = textLink[1].trim();
        //}
        if(textCreatePost.size() == 3){
            link = textCreatePost.get(1);
            url = new URI(textCreatePost.get(2)).toURL();
        }
        String callbackName = nameButton+"||";
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
        URL url = null;
        if(textCreatePost.size() == 3) {
            text = textCreatePost.get(0);
            textLink = textCreatePost.get(2).split(":");
            url = new URI(textCreatePost.get(1)).toURL();
        } else if(textCreatePost.size() == 2){
            textLink = textCreatePost.get(1).split(":");
            url = new URI(textCreatePost.get(0)).toURL();
            text = "";
        }
        String textButton = textLink[0].trim();
        String link = textLink[1].trim();
        return inputData.photoTransmission(chatId,text, textButton, textButton, link, url);
    }
}
