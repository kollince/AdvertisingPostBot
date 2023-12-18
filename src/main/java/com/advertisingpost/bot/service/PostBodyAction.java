package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.Action;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PostBodyAction implements Action {
    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Введите рекламный текст: ";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var headerText = msg.getText();
        // userRepository.save(new User(email));
        var text = headerText + " добавлен, выполните команду: /postimage: ";
        return new SendMessage(chatId, text);
    }
}
