package com.advertisingpost.bot.service.processing;

import com.advertisingpost.bot.service.processing.interfaces.ModeParsing;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

@Log4j
@Component
public class ModeParsingImpl implements ModeParsing {
    @Value("${bot.ParseMode}")
    String mode;
    @Override
    public SendMessage ParsingMessage(SendMessage message){
        switch (mode) {
            case "HTML" -> message.setParseMode(ParseMode.HTML);
            case "MARKDOWN" -> message.setParseMode(ParseMode.MARKDOWN);
            case "MARKDOWNV2" -> message.setParseMode(ParseMode.MARKDOWNV2);
        }
        return message;
    }
    @Override
    public SendPhoto ParsingPhoto(SendPhoto sendPhoto){
        switch (mode) {
            case "HTML" -> sendPhoto.setParseMode(ParseMode.HTML);
            case "MARKDOWN" -> sendPhoto.setParseMode(ParseMode.MARKDOWN);
            case "MARKDOWNV2" -> sendPhoto.setParseMode(ParseMode.MARKDOWNV2);
        }
        return sendPhoto;
    }

    @Override
    public SendVideo ParsingVideo(SendVideo sendVideo) {
        switch (mode) {
            case "HTML" -> sendVideo.setParseMode(ParseMode.HTML);
            case "MARKDOWN" -> sendVideo.setParseMode(ParseMode.MARKDOWN);
            case "MARKDOWNV2" -> sendVideo.setParseMode(ParseMode.MARKDOWNV2);
        }
        return sendVideo;
    }
    @Override
    public SendAnimation ParsingAnimation(SendAnimation sendAnimation) {
        switch (mode) {
            case "HTML" -> sendAnimation.setParseMode(ParseMode.HTML);
            case "MARKDOWN" -> sendAnimation.setParseMode(ParseMode.MARKDOWN);
            case "MARKDOWNV2" -> sendAnimation.setParseMode(ParseMode.MARKDOWNV2);
        }
        return sendAnimation;
    }

}
