package com.advertisingpost.bot.service.interfaces;

import com.advertisingpost.bot.model.UserMessage;

import java.util.List;

public interface ReadMessageUser {
    List<List<UserMessage>> addArrayList(List<UserMessage> message);
}
