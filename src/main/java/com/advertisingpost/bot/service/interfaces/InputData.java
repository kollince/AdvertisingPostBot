package com.advertisingpost.bot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface InputData {
    SendMessage transmission(String chatId, String text, String nameButton, String callbackName);
}
