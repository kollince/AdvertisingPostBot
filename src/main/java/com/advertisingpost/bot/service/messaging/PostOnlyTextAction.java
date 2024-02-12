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
import java.net.URISyntaxException;
import java.util.ArrayList;

@Log4j
@Component
@AllArgsConstructor
public class PostOnlyTextAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        var text = StringDataMessage.POST_ONLY_TEXT_ACTION_ENTER_ADV_TEXT.getMessage();
        return new SendMessage(chatId, text);
    }

    @Override
    public SendMessage callback(Update update) {
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        }else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        String nameButton = StringDataMessage.POST_IMAGE_ACTION_ADD_LINK_BUTTON.getMessage();
        //CREATE_ADD_LINK
        String callbackName = StringDataMessage.CREATE_ADD_LINK.getMessage();
        var text = StringDataMessage.POST_ONLY_TEXT_ACTION_TEXT_ADDED.getMessage();
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        return null;
    }

    @Override
    public SendVideo handleVideo(Update update, ArrayList<String> textCreatePost, boolean isPublished) throws MalformedURLException, URISyntaxException {
        return null;
    }

    @Override
    public SendAnimation handleAnimation(Update update, ArrayList<String> textCreatePost, boolean isPublished) throws MalformedURLException, URISyntaxException {
        return null;
    }

}
