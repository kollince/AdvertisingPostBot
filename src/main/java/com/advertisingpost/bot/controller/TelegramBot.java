package com.advertisingpost.bot.controller;

import com.advertisingpost.bot.config.BotConfig;
import com.advertisingpost.bot.service.*;
import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    private final Map<Long, String> bindingBy = new ConcurrentHashMap<>();
    private final ArrayList<String> textCreatePost = new ArrayList<>();
    @Autowired
    private final InputData inputData;

    @Value("${bot.token}")
    String token;
    final BotConfig config;
    public TelegramBot(@Value("${bot.token}") String token, InputData inputData, BotConfig config){
        super(token);
        this.inputData = inputData;
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId;
        ArrayList<String> list = new ArrayList<>();
        list.add("/start - Команды бота");
        list.add( "/echo - Ввод данных для command");
        list.add( "/postheader - Создание рекламного поста");
        Map<String, Action> map = new HashMap<>();
        map.put("/start", new InfoAction(list,inputData));
        map.put("/postheader", new PostHeaderAction(inputData));
        map.put("/postbody", new PostBodyAction(inputData));
        map.put("/postimage", new PostImageAction(inputData));
        map.put("/postaddlink", new PostAddLinkAction(inputData));
        map.put("/postpreview", new PostPreviewAction(inputData));
        map.put("CREATE_HEADER", new PostHeaderAction(inputData));
        map.put("CREATE_BODY", new PostBodyAction(inputData));
        map.put("CREATE_IMAGE", new PostImageAction(inputData));
        map.put("CREATE_ADD_LINK", new PostAddLinkAction(inputData));
        map.put("CREATE_PREVIEW", new PostPreviewAction(inputData));
        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            if (map.containsKey(key)) {
                if (update.getMessage().hasPhoto()){
                    SendPhoto msg = new SendPhoto();
                    try {
                        msg = map.get(key).handlePhoto(update, textCreatePost);
                    } catch (MalformedURLException | URISyntaxException e) {
                        log.debug(e);
                    }
                    bindingBy.put(chatId, key);
                    sendPhoto(msg);
                } else {
                    SendMessage msg = new SendMessage();
                    try {
                        msg = map.get(key).handleText(update, textCreatePost);
                    } catch (MalformedURLException | URISyntaxException e) {
                        log.debug(e);
                    }
                    bindingBy.put(chatId, key);
                    send(msg);
                }
            } else if (bindingBy.containsKey(chatId)) {
                BotApiMethod msg = new SendMessage();
                try {
                    msg = map.get(bindingBy.get(chatId)).callback(update);
                } catch (MalformedURLException | URISyntaxException e) {
                    log.debug(e);
                }
                if (update.getMessage().hasPhoto()){
                    GetFile getFile = new GetFile();
                    int el = update.getMessage().getPhoto().size()-1;
                    getFile.setFileId(update.getMessage().getPhoto().get(el).getFileId());
                    try {
                        File file = execute(getFile);
                        URL url = new URI("https://api.telegram.org/file/bot" + token + "/" + file.getFilePath()).toURL();
                        textCreatePost.add(String.valueOf(url));
                        log.debug(url);
                    } catch (TelegramApiException | IOException | URISyntaxException e) {
                        log.debug(e);
                    }
                } else {
                    textCreatePost.add(update.getMessage().getText());
                }
                bindingBy.remove(chatId);
                send(msg);
            }
            if (update.getMessage().hasText() && update.getMessage().getText().equals("/start")){
                textCreatePost.clear();
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            if (map.containsKey(callbackData)){
                if (callbackData.equals("CREATE_PREVIEW")){
                    SendPhoto msg = new SendPhoto();
                    try {
                        msg = map.get(callbackData).handlePhoto(update, textCreatePost);
                    } catch (MalformedURLException | URISyntaxException e){
                        log.debug(e);
                    }
                    bindingBy.put(chatId, callbackData);
                    sendPhoto(msg);
                } else {
                    SendMessage msg = new SendMessage();
                    try {
                        msg = map.get(callbackData).handleText(update, textCreatePost);
                    } catch (MalformedURLException | URISyntaxException e) {
                        log.debug(e);
                    }
                    bindingBy.put(chatId, callbackData);
                    send(msg);
                }
            } else if (bindingBy.containsKey(chatId)) {
                log.debug(bindingBy);
                BotApiMethod msg;
                try {
                    msg = map.get(bindingBy.get(chatId)).callback(update);
                } catch (MalformedURLException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                bindingBy.remove(chatId);
                send(msg);
            }
            if(callbackData.equals("CREATE_SUCCESSFUL")) {
                String text = "Пост успешно создан";
                SendMessage message = new SendMessage();
                message.setChatId(update.getCallbackQuery().getMessage().getChatId());
                message.setText(text);
                executeNewMethod(message);
            }

        }

    }
    private void executeNewMethod(SendMessage message) {
        try{
            if (message != null) {
                execute(message);
            }
        } catch (TelegramApiException e){
            log.debug(e);
        }
    }
    private void send(BotApiMethod msg) {
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
}