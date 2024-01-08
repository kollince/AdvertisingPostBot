package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

@Log4j
@Component
@AllArgsConstructor
public class PostImageAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) {
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        var text = StringDataMessage.POST_IMAGE_ACTION_IMAGE_UPLOAD.getMessage();
        return new SendMessage(chatId, text);
    }
    @Override
    public SendMessage callback(Update update) {
        String chatId = "";
        String messageText = "";
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().hasPhoto()){
                GetFile getFile = new GetFile();
                int el = update.getMessage().getPhoto().size() - 1;
                messageText = "";
            } else {
                messageText = "";
            }
        } else if (update.hasCallbackQuery()){
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            messageText = update.getCallbackQuery().getMessage().getText()+" 2";
        } else if (update.getMessage().hasPhoto()){
            int el = update.getMessage().getPhoto().size() - 1;
            messageText = update.getMessage().getPhoto().get(el).getFilePath();
        }

        String nameButton = StringDataMessage.POST_IMAGE_ACTION_ADD_LINK_BUTTON.getMessage();
        String callbackName = "CREATE_ADD_LINK";
        var text = StringDataMessage.POST_IMAGE_ACTION_IMAGE_ADDED.getMessage();
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }
    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        return null;
    }

}
