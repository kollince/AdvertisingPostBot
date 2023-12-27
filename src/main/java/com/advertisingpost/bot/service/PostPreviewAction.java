package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Log4j
@AllArgsConstructor
@Component
public class PostPreviewAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handle(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        String text = "" ;
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
       // String image = update.getMessage().getPhoto().get(0).getFileId();
        text = textCreatePost.get(0)+"\n"+textCreatePost.get(1)+"\n"+textCreatePost.get(2);
        String link = textCreatePost.get(3);
        URL url = new URI(textCreatePost.get(2)).toURL();
        log.debug(text);
        //var text = "Здесь будет отображаться рекламный пост. Бла бла бла...";
//        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//        List<InlineKeyboardButton> rowInline = new ArrayList<>();
//        var linkButton = new InlineKeyboardButton();
//        linkButton.setText("Перейти");
//        linkButton.setUrl(textCreatePost.get(3));
//        rowInline.add(linkButton);
//        rowsInline.add(rowInline);
//        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
//        markup.setKeyboard(rowsInline);
//        SendMessage message = new SendMessage(chatId, text);
//        message.setReplyMarkup(markup);
        String nameButton = "Перейти";
        String callbackName = "Перейти";

        return inputData.transmission(chatId,text, nameButton, callbackName, link, url);
    }

    @Override
    public BotApiMethod callback(Update update) throws MalformedURLException, URISyntaxException {

//        String chatId;
//        String messageText;
//        if (update.hasMessage()){
//            chatId = update.getMessage().getChatId().toString();
//            messageText = update.getMessage().getText();
//        }else {
//            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
//            messageText = update.getCallbackQuery().getMessage().getText();
//        }
//        String nameButton = "Просмотреть";
//        String callbackName = "CREATE_SUCCESSFUL";
//        var text = "Пост создан успешно!";
//        return inputData.transmission(chatId, text, nameButton, callbackName);
        ArrayList<String> temp = new ArrayList<>();
        temp.add("0");
        return handle(update,temp);
    }
}
