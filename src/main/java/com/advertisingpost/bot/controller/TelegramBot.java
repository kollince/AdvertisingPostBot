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
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
//        Message channel = update.getChannelPost();
//        SendMessage message = new SendMessage();
//        message.setText("test");
//        message.setChatId("-1001282898271");
//        message.setChatId("@cryptafanat");
//        log.debug(message);
//        log.debug(update.getMessage().getForwardFromChat());
//        log.debug(update.getMessage().getChatId());

//        sendChannel(message);
        mapAction.generalMapPut(inputData);
        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            if(mapAction.generalMapRead().containsKey(key)){
                mapContainsKey(update,key,mapAction.generalMapRead(), chatId);
            } else if (mapAction.bindingByRead().containsKey(chatId)) {
//                log.debug(mapAction.bindingByRead().get(chatId));
                try {
                    notMapContainsKey(update, mapAction.generalMapRead(), chatId);
                    log.debug(processingUsersMessages.readMessage());
                } catch (TelegramApiException e) {
                    log.debug(e);
                }
            }
            if (update.getMessage().hasText() && update.getMessage().getText().equals("/start")){
                processingUsersMessages.clearArrayList();
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
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
    private void sendChannel(SendMessage msg) {
        try {
            execute(msg);
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
    private void sendPhotoChanel(SendPhoto msg) {
        try {
//            msg.setChatId(channelChatId);
            execute(msg);
        } catch (TelegramApiException e) {
            log.debug(e);
        }
    }
    private void sendVideo(SendVideo msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.debug(e);
        }
    }
    private void sendAnimation(SendAnimation msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.debug(e);
        }
    }
    private File sendFile(Update update) throws TelegramApiException {
        GetFile getFile = new GetFile();
        if (update.getMessage().hasPhoto()) {
            int el = update.getMessage().getPhoto().size() - 1;
            getFile.setFileId(update.getMessage().getPhoto().get(el).getFileId());
        } else if (update.getMessage().hasVideo()) {
            getFile.setFileId(update.getMessage().getVideo().getFileId());
        } else if (update.getMessage().hasAnimation()) {
            getFile.setFileId(update.getMessage().getAnimation().getFileId());
        }
        return  execute(getFile);
    }
    private void mapContainsKey(Update update, String key, Map<String, Action> map, long chatId){

        if (update.getMessage().hasText()){
            send(preparingMessages.sendingMessage(update, key, map, chatId,
                    processingUsersMessages.readMessage(), mapAction));
        }
    }
    private void notMapContainsKey(Update update, Map<String, Action> map, long chatId) throws TelegramApiException {
        //Отправка сообщения и фото пользователю
        if (update.getMessage().hasPhoto() || update.getMessage().hasVideo() || update.getMessage().hasAnimation()) {
            send(preparingMessages.collectingMessagesMedia(update, map, chatId, mapAction, processingUsersMessages, token, sendFile(update).getFilePath()));
            log.debug("1");
        } else if (update.getMessage().hasText()){
            send(preparingMessages.collectingMessages(update, map, chatId, mapAction, processingUsersMessages, token));
            log.debug("2");
        } else {
            log.debug("3");
            send(preparingMessages.sendCallbackData(update,map, processingUsersMessages.readMessage(),mapAction,chatId, StringDataMessage.CREATE_IMAGE.getMessage()));
        }
    }
    private void mapContainsKeyCallbackData(Update update, Map<String, Action> map, long chatId, String callbackData){
        if (callbackData.equals(StringDataMessage.CREATE_ONLY_TEXT.getMessage())){
            send(preparingMessages.sendCallbackData(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
        } else if (callbackData.equals(StringDataMessage.CREATE_PREVIEW.getMessage()) || callbackData.equals(StringDataMessage.CREATE_POST.getMessage())){
            log.debug(callbackData);
            //TODO Сократить (завести отдельную булевую переменную) условие callbackData.equals(StringDataMessage.CREATE_POST.getMessage())
            String location = "";
            if (!callbackData.equals(StringDataMessage.CREATE_POST.getMessage()) && processingUsersMessages.readMessage().size() == 3){
                log.debug(processingUsersMessages.readMessage());
                location = processingUsersMessages.readMessage().get(1);
                log.debug(location);
            } else if (!callbackData.equals(StringDataMessage.CREATE_POST.getMessage()) && processingUsersMessages.readMessage().size() == 2){
                log.debug(processingUsersMessages.readMessage());
                log.debug(location);
                location = processingUsersMessages.readMessage().get(0);
            } else if (callbackData.equals(StringDataMessage.CREATE_POST.getMessage()) && processingUsersMessages.readMessage().size() == 4) {
                log.debug(processingUsersMessages.readMessage());
                location = processingUsersMessages.readMessage().get(1);
                log.debug(location);
            } else if (callbackData.equals(StringDataMessage.CREATE_POST.getMessage()) && processingUsersMessages.readMessage().size() == 3) {
                log.debug(processingUsersMessages.readMessage());
                location = processingUsersMessages.readMessage().get(0);
                log.debug(location);
            }
            if (!isUrlHttp(location)){
                send(preparingMessages.sendCallbackData(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
            } else {
                log.debug(processingUsersMessages.readMessage());
                    String extensionFiles = "";
                    try {
                        URL url = new URI("http://localhost").toURL();
                        log.debug(processingUsersMessages.readMessage().size());
                        //TODO Сократить (завести отдельную булевую переменную) условие callbackData.equals(StringDataMessage.CREATE_POST.getMessage())
                        if (processingUsersMessages.readMessage().size() == 2) {
                            url = new URI(processingUsersMessages.readMessage().get(0)).toURL();
                        } else if (processingUsersMessages.readMessage().size() == 3 && !callbackData.equals(StringDataMessage.CREATE_POST.getMessage())) {
                            url = new URI(processingUsersMessages.readMessage().get(1)).toURL();
                        } else if (processingUsersMessages.readMessage().size() == 3 && callbackData.equals(StringDataMessage.CREATE_POST.getMessage())) {
                            url = new URI(processingUsersMessages.readMessage().get(0)).toURL();
                        } else if (processingUsersMessages.readMessage().size() == 4 && callbackData.equals(StringDataMessage.CREATE_POST.getMessage())) {
                            url = new URI(processingUsersMessages.readMessage().get(1)).toURL();
                        }
                        extensionFiles = url.getPath().substring(url.getPath().lastIndexOf('.')+1);
                    } catch (Exception e){
                        log.debug(e);
                    }
//                if (callbackData.equals(StringDataMessage.CREATE_POST.getMessage())){
//                    chatId = Long.parseLong(processingUsersMessages.readMessage().get(processingUsersMessages.readMessage().size()-1));
//                }
                String channelChatId = processingUsersMessages.readMessage().get(processingUsersMessages.readMessage().size()-1);
                if (extensionFiles.equals("mp4")) {
                    sendVideo(preparingMessages.sendCallbackDataVideo(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
                } else if (extensionFiles.equals("jpg")) {
                    if (callbackData.equals(StringDataMessage.CREATE_POST.getMessage())){
                        SendPhoto msgPhoto = preparingMessages.sendCallbackDataPhoto(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData);
                        msgPhoto.setChatId(channelChatId);
                        sendPhotoChanel(msgPhoto);
                    } else {
                        sendPhoto(preparingMessages.sendCallbackDataPhoto(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
                    }

                } else if (extensionFiles.equals("gif")) {
                    sendAnimation(preparingMessages.sendCallbackDataAnimation(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
                } else {
                    String key = update.getMessage().getText();
                    log.debug(key);
                    send(preparingMessages.sendingMessage(update, key, map, chatId,
                            processingUsersMessages.readMessage(), mapAction));
                }
            }
        } else {
            log.debug(callbackData);
            send(preparingMessages.sendCallbackData(update, map, processingUsersMessages.readMessage(), mapAction, chatId, callbackData));
        }
    }
    private boolean isUrlHttp(String location) {
        return location != null && location.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

}