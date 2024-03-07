package com.advertisingpost.bot.dao;

import com.advertisingpost.bot.dao.interfaces.MessageDao;
import com.advertisingpost.bot.model.UserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@RequiredArgsConstructor
@Component
@Log4j
public class MessageDaoImpl implements MessageDao {
    private final ArrayList<ArrayList<UserMessage>> arrayListArrayList = new ArrayList<>();
    private final ArrayList<String> messageList = new ArrayList<>();
    @Override
    public ArrayList<String> addArticle(String article){
        messageList.add(article);
        return messageList;
    }
    @Override
    public ArrayList<String> addAPathImage(String pathImage){
        messageList.add(pathImage);
        return messageList;
    }
    @Override
    public ArrayList<String> addNameButtonLink(String nameButtonLink){
        messageList.add(nameButtonLink);
        return messageList;
    }
    @Override
    public ArrayList<ArrayList<UserMessage>> addArrayList(ArrayList<UserMessage> message) {
        arrayListArrayList.add(message);
        return arrayListArrayList;
    }

    @Override
    public ArrayList<ArrayList<UserMessage>> clearArrayList() {
        messageList.clear();
        arrayListArrayList.clear();
        return arrayListArrayList;
    }

    @Override
    public ArrayList<String> readMessageList() {
        return messageList;
    }

}
