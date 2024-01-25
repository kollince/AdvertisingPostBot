package com.advertisingpost.bot.service.buttonsUsers;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.processing.interfaces.ModeParsing;
import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        SendPhoto sendPhoto1 = new SendPhoto();
        try {
            File file = new File(url.getFile());
            byte [] myByteArray  = new byte [(int)file.length()];

            BufferedImage img = ImageIO.read(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(new ByteArrayInputStream(myByteArray), "photo.jpg"));
            //log.debug(sendPhoto1.getPhoto());
            //sendPhoto.setPhoto(new InputFile(new ByteArrayInputStream(baos.toByteArray()), "photo.jpg"));
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
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            int bytesRead;
            int current = 0;
            Socket sock = null;
            File file = new File(url.toURI());
            byte [] myByteArray  = new byte [(int)file.length()];
//            fos = new FileOutputStream(file);
//            bos = new BufferedOutputStream(fos);
//            InputStream is = sock.getInputStream();
//            bytesRead = is.read(myByteArray,0,myByteArray.length);
//            current = bytesRead;
//            bos.write(myByteArray, 0, current);
            //TODO
            sendVideo.setChatId(chatId);
            sendVideo.setVideo(new InputFile(new ByteArrayInputStream(myByteArray), "video.mp4"));
            sendVideo.setCaption(EmojiParser.parseToUnicode(text));
            modeParsing.ParsingVideo(sendVideo);
            sendVideo.setReplyMarkup(inlineButtons(nameButton, callbackName, link));
        } catch (Exception e){
            log.debug(e);
        }
        return sendVideo;
    }

    private InlineKeyboardMarkup inlineButtons(String nameButton, String callbackName, String link) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        String[] nameButtonsArray = nameButton.split(":");
        String[] callbackNameArray = callbackName.split(":");
        if(nameButtonsArray.length > 1) {
            for (int i = 0; i < 3; i++) {
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                var button = new InlineKeyboardButton();
                //TODO Доделать callbackName для кнопок
                button.setText(nameButtonsArray[i]);
                button.setCallbackData(callbackNameArray[i]);
                rowInline.add(button);
                rowsInline.add(rowInline);
            }
        } else {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            var linkButton = new InlineKeyboardButton();
            linkButton.setText(nameButton);
            linkButton.setCallbackData(callbackName);
            if (link != null) {
                linkButton.setUrl(link);
            }
            rowInline.add(linkButton);
            rowsInline.add(rowInline);
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);
        return markup;
    }

}
