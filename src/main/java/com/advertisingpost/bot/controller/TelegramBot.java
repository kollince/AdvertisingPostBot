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
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    private final InputData inputData;
    private final ProcessingUsersMessages processingUsersMessages;
    private final MapAction mapAction;
    private final PreparingMessages preparingMessages;
    @Value("${bot.token}")
    String token;
    final BotConfig config;
    private final String CREATE_IMAGE = StringDataMessage.CREATE_IMAGE.getMessage();
    private final String CREATE_PREVIEW = StringDataMessage.CREATE_PREVIEW.getMessage();
    private final String CREATE_ADD_CHANNEL = StringDataMessage.CREATE_ADD_CHANNEL.getMessage();
    private final String CREATE_POST = StringDataMessage.CREATE_POST.getMessage();
    private final String CANCEL_POST = StringDataMessage.CANCEL_POST.getMessage();
    private final String CREATE_ADD_LINK = StringDataMessage.CREATE_ADD_LINK.getMessage();

    public TelegramBot(@Value("${bot.token}") String token, InputData inputData, BotConfig config,
                       ProcessingUsersMessages processingUsersMessages, MapAction mapAction, PreparingMessages preparingMessages){
        super(token);
        this.inputData = inputData;
        this.config = config;
        this.processingUsersMessages = processingUsersMessages;
        this.mapAction = mapAction;
        this.preparingMessages = preparingMessages;
        menuCommands();
    }

    private void menuCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        String START = StringDataMessage.START.getMessage();
        String MENU_START = StringDataMessage.MENU_START.getMessage();
        listOfCommands.add(new BotCommand(START, MENU_START));
        String HELP = StringDataMessage.HELP.getMessage();
        String MENU_HELP = StringDataMessage.MENU_HELP.getMessage();
        listOfCommands.add(new BotCommand(HELP, MENU_HELP));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId;
        mapAction.generalMapPut(inputData);
        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            chatId = update.getMessage().getChatId().toString();
            if(mapAction.generalMapRead().containsKey(key)){
                mapContainsKey(update,key,mapAction.generalMapRead(), chatId);
            } else if (mapAction.bindingByRead().containsKey(chatId)) {
                try {
                    notMapContainsKey(update, mapAction.generalMapRead(), chatId);
                } catch (TelegramApiException e) {
                    log.debug(e);
                }
            }
            if (
                    update.getMessage().hasText()
                            &&
                    update.getMessage().getText().equals(StringDataMessage.START.getMessage())
               )
            {
                processingUsersMessages.clearArrayList();
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
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
    private boolean checkChannel(SendMessage msg) {
        boolean isChannel = false;
        try {
            isChannel = execute(msg).isChannelMessage();
        } catch (TelegramApiException e) {
            log.debug(e);}
        return isChannel;
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
    private void mapContainsKey(Update update, String key, Map<String, Action> map, String chatId){

        if (update.getMessage().hasText()){

            send(preparingMessages.sendingMessage(update, key, map, chatId,
                    processingUsersMessages.readMessage(), mapAction));
        }
    }

    private void notMapContainsKey(Update update, Map<String, Action> map, String chatId) throws TelegramApiException {
        switch (stateInputChannelMethod(update, chatId)) {
            case 0 -> send(preparingMessages.collectingMessagesMedia
                    (
                            update, map, chatId, mapAction, processingUsersMessages, token, sendFile(update)
                           .getFilePath()
                    )
            );
            case 1 -> send(preparingMessages.collectingMessages
                    (
                            update, map, chatId, mapAction, processingUsersMessages, token
                    )
            );

            case 2 -> send(preparingMessages.sendCallbackData
                    (
                            update, map, processingUsersMessages.readMessage(), mapAction, chatId,
                            StringDataMessage.CREATE_ADD_CHANNEL.getMessage(), false
                    )
            );
            case 3 -> send(preparingMessages.sendCallbackData
                    (
                            update, map, processingUsersMessages.readMessage(), mapAction, chatId,
                            StringDataMessage.CREATE_IMAGE.getMessage(), false
                    )
            );
            case 4 -> send(preparingMessages.sendCallbackData
                    (
                            update, map, processingUsersMessages.readMessage(), mapAction, chatId,
                            StringDataMessage.CREATE_ADD_LINK.getMessage(), false
                    )
            );

        }
    }

    private int stateInputChannelMethod(Update update, String chatId) {
        int stateInputChannel = -1;
        if (update.getMessage().hasPhoto() || update.getMessage().hasVideo() || update.getMessage().hasAnimation()) {
            stateInputChannel = 0;
        } else if (update.getMessage().hasText()) {
            if (!mapAction.bindingByRead().get(chatId).equals(CREATE_IMAGE)) {
                if (mapAction.bindingByRead().get(chatId).equals(CREATE_ADD_CHANNEL)) {
                    String nameChannel;
                    if (update.getMessage().getForwardFromChat() != null) {
                        nameChannel = update.getMessage().getForwardFromChat().getUserName();
                    } else {
                        nameChannel = update.getMessage().getText();
                    }
                    GetChat getChat = new GetChat("@"+nameChannel);
                    try {
                        if (execute(getChat).isChannelChat()) {
                            stateInputChannel = 1;
                            update.getMessage().setText(nameChannel);
                        }
                    } catch (Exception e) {
                        stateInputChannel = 2;
                    }
                }
                if (mapAction.bindingByRead().containsKey(chatId)) {
                    if (!mapAction.bindingByRead().get(chatId).equals(CREATE_ADD_CHANNEL)) {
                        stateInputChannel = 1;
                        if (mapAction.bindingByRead().get(chatId).equals(CREATE_ADD_LINK)) {
                            String[] element = update.getMessage().getText().split(":");
                            if (element.length < 2) {
                                stateInputChannel = 4;
                            } else {
                                String url = "https://"+element[1].trim();
                                if (isUrlHttp(url)){
                                    stateInputChannel = 1;
                                }else {
                                    stateInputChannel = 4;
                                }
                            }
                        }
                    }
                }
            } else {
                stateInputChannel = 3;
              }
        }
        return stateInputChannel;
    }

    private void mapContainsKeyCallbackData(Update update, Map<String, Action> map, String chatId, String callbackData){
        ArrayList<String> readMessage = processingUsersMessages.readMessage();
            if (callbackData.equals(CREATE_PREVIEW)) {
            if (!isUrlHttp(location(callbackData,readMessage))) {
                send(preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData, false));
            } else {
                invokeMedia(update, map, chatId, callbackData, readMessage);
            }
        } else if (callbackData.equals(CREATE_POST)) {
            if (!isUrlHttp(location(callbackData,readMessage))) {
                SendMessage msgChannel = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData, false);
                msgChannel.setChatId(channelChatId(readMessage));
                send(msgChannel);
                SendMessage msg = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData, true);
                send(msg);
            } else {
                invokeMedia(update, map, chatId, callbackData, readMessage);
            }
        } else {
                if (callbackData.equals(CANCEL_POST)){
                    processingUsersMessages.clearArrayList();
                }
            send(preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData, false));
        }
    }

    private String channelChatId(ArrayList<String> readMessage) {
        String channelChatId = "default";
        if (readMessage.size() > 2){
            channelChatId = "@"+processingUsersMessages.readMessage().get(processingUsersMessages.readMessage().size()-1);
        }
        return channelChatId;
    }

    private void invokeMedia(Update update, Map<String, Action> map, String chatId, String callbackData, ArrayList<String> readMessage) {
        switch (extensionFiles(readMessage, callbackData)) {
            case "mp4" -> {
                if (callbackData.equals(CREATE_POST)) {
                    SendVideo msgVideo = preparingMessages.sendCallbackDataVideo(update, map, readMessage, mapAction, chatId, callbackData, false);
                    msgVideo.setChatId(channelChatId(readMessage));
                    sendVideo(msgVideo);
                    SendMessage msg = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData, true);
                    send(msg);
                } else {
                    sendVideo(preparingMessages.sendCallbackDataVideo(update, map, readMessage, mapAction, chatId, callbackData, false));
                }
            }
            case "jpg" -> {
                if (callbackData.equals(CREATE_POST)) {
                    SendPhoto msgPhoto = preparingMessages.sendCallbackDataPhoto(update, map, readMessage, mapAction, chatId, callbackData, false);
                    msgPhoto.setChatId(channelChatId(readMessage));
                    sendPhoto(msgPhoto);
                    SendMessage msg = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData, true);
                    send(msg);
                } else {
                    sendPhoto(preparingMessages.sendCallbackDataPhoto(update, map, readMessage, mapAction, chatId, callbackData, false));
                }
            }
            case "gif" -> {
                if (callbackData.equals(CREATE_POST)) {
                    SendAnimation msgAnimation = preparingMessages.sendCallbackDataAnimation(update, map, readMessage, mapAction, chatId, callbackData, false);
                    msgAnimation.setChatId(channelChatId(readMessage));
                    sendAnimation(msgAnimation);
                    SendMessage msg = preparingMessages.sendCallbackData(update, map, readMessage, mapAction, chatId, callbackData, true);
                    send(msg);
                } else {
                    sendAnimation(preparingMessages.sendCallbackDataAnimation(update, map, readMessage, mapAction, chatId, callbackData, false));
                }
            }
            default -> {
                String key = update.getMessage().getText();
                send(preparingMessages.sendingMessage(update, key, map, chatId, processingUsersMessages.readMessage(), mapAction));
            }
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
        return location != null && location.matches("^https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{2,6}\\b(?:[-a-zA-Z0-9()@:%_+.~#?&/=]*)$");
    }
}