package com.advertisingpost.bot.service.processing;

import com.advertisingpost.bot.dao.interfaces.MessageDao;
import com.advertisingpost.bot.model.UserMessage;
import com.advertisingpost.bot.service.processing.interfaces.ProcessingUsersMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@RequiredArgsConstructor
@Service
public class ProcessingUsersMessagesImpl implements ProcessingUsersMessages {
    @Autowired
    private MessageDao messageDao;
    @Override
    public ArrayList<String> addArticle(String article){
        return messageDao.addArticle(article);
    }
    @Override
    public ArrayList<String> addAPathImage(String pathImage){
        return messageDao.addAPathImage(pathImage);
    }
    @Override
    public ArrayList<String> nameButtonLink(String nameButtonLink){
        return messageDao.addNameButtonLink(nameButtonLink);
    }
    @Override
    public ArrayList<ArrayList<UserMessage>> addArrayList(ArrayList<UserMessage> message) {
        return messageDao.addArrayList(message);
    }
    @Override
    public ArrayList<ArrayList<UserMessage>> clearArrayList() {
        return messageDao.clearArrayList();
    }

    @Override
    public ArrayList<String> readMessage() {
        return messageDao.readMessageList();
    }

}
