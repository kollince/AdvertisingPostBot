package com.advertisingpost.bot.controllers;

import com.advertisingpost.bot.config.BotConfig;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    private final String createPost = "Создать пост";
    private final String about = "О нас";
    private String greeting = "Привет!";
    private String textCreatePost;
    final BotConfig config;
    public TelegramBot(@Value("${bot.token}") String token, BotConfig config){
        super(token);
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
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(greeting);
        executeNewMethod(response);


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
    //    private void postCreate(long chatId) {
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        String textPreview = EmojiParser.parseToUnicode("<strong><u>Чья-та реклама!</u></strong>\n*Россия* в течение 2–3 лет " +
//                "может стать лидером в мировой отрасли промышленного майнинга " +
//                "по уровню использования электроэнергии из попутного нефтяного газа (ПНГ), сообщил основатель и " +
//                "генеральный директор BitRiver Игорь Рунец. Опыт использования ПНГ для майнинга уже есть в таких " +
//                "странах, как США, Канада, Саудовская Аравия, Кувейт, Оман, Казахстан и ряде других. :blush:");
//
//        message.setText(textPreview);
//        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        var linkButton = new InlineKeyboardButton();
//        linkButton.setText("Подробнее");
//        linkButton.setCallbackData("LINK_BUTTON");
//        linkButton.setUrl("https://ya.ru");
//        rowInline.add(linkButton);
//        rowsInline.add(rowInline);
//        markup.setKeyboard(rowsInline);
//        message.enableHtml(true);
//        message.setReplyMarkup(markup);
//        executeNewMethod(message);
//    }
//    private void startCommandReceived(long chatId, String firstName) {
//        String answer = "Привет, "+firstName+"! ";
//        sendMessage(chatId, answer);
//    }
//    private void sendMessage(long chatId, String textToSend){
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText(textToSend);
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setResizeKeyboard(true);
//        List<KeyboardRow> keyboardRows = new ArrayList<>();
//        KeyboardRow row = new KeyboardRow();
//        row.add(createPost);
//        row.add(about);
//        keyboardRows.add(row);
//        keyboardMarkup.setKeyboard(keyboardRows);
//        message.setReplyMarkup(keyboardMarkup);
//        executeNewMethod(message);
//    }

}
