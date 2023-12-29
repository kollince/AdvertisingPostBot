package com.advertisingpost.bot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public interface Action {
    SendMessage handleText(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException;

    SendMessage callback(Update update) throws MalformedURLException, URISyntaxException;

    SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException;
}
