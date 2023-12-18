package com.advertisingpost.bot.service;

import com.advertisingpost.bot.controller.UpdatingBot;
import com.advertisingpost.bot.service.interfaces.Action;
import lombok.extern.log4j.Log4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class PostHeaderAction implements Action {
    private UpdatingBot updatingBot;
    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var text = "Введите заголовок для нового поста:";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        var headerText = msg.getText();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var linkButton = new InlineKeyboardButton();
        linkButton.setText("Перейти к заполнению текста");
        linkButton.setCallbackData("LINK_BUTTON");
        linkButton.setUrl("/postbody");
        rowInline.add(linkButton);
        rowsInline.add(rowInline);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);
        // userRepository.save(new User(email));
        var text = "Заголовок " + headerText + " добавлен, выполните команду: /postbody ";
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(markup);
        return message;
    }


}
