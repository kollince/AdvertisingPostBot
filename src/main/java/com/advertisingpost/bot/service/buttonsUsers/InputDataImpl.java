package com.advertisingpost.bot.service.buttonsUsers;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.processing.interfaces.ModeParsing;
import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.*;

@Component
@Log4j
@Service
@AllArgsConstructor
public class InputDataImpl implements InputData {
    @Autowired
    private final ModeParsing modeParsing;
    @Override
    public SendMessage transmission(String chatId, String text, String nameButton, String callbackName, String link, URL url) {
        //inlineButtons(nameButton, callbackName, link);
        SendMessage message = new SendMessage(chatId, text);
        modeParsing.ParsingMessage(message);
        message.setReplyMarkup(inlineButtons(nameButton, callbackName, link));
        return message;
    }
    @Override
    public SendPhoto photoTransmission(String chatId, String text, String nameButton, String callbackName, String link, URL url) {
        SendPhoto sendPhoto = new SendPhoto();
        try {
//            BufferedImage img = ImageIO.read(url);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(img, "jpg", baos);
            sendPhoto.setChatId(chatId);
            //sendPhoto.setPhoto(new InputFile(new ByteArrayInputStream(baos.toByteArray()), "photo.jpg"));
            sendPhoto.setPhoto(new InputFile(url.openStream(), "photo.jpg"));
            sendPhoto.setCaption(EmojiParser.parseToUnicode(text));
            log.debug(sendPhoto);
            modeParsing.ParsingPhoto(sendPhoto);
            sendPhoto.setReplyMarkup(inlineButtons(nameButton, callbackName, link));
        } catch (Exception e){
            log.debug(e);
        }
        return sendPhoto;
    }

    @Override
    public SendVideo videoTransmission(String chatId, String text, String nameButton, String callbackName, String link, URL url) {
        SendVideo sendVideo = new SendVideo();
        try {
            sendVideo.setVideo(new InputFile(url.openStream(), "video.mp4"));
            sendVideo.setChatId(chatId);
            sendVideo.setCaption(EmojiParser.parseToUnicode(text));
            modeParsing.ParsingVideo(sendVideo);
            sendVideo.setReplyMarkup(inlineButtons(nameButton, callbackName, link));
        } catch (Exception e){
            log.debug(e);
        }
        return sendVideo;
    }
    @Override
    public SendAnimation animationTransmission(String chatId, String text, String nameButton, String callbackName, String link, URL url) {
        SendAnimation sendAnimation = new SendAnimation();
        try {
            sendAnimation.setAnimation(new InputFile(url.openStream(), "animation.gif"));
            sendAnimation.setChatId(chatId);
            sendAnimation.setCaption(EmojiParser.parseToUnicode(text));
            modeParsing.ParsingAnimation(sendAnimation);
            sendAnimation.setReplyMarkup(inlineButtons(nameButton, callbackName, link));
        } catch (Exception e){
            log.debug(e);
        }
        return sendAnimation;
    }

    private InlineKeyboardMarkup inlineButtons(String nameButton, String callbackName, String link) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        String[] nameButtonsArray = nameButton.split(":");
        String[] callbackNameArray = callbackName.split(":");
        if(nameButtonsArray.length > 1) {
            for (int i = 0; i < 3; i++) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                var button = new InlineKeyboardButton();
                button.setText(nameButtonsArray[i]);
                button.setCallbackData(callbackNameArray[i]);
                rowInline.add(button);
                rowsInline.add(rowInline);
            }
        } else {
//            Map<String, String> mapButtons = Map.of(nameButton, callbackName, "Отмена", "CANCEL");
//            List<Map.Entry<String, String>> list = new ArrayList<>(mapButtons.entrySet());
//            list.sort(Map.Entry.comparingByKey());
//            for (Map.Entry<String, String> entry : list) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                var linkButton = new InlineKeyboardButton();
                linkButton.setText(nameButton);
                linkButton.setCallbackData(callbackName);
//                if (mapButtons.containsKey(nameButton)){
                    if (link != null) {
                        linkButton.setUrl(link);
                        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
                        var linkButton1 = new InlineKeyboardButton();
                        linkButton1.setText("Отмена");
                        linkButton1.setCallbackData("CANCEL");
                        rowInline1.add(linkButton1);
                        rowsInline.add(rowInline1);
                    }
//                }
                rowInline.add(linkButton);
                rowsInline.add(rowInline);
//            }
//            for (int i = 0; i < 2; i++) {
//                List<InlineKeyboardButton> rowInline = new ArrayList<>();
//                var linkButton = new InlineKeyboardButton();
//                linkButton.setText(nameButton);
//                linkButton.setCallbackData(callbackName);
//                if (link != null) {
//                    linkButton.setUrl(link);
//                }
//                rowInline.add(linkButton);
//                rowsInline.add(rowInline);
//            }
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);
        return markup;
    }

}
