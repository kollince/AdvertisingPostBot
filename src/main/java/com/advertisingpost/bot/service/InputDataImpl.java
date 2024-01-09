package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.InputData;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
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
public class InputDataImpl implements InputData {


    @Override
    public SendMessage transmission(String chatId, String text, String nameButton, String callbackName, String link, URL url) {
        inlineButtons(nameButton, callbackName, link);
        SendMessage message = new SendMessage(chatId, text);
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
//            String textPreview = EmojiParser.parseToUnicode("~~Чья та реклама~~\n**Россия** в течение 2–3 лет " +
//                    "может стать лидером в мировой отрасли промышленного __майнинга__ " +
//                    "по уровню использования электроэнергии из ||попутного|| нефтяного газа ПНГ сообщил основатель и " +
//                    "генеральный директор BitRiver Игорь Рунец Опыт использования ПНГ для майнинга уже есть в таких " +
//                    "странах как США Канада Саудовская Аравия Кувейт Оман Казахстан и ряде других :blush:");
            sendPhoto.setCaption(EmojiParser.parseToUnicode(text));
            sendPhoto.setParseMode(ParseMode.HTML);
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
        if (link != null){
            linkButton.setUrl(link);
        }
        rowInline.add(linkButton);
        rowsInline.add(rowInline);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInline);
        return markup;
    }

}
