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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
@Log4j
@Component
@AllArgsConstructor
public class InfoAction implements Action {
    private final List<String> actions;
    private InputData inputData;
//    public InfoAction(List<String> actions) {
//        this.actions = actions;
//    }
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var out = new StringBuilder();
        out.append("Выберите действие:").append("\n");
        for (String action : actions) {
            out.append(action).append("\n");
        }
        String nameButton = "Приступить к созданию";
        String callbackName = "CREATE_HEADER";
        var text = "Этот бот создает рекламные посты.\n"+out;
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public BotApiMethod callback(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var headerText = update.getMessage().getText();
        String nameButton = "Перейти к вводу текста";
        String callbackName = "CREATE_BODY";
        var text = "Заголовок " + headerText + " добавлен, выполните команду: /postbody ";
        //transmission(chatId, text, nameButton, callbackName);
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        return null;
    }


}
