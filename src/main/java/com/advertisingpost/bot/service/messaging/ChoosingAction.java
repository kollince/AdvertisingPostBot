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
import java.net.URISyntaxException;
import java.util.ArrayList;
@Log4j
@Component
@AllArgsConstructor
public class ChoosingAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        String out = StringDataMessage.CHOOSE_ACTION_BUTTON.getMessage();
        String nameButton = StringDataMessage.COMMON_INPUT_TEXT_BUTTON.getMessage();
        String nameButton1 = StringDataMessage.COMMON_INPUT_TEXT_BUTTON.getMessage();
        String nameButton2 = StringDataMessage.COMMON_INPUT_TEXT_BUTTON.getMessage();
        //String callbackName = "CREATE_HEADER";
        String callbackName = "CREATE_BODY";
        var text = StringDataMessage.INFO_ACTION_CREATE_ADV_POST.getMessage()+"\n"+ out;
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendMessage callback(Update update) throws MalformedURLException, URISyntaxException {
        return null;
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        return null;
    }
}
