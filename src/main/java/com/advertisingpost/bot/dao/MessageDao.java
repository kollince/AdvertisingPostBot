package com.advertisingpost.bot.dao;

import com.advertisingpost.bot.model.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private final List<List<UserMessage>> arrayListArrayList = new ArrayList<>();
    private final List<UserMessage> messageList = new ArrayList<>();
    public List<List<UserMessage>> addArrayList(List<UserMessage> message) {
        arrayListArrayList.add(message);
        return arrayListArrayList;
    }
}
