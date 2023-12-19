package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.InputData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
@Component
public class InputDataImpl implements InputData {
    @Override
    public SendMessage transmission(String chatId, String text, String nameButton, String callbackName) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var linkButton = new InlineKeyboardButton();
        linkButton.setText(nameButton);
        linkButton.setCallbackData(callbackName);
        //linkButton.setUrl("/postbody");
        rowInline.add(linkButton);
        rowsInline.add(rowInline);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);
        // userRepository.save(new User(email));
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(markup);
        return message;
    }
}
