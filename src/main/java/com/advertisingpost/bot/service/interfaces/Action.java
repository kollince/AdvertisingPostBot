package com.advertisingpost.bot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public interface Action {
    SendMessage handle(Update update, ArrayList<String> textCreatePost);

    BotApiMethod callback(Update update);
}
