package com.advertisingpost.bot.service.messaging;

import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Log4j
@AllArgsConstructor
@Component
public class PostAddLinkAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) {
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        var text = StringDataMessage.POST_ADD_LINK_ACTION_ADD_LINK.getMessage();
        return new SendMessage(chatId, text);
    }

    @Override
    public SendMessage callback(Update update) {
        String chatId;
        String messageText;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
            messageText = update.getMessage().getText();
        }else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            messageText = update.getCallbackQuery().getMessage().getText();
        }
        String nameButton = StringDataMessage.POST_ADD_LINK_ACTION_OPEN_ADV_POST.getMessage();
        String callbackName = "CREATE_PREVIEW";
        var text = StringDataMessage.POST_ADD_LINK_ACTION_IMAGE_ADDED.getMessage();
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost){
        return null;
    }


}