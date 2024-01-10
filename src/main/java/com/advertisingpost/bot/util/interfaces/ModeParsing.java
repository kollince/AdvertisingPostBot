package com.advertisingpost.bot.util.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public interface ModeParsing {
    SendMessage ParsingMessage(SendMessage message);
    SendPhoto ParsingPhoto(SendPhoto sendPhoto);
}
