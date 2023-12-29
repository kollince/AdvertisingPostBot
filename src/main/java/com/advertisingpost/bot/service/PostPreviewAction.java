package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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
        String nameButton = "Перейти";
        String callbackName = "Перейти";
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
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        text = textCreatePost.get(0)+"\n"+textCreatePost.get(1);
        String link = textCreatePost.get(3);
        URL url = new URI(textCreatePost.get(2)).toURL();
        String nameButton = "Перейти";
        String callbackName = "Перейти";
        return inputData.photoTransmission(chatId,text, nameButton, callbackName, link, url);
    }
}
