package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
@Log4j
@Component
@AllArgsConstructor
public class InfoAction implements Action {
    private InputData inputData;

    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        String out = StringDataMessage.INFO_ACTION_SELECT_ACTION.getMessage() + "\n" +
                StringDataMessage.INFO_ACTION_HELP.getMessage();
        String nameButton = StringDataMessage.COMMON_INPUT_TEXT_BUTTON.getMessage();
        //String callbackName = "CREATE_HEADER";
        String callbackName = "CREATE_BODY";
        var text = StringDataMessage.INFO_ACTION_CREATE_ADV_POST.getMessage()+"\n"+ out;
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendMessage callback(Update update) {
        AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                .callbackQueryId(update.getCallbackQuery().getId()).build();
//        var chatId = update.getMessage().getChatId().toString();
//        var headerText = update.getMessage().getText();
//        String nameButton = StringDataMessage.INFO_ACTION_INPUT_TEXT.getMessage();
//        String callbackName = "CREATE_BODY";
//        var text = "Заголовок " + headerText + " добавлен, выполните команду: /postbody ";
        //String text = StringDataMessage.INFO_ACTION_HEADER_ADDED.getMessage();
        //transmission(chatId, text, nameButton, callbackName);
        //return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
        return null;
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        return null;
    }


}
