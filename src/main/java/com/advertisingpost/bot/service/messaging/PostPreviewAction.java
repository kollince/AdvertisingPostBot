package com.advertisingpost.bot.service.messaging;

import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

@Log4j
@AllArgsConstructor
@Component
public class PostPreviewAction implements Action {
    private InputData inputData;
    private String[] getData(Update update, ArrayList<String> textCreatePost, boolean isTextAttach) {
        String text = "" ;
        String chatId;
        String[] textLink = new String[0];
        String url = null;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        try {
            if (textCreatePost.size() == 2) {
                textLink = textCreatePost.get(1).split(":");
                if (isTextAttach){
                    text = textCreatePost.get(0);
                } else {
                    url = textCreatePost.get(0);
                }
            }  else if (textCreatePost.size() == 3) {
                text = textCreatePost.get(0);
                textLink = textCreatePost.get(2).split(":");
                url = textCreatePost.get(1);
            }
        } catch (Exception e) {
            log.debug(e);
        }
        String postButtonPublish = StringDataMessage.POST_BUTTON_NEXT.getMessage();
        String postButtonCancel = StringDataMessage.POST_BUTTON_CANCEL.getMessage();
        String callbackNamePost = StringDataMessage.CREATE_ADD_CHANNEL.getMessage();
        String callbackNameCancel = StringDataMessage.CANCEL_POST.getMessage();
        String nameButton = textLink[0].trim()+":"+postButtonPublish+":"+postButtonCancel;
        String callbackName = callbackNamePost+":"+callbackNameCancel;
        String link = textLink[1].trim();
        return new String[] {chatId, text, nameButton, callbackName, link, url};
    }
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = true;
        String[] data = getData(update, textCreatePost, isTextAttach);
        URL url = null;
        try {
            if (data[5] != null) url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        return inputData.transmission(data[0], data[1], data[2], data[3], data[4], url);
    }
    @Override
    public SendMessage callback(Update update) {
        String chatId;
        if (update.hasMessage()){
            chatId = update.getMessage().getChatId().toString();
        }else {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        }
        String nameButton = StringDataMessage.POST_ADD_LINK_ACTION_OPEN_ADV_POST.getMessage();
        String callbackName = StringDataMessage.CREATE_PREVIEW.getMessage();
        var text = StringDataMessage.POST_ADD_LINK_ACTION_IMAGE_ADDED.getMessage();
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = false;
        URL url = null;
        String[] data = getData(update, textCreatePost, isTextAttach);
        try {
            url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        return inputData.photoTransmission(data[0], data[1], data[2], data[3], data[4], url);
    }
    @Override
    public SendVideo handleVideo(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = false;
        URL url = null;
        String[] data = getData(update, textCreatePost, isTextAttach);
        try {
            url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        return inputData.videoTransmission(data[0], data[1], data[2], data[3], data[4], url);
    }
    @Override
    public SendAnimation handleAnimation(Update update, ArrayList<String> textCreatePost, boolean isPublished) {
        boolean isTextAttach = false;
        URL url = null;
        String[] data = getData(update, textCreatePost, isTextAttach);
        try {
            url = new URI(data[5]).toURL();
        } catch (Exception e){
            log.debug(e);
        }
        return inputData.animationTransmission(data[0], data[1], data[2], data[3], data[4], url);
    }

}
