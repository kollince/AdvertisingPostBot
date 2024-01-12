package com.advertisingpost.bot.controller;

import com.advertisingpost.bot.config.BotConfig;
import com.advertisingpost.bot.service.*;
import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import com.advertisingpost.bot.service.interfaces.ReadMessageUser;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    @Autowired
    private final ReadMessageUser readMessageUser;
    @Value("${bot.token}")
    String token;
    final BotConfig config;
    public TelegramBot(@Value("${bot.token}") String token, InputData inputData, BotConfig config, ReadMessageUser readMessageUser){
        super(token);
        this.inputData = inputData;
        this.config = config;
        this.readMessageUser = readMessageUser;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId;
        Map<String, Action> map = new HashMap<>();
        map.put("/start", new InfoAction(inputData));
        //map.put("/postheader", new PostHeaderAction(inputData));
//        map.put("/postbody", new PostBodyAction(inputData));
//        map.put("/postimage", new PostImageAction(inputData));
//        map.put("/postaddlink", new PostAddLinkAction(inputData));
//        map.put("/postpreview", new PostPreviewAction(inputData));
        //map.put("CREATE_HEADER", new PostHeaderAction(inputData));
        map.put("CREATE_BODY", new PostBodyAction(inputData));
        map.put("CREATE_IMAGE", new PostImageAction(inputData));
        map.put("CREATE_ADD_LINK", new PostAddLinkAction(inputData));
        map.put("CREATE_PREVIEW", new PostPreviewAction(inputData));
        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            if (map.containsKey(key)) {
                mapContainsKey(update, key, map, chatId);
            } else if (bindingBy.containsKey(chatId)) {
                notMapContainsKey(update, key, map, chatId);
            }
            if (update.getMessage().hasText() && update.getMessage().getText().equals("/start")){
                readMessageUser.clearArrayList();
//                textCreatePost.clear();
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            if (map.containsKey(callbackData)){
                mapContainsKeyCallbackData(update, map, chatId , callbackData);
                AnswerCallbackQuery close = AnswerCallbackQuery.builder()
                        .callbackQueryId(update.getCallbackQuery().getId()).build();
                answerCallbackQuery(close);
            } else if (bindingBy.containsKey(chatId)) {
                SendMessage msg = new SendMessage();
                try {
                    msg = map.get(bindingBy.get(chatId)).callback(update);
                } catch (MalformedURLException | URISyntaxException e) {
                     log.debug(e);
                }
                bindingBy.remove(chatId);
                log.debug(msg);
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
    private void mapContainsKey(Update update, String key, Map<String, Action> map, long chatId){

        if (update.getMessage().hasPhoto()){
            SendPhoto msg = new SendPhoto();
            try {
                //msg = map.get(key).handlePhoto(update, textCreatePost);
                msg = map.get(key).handlePhoto(update, readMessageUser.readMessage());
            } catch (MalformedURLException | URISyntaxException e) {
                log.debug(e);
            }
            bindingBy.put(chatId, key);
            sendPhoto(msg);
        } else {
            SendMessage msg = new SendMessage();
            try {
                //msg = map.get(key).handleText(update, textCreatePost);
                msg = map.get(key).handleText(update, readMessageUser.readMessage());
            } catch (MalformedURLException | URISyntaxException e) {
                log.debug(e);
            }
            bindingBy.put(chatId, key);
            log.debug(msg.getText());
            send(msg);
        }
    }
    private void notMapContainsKey(Update update, String key, Map<String, Action> map, long chatId){
        SendMessage msg = new SendMessage();
        try {
            msg = map.get(bindingBy.get(chatId)).callback(update);
            log.debug(update.getMessage().getEntities());
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
                readMessageUser.addAPathImage(String.valueOf(url));
                //textCreatePost.add(String.valueOf(url));
            } catch (TelegramApiException | IOException | URISyntaxException e) {
                log.debug(e);
            }
        } else {
                //textCreatePost.add(update.getMessage().getText());
                readMessageUser.addArticle(update.getMessage().getText());
            log.debug(readMessageUser.readMessage());

        }
        bindingBy.remove(chatId);
        log.debug(msg.getText());
        send(msg);
    }
    private void mapContainsKeyCallbackData(Update update, Map<String, Action> map, long chatId, String callbackData){
        if (callbackData.equals("CREATE_PREVIEW")){
            SendPhoto msg = new SendPhoto();
            try {
                //msg = map.get(callbackData).handlePhoto(update, textCreatePost);
                msg = map.get(callbackData).handlePhoto(update, readMessageUser.readMessage());
            } catch (MalformedURLException | URISyntaxException e){
                log.debug(e);
            }
            bindingBy.put(chatId, callbackData);
            sendPhoto(msg);
        } else {
            //TODO: Подумать над правильным условием, сюда залетает каждая подсказка после нажатия кнопки
            //TODO: Подумать над структурой всего приложения (MVC)
            //TODO: Подумать как быстро через файл настроек переключаться в режимы HTML или Markdown
            SendMessage msg = new SendMessage();
            try {
                //msg = map.get(callbackData).handleText(update, textCreatePost);
                msg = map.get(callbackData).handleText(update, readMessageUser.readMessage());
                msg.setParseMode(ParseMode.HTML);
            } catch (MalformedURLException | URISyntaxException e) {
                log.debug(e);
            }
            bindingBy.put(chatId, callbackData);
            log.debug(msg.getText());
            send(msg);
        }
    }

}