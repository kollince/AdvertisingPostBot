package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Log4j
@AllArgsConstructor
@Component
public class PostAddLinkAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handle(Update update, ArrayList<String> textCreatePost) {
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        var text = "Добавьте ссылку: ";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        String chatId;
        String messageText;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
            messageText = update.getMessage().getText();
        }else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            messageText = update.getCallbackQuery().getMessage().getText();
        }
        String nameButton = "Открыть рекламный пост";
        String callbackName = "CREATE_PREVIEW";
        var text = "Ссылка " + messageText + " добавлена, выполните команду: /postpreview";
        return inputData.transmission(chatId, text, nameButton, callbackName, null);
    }

}
