package com.advertisingpost.bot.controller;

import com.advertisingpost.bot.service.InfoAction;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j
@RequiredArgsConstructor
@Component
public class UpdatingBot {
    private final String createPost = "Создать пост";
    private final String about = "О нас";
    public ReplyKeyboardMarkup keyBoard (){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(createPost);
        row.add(about);
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }
    public InlineKeyboardMarkup createButtons(){
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var linkButton = new InlineKeyboardButton();
        linkButton.setText("Подробнее");
        linkButton.setCallbackData("LINK_BUTTON");
        linkButton.setUrl("https://ya.ru");
        rowInline.add(linkButton);
        rowsInline.add(rowInline);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);
        return markup;
    }


}
