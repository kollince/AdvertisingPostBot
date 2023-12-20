package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.Action;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class PostAddLinkAction implements Action {
    @Override
    public SendMessage handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Добавьте ссылку: ";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var headerText = msg.getText();
        // userRepository.save(new User(email));
        var text = "Ссылка " + headerText + " добавлена, выполните команду: /postpreview";
        return new SendMessage(chatId, text);
    }
}
