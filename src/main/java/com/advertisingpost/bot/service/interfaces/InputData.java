package com.advertisingpost.bot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.net.URL;

public interface InputData {
    SendMessage transmission(String chatId, String text, String nameButton, String callbackName, String link, URL url);
    SendPhoto photoTransmission(String chatId, String text, String nameButton, String callbackName, String link, URL url);
}
