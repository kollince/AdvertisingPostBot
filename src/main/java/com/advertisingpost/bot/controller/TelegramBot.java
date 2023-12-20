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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    private final Map<Long, String> bindingBy = new ConcurrentHashMap<>();
    private String greeting = "Привет! Что умеет этот бот";
    private String textCreatePost;
    @Autowired
    private InputData inputData;
    final BotConfig config;
    public TelegramBot(@Value("${bot.token}") String token, Map<String, Action> actions, UpdatingBot updatingBot, BotConfig config){
        super(token);
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = 0;
        if (update.hasMessage()){
            //chatId = update.getMessage().getChatId();
        }
        ArrayList<String> list = new ArrayList<>();
        list.add("/start - Команды бота");
        list.add( "/echo - Ввод данных для command");
        list.add( "/postheader - Создание рекламного поста");
        Map<String, Action> map = new HashMap<>();
        map.put("/start", new InfoAction(list,inputData));
        map.put("/postheader", new PostHeaderAction(inputData));
        map.put("/postbody", new PostBodyAction());
        map.put("/postimage", new PostImageAction());
        map.put("/postaddlink", new PostAddLinkAction());
        map.put("/postpreview", new PostPreviewAction());
        map.put("CREATE_HEADER", new PostHeaderAction(inputData));


        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            if (map.containsKey(key)) {
                SendMessage msg = map.get(key).handle(update);
                bindingBy.put(chatId, key);
                log.debug(chatId+", "+key);
                send(msg);
            } else if (bindingBy.containsKey(chatId)) {
                BotApiMethod msg = map.get(bindingBy.get(chatId)).callback(update);
                log.debug("1 "+chatId+", "+bindingBy);
                bindingBy.remove(chatId);
                log.debug("2 "+chatId+", "+bindingBy);
                send(msg);
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatCallId = update.getCallbackQuery().getMessage().getChatId();
            //Здесь ошибка
//            if (map.containsKey(callbackData)){
//                SendMessage msg = map.get(callbackData).handle(update);
//                bindingBy.put(chatId, callbackData);
//                log.debug(chatId+", "+callbackData);
//                send(msg);
//            }
            if(callbackData.equals("CREATE_HEADER")){
                String text = "Введите заголовок для нового поста:";
                SendMessage message = new SendMessage();
                message.setChatId(chatCallId);
                message.setText(text);
                executeNewMethod(message);
                log.debug(update.getCallbackQuery());
            } else if (callbackData.equals("CREATE_BODY")) {
                String text = "Введите рекламный текст:";
                SendMessage message = new SendMessage();
                message.setChatId(chatCallId);
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
//        private void advPostCreate(long chatId) {
//            SendMessage message = new SendMessage();
//            String textPreview = EmojiParser.parseToUnicode("<strong><u>Чья-та реклама!</u></strong>\n*Россия* в течение 2–3 лет " +
//                    "может стать лидером в мировой отрасли промышленного майнинга " +
//                    "по уровню использования электроэнергии из попутного нефтяного газа (ПНГ), сообщил основатель и " +
//                    "генеральный директор BitRiver Игорь Рунец. Опыт использования ПНГ для майнинга уже есть в таких " +
//                    "странах, как США, Канада, Саудовская Аравия, Кувейт, Оман, Казахстан и ряде других. :blush:");
//            message.setText(textPreview);
//            message.setChatId(chatId);
//            message.enableHtml(true);
//            message.setReplyMarkup(updatingBot.createButtons());
//            executeNewMethod(message);
//    }
//    private void startCommandReceived(long chatId, String firstName) {
//        String answer = "Привет, "+firstName+"! ";
//        sendMessage(chatId, answer);
//    }
//    private void firstMessage(long chatId, String textToSend){
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(textToSend);
//        message.setReplyMarkup(updatingBot.keyBoard());
//        executeNewMethod(message);
//    }

}