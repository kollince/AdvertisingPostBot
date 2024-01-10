package com.advertisingpost.bot.util;

import com.advertisingpost.bot.util.interfaces.ModeParsing;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

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
}
