package com.advertisingpost.bot.service.processing.interfaces;

import com.advertisingpost.bot.service.messaging.interfaces.Action;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Map;

public interface PreparingMessages {
    SendPhoto sendingPhoto(Update update, String key, Map<String, Action> map, long chatId,
                           ArrayList<String> arrayList, MapAction mapAction);

    SendMessage sendingMessage(Update update, String key, Map<String, Action> map, long chatId,
                               ArrayList<String> readMessage, MapAction mapAction);

    SendMessage collectingMessages(Update update, Map<String, Action> map, long chatId, MapAction mapAction, ProcessingUsersMessages processingUsersMessages, String token, String sendFile);
}
