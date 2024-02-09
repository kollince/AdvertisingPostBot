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
import java.util.ArrayList;
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

    private final String CREATE_POST = StringDataMessage.CREATE_POST.getMessage();
    private final String CREATE_PREVIEW = StringDataMessage.CREATE_PREVIEW.getMessage();
    private final String CREATE_ONLY_TEXT = StringDataMessage.CREATE_ONLY_TEXT.getMessage();
    private final String VIEW_POST = StringDataMessage.VIEW_POST.getMessage();

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
                    notMapContainsKey(update, mapAction.generalMapRead(), chatId);
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
        } else if (update.getMessage().hasText()){
            send(preparingMessages.collectingMessages(update, map, chatId, mapAction, processingUsersMessages, token));
        } else {
            send(preparingMessages.sendCallbackData(update,map, processingUsersMessages.readMessage(),mapAction,chatId, StringDataMessage.CREATE_IMAGE.getMessage()));
        }
    }
    private void mapContainsKeyCallbackData(Update update, Map<String, Action> map, long chatId, String callbackData){
        ArrayList<String> readMessage = processingUsersMessages.readMessage();
        log.debug(callbackData);
        String channelChatId = "";
        if (readMessage.size() > 2){
             channelChatId = "@"+processingUsersMessages.readMessage().get(processingUsersMessages.readMessage().size()-1);
        }
        if (callbackData.equals(CREATE_ONLY_TEXT)) {
            send(preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData));
        } else if (callbackData.equals(CREATE_POST)) {
            SendMessage msgChannel = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData);
            log.debug(readMessage);
            msgChannel.setChatId(channelChatId);
            //TODO  Походу нужно создать новый метод preparingMessages.sendCallbackData1
            send(msgChannel);
            SendMessage msg = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData);
            send(msg);

        //TODO убрал callbackData.equals(CREATE_POST) из-неправильной концепции (здесь сообщение отправлялось в канал)
//        } else if (callbackData.equals(CREATE_PREVIEW) || callbackData.equals(CREATE_POST)){
        } else if (callbackData.equals(CREATE_PREVIEW)){
            if (!isUrlHttp(location(callbackData,readMessage))){
                if (readMessage.size() > 2 && callbackData.equals(CREATE_POST)){
                    log.debug(readMessage.size()+"-"+callbackData);
                    SendMessage msg = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData);
                    log.debug(readMessage);
                    msg.setChatId(channelChatId);
                    //sendTextChannel(msg);
                    send(msg);
                    log.debug(msg);
                    SendMessage msg1 = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData);
                    msg1.setText(StringDataMessage.POST_PUBLISHED_CHANNEL.getMessage());
                    send(msg1);
                } else {
                    send(preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData));
                }
            } else {
                switch (extensionFiles(readMessage, callbackData)) {
                    case "mp4" -> {
                        if (callbackData.equals(CREATE_POST)) {
                            SendVideo msgVideo = preparingMessages.sendCallbackDataVideo(update, map, readMessage, mapAction, chatId, callbackData);
                            msgVideo.setChatId(channelChatId);
                            sendVideo(msgVideo);
                        } else {
                            sendVideo(preparingMessages.sendCallbackDataVideo(update, map, readMessage, mapAction, chatId, callbackData));
                        }
                    }
                    case "jpg" -> {
                        if (callbackData.equals(CREATE_POST)) {
                            SendPhoto msgPhoto = preparingMessages.sendCallbackDataPhoto(update, map, readMessage, mapAction, chatId, callbackData);
                            msgPhoto.setChatId(channelChatId);
                            sendPhoto(msgPhoto);
                        } else {
                            sendPhoto(preparingMessages.sendCallbackDataPhoto(update, map, readMessage, mapAction, chatId, callbackData));
                        }
                    }
                    case "gif" -> {
                        if (callbackData.equals(CREATE_POST)) {
                            SendAnimation msgAnimation = preparingMessages.sendCallbackDataAnimation(update, map, readMessage, mapAction, chatId, callbackData);
                            msgAnimation.setChatId(channelChatId);
                            sendAnimation(msgAnimation);
                        } else {
                            sendAnimation(preparingMessages.sendCallbackDataAnimation(update, map, readMessage, mapAction, chatId, callbackData));
                        }
                    }
                    default -> {
                        String key = update.getMessage().getText();
                        send(preparingMessages.sendingMessage(update, key, map, chatId, processingUsersMessages.readMessage(), mapAction));
                    }
                }
            }
        } else {
            send(preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData));
        }
    }

    private String location(String callbackData, ArrayList<String> readMessage) {
        String location = "";
        if (!callbackData.equals(CREATE_POST) && readMessage.size() == 3){
            location = readMessage.get(1);
        } else if (!callbackData.equals(CREATE_POST) && readMessage.size() == 2){
            location = readMessage.get(0);
        } else if (callbackData.equals(CREATE_POST) && readMessage.size() == 4) {
            location = readMessage.get(1);
        } else if (callbackData.equals(CREATE_POST) && readMessage.size() == 3) {
            location = readMessage.get(0);
        }
        return location;
    }

    private String extensionFiles(ArrayList<String> readMessage, String callbackData) {
        String extensionFiles = "";
        try {
            URL url = new URI("http://localhost").toURL();
            if (readMessage.size() == 2) {
                url = new URI(readMessage.get(0)).toURL();
            } else if (readMessage.size() == 3 && !callbackData.equals(CREATE_POST)) {
                url = new URI(readMessage.get(1)).toURL();
            } else if (readMessage.size() == 3 && callbackData.equals(CREATE_POST)) {
                url = new URI(readMessage.get(0)).toURL();
            } else if (readMessage.size() == 4 && callbackData.equals(CREATE_POST)) {
                url = new URI(readMessage.get(1)).toURL();
            }
            extensionFiles = url.getPath().substring(url.getPath().lastIndexOf('.')+1);
        } catch (Exception e){
            log.debug(e);
        }
        return extensionFiles;
    }

    private boolean isUrlHttp(String location) {
        return location != null && location.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

}