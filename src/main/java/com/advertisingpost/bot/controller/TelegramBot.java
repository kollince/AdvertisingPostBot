package com.advertisingpost.bot.controller;

import com.advertisingpost.bot.config.BotConfig;
import com.advertisingpost.bot.service.*;
import com.advertisingpost.bot.service.interfaces.Action;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
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
    final UpdatingBot updatingBot;
    final BotConfig config;
    public TelegramBot(@Value("${bot.token}") String token, Map<String, Action> actions, UpdatingBot updatingBot, BotConfig config){
        super(token);
        this.updatingBot = updatingBot;
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        log.debug(update.getMessage().getText());
        ArrayList<String> list = new ArrayList<>();
        list.add("/start - Команды бота");
        list.add( "/echo - Ввод данных для command");
        list.add( "/postheader - Создание рекламного поста");
        Map<String, Action> map = new HashMap<>();
        map.put("/start", new InfoAction(list));
        map.put("/postheader", new PostHeaderAction());
        map.put("/postbody", new PostBodyAction());
        map.put("/postimage", new PostImageAction());
        map.put("/postaddlink", new PostAddLinkAction());
        map.put("/postpreview", new PostPreviewAction());

        //map.put("/echo", new )

        //advPostCreate(chatId);
        //firstMessage(chatId,greeting);

        if (update.hasMessage()) {
            String key = update.getMessage().getText();
            if (map.containsKey(key)) {
                BotApiMethod msg = map.get(key).handle(update);
                bindingBy.put(chatId, key);
                send(msg);
            } else if (bindingBy.containsKey(chatId)) {
                BotApiMethod msg = map.get(bindingBy.get(chatId)).callback(update);
                bindingBy.remove(chatId);
                send(msg);
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
