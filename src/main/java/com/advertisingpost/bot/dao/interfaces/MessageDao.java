package com.advertisingpost.bot.dao.interfaces;

import com.advertisingpost.bot.model.UserMessage;

import java.util.ArrayList;
import java.util.List;

public interface MessageDao {
    ArrayList<String> addArticle(String article);
    ArrayList<String> addAPathImage(String pathImage);
    ArrayList<String> addNameButtonLink(String nameButtonLink);
    ArrayList<ArrayList<UserMessage>> addArrayList(ArrayList<UserMessage> message);
    ArrayList<ArrayList<UserMessage>> clearArrayList();
    ArrayList<String> readMessageList();
}
