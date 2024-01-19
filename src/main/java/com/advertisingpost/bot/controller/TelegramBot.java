package com.advertisingpost.bot.controller;

import com.advertisingpost.bot.config.BotConfig;
import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.processing.interfaces.MapAction;
import com.advertisingpost.bot.service.processing.interfaces.PreparingMessages;
import com.advertisingpost.bot.service.processing.interfaces.ProcessingUsersMessages;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    //@Autowired
    private final InputData inputData;
    //@Autowired
    private final ProcessingUsersMessages processingUsersMessages;
    //@Autowired
    private final MapAction mapAction;
    private final PreparingMessages preparingMessages;
    @Value("${bot.token}")
    String token;
    final BotConfig config;
    public TelegramBot(@Value("${bot.token}") String token, InputData inputData, BotConfig config,
                       ProcessingUsersMessages processingUsersMessages, MapAction mapAction, PreparingMessages preparingMessages){
        super(token);
        this.inputData = inputData;
        this.config = config;
        this.processingUsersMessages = processingUsersMessages;
        this.mapAction = mapAction;
        this.preparingMessages = preparingMessages;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId;
        mapAction.generalMapPut(inputData);
        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            if(mapAction.generalMapRead().containsKey(key)){
                mapContainsKey(update,key,mapAction.generalMapRead(), chatId);
            } else if (mapAction.bindingByRead().containsKey(chatId)) {
                try {
                    notMapContainsKey(update, key, mapAction.generalMapRead(), chatId);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if (update.getMessage().hasText() && update.getMessage().getText().equals("/start")){
                processingUsersMessages.clearArrayList();
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            log.debug(callbackData);
            chatId = update.getCallbackQuery().getMessage().getChatId();
            if (mapAction.generalMapRead().containsKey(callbackData)){
                mapContainsKeyCallbackData(update, mapAction.generalMapRead(), chatId , callbackData);
                AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                        .callbackQueryId(update.getCallbackQuery().getId()).build();
                answerCallbackQuery(close);
            } else if (mapAction.bindingByRead().containsKey(chatId)) {
                SendMessage msg = new SendMessage();
                try {
                    msg = mapAction.generalMapRead().get(mapAction.bindingByRead().get(chatId)).callback(update);
                } catch (MalformedURLException | URISyntaxException e) {
                     log.debug(e);
                }
                mapAction.bindingByRemove(chatId);
                send(msg);
            }
        }
    }
    private void answerCallbackQuery(AnswerCallbackQuery answer){
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            log.debug(e);
        }

    }
    private void send(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.debug(e);
        }
    }
    private void sendPhoto(SendPhoto msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.debug(e);
        }
    }
    private File sendFile(Update update) throws TelegramApiException {
        GetFile getFile = new GetFile();
        int el = update.getMessage().getPhoto().size() - 1;
        getFile.setFileId(update.getMessage().getPhoto().get(el).getFileId());
        return  execute(getFile);
    }
    private void mapContainsKey(Update update, String key, Map<String, Action> map, long chatId){

        if (update.getMessage().hasText()){
            send(preparingMessages.sendingMessage(update, key, map, chatId,
                    processingUsersMessages.readMessage(), mapAction));
        }
    }
    private void notMapContainsKey(Update update, String key, Map<String, Action> map, long chatId) throws TelegramApiException {
        //Отправка сообщения и фото пользователю
        if (update.getMessage().hasPhoto()) {
            send(preparingMessages.collectingMessagesPhoto(update, map, chatId, mapAction, processingUsersMessages, token, sendFile(update).getFilePath()));
        } else {
            send(preparingMessages.collectingMessages(update, map, chatId, mapAction, processingUsersMessages, token));
        }
    }
    private void mapContainsKeyCallbackData(Update update, Map<String, Action> map, long chatId, String callbackData){
        if (callbackData.equals(StringDataMessage.CREATE_ONLY_TEXT.getMessage())){
            send(preparingMessages.sendCallbackData(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
        } else if (callbackData.equals(StringDataMessage.CREATE_PREVIEW.getMessage())){
            sendPhoto(preparingMessages.sendCallbackDataPhoto(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
        } else if (callbackData.equals(StringDataMessage.CREATE_PREVIEW_TEXT.getMessage())) {
            send(preparingMessages.sendCallbackData(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
        } else {
            send(preparingMessages.sendCallbackData(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
        }
    }

}