package com.advertisingpost.bot.service.processing.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

public interface ModeParsing {
    SendMessage ParsingMessage(SendMessage message);
    SendPhoto ParsingPhoto(SendPhoto sendPhoto);
    SendVideo ParsingVideo(SendVideo sendVideo);
}
