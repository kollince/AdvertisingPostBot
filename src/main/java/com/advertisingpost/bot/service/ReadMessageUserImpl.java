package com.advertisingpost.bot.service;

import com.advertisingpost.bot.dao.MessageDao;
import com.advertisingpost.bot.model.UserMessage;
import com.advertisingpost.bot.service.interfaces.ReadMessageUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class ReadMessageUserImpl implements ReadMessageUser {
    @Autowired
    private MessageDao messageDao;
    @Override
    public List<List<UserMessage>> addArrayList(List<UserMessage> message) {
        return messageDao.addArrayList(message);
    }
}
