package com.advertisingpost.bot.service.buttonsUsers.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

import java.net.URL;

public interface InputData {
    SendMessage transmission(String chatId, String text, String nameButton, String callbackName, String link, URL url);
    SendPhoto photoTransmission(String chatId, String text, String nameButton, String callbackName, String link, URL url);
    SendVideo videoTransmission(String chatId, String text, String nameButton, String callbackName, String link, URL url);
}
