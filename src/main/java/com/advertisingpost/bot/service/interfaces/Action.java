package com.advertisingpost.bot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Action {
    SendMessage handle(Update update);

    BotApiMethod callback(Update update);
}
