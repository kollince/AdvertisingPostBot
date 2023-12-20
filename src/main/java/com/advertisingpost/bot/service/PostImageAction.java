package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.Action;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PostImageAction implements Action {
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Загрузите изображение: ";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var headerText = msg.getText();
        // userRepository.save(new User(email));
        var text = "Изображение " + headerText + " добавлено, выполните команду: /postaddlink";
        return new SendMessage(chatId, text);
    }
}
