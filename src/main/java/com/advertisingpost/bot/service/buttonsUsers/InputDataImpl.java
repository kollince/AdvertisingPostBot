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
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        inlineButtons(nameButton, callbackName, link);
        SendMessage message = new SendMessage(chatId, text);
        modeParsing.ParsingMessage(message);
        message.setReplyMarkup(inlineButtons(nameButton, callbackName, link));
        return message;
    }

    @Override
    public SendPhoto photoTransmission(String chatId, String text, String nameButton, String callbackName, String link, URL url) {
        SendPhoto sendPhoto = new SendPhoto();
        try {
            BufferedImage img = ImageIO.read(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(new ByteArrayInputStream(baos.toByteArray()), "photo.jpg"));
            sendPhoto.setCaption(EmojiParser.parseToUnicode(text));
            modeParsing.ParsingPhoto(sendPhoto);
            sendPhoto.setReplyMarkup(inlineButtons(nameButton, callbackName, link));
        } catch (Exception e){
            log.debug(e);
        }
        return sendPhoto;
    }
    private InlineKeyboardMarkup inlineButtons(String nameButton, String callbackName, String link) {
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var linkButton = new InlineKeyboardButton();
        linkButton.setText(nameButton);
        linkButton.setCallbackData(callbackName);
        var linkButton1 = new InlineKeyboardButton();
        linkButton1.setText(nameButton);
        linkButton1.setCallbackData(callbackName);
        var linkButton2 = new InlineKeyboardButton();
        linkButton2.setText(nameButton);
        linkButton2.setCallbackData(callbackName);
        if (link != null){
            linkButton.setUrl(link);
        }
        rowInline.add(linkButton2);
        rowInline.add(linkButton1);
        rowInline.add(linkButton);
        rowsInline.add(rowInline);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);
        return markup;
    }

}
