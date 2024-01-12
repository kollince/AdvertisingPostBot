package com.advertisingpost.bot.service.interfaces;

import com.advertisingpost.bot.model.UserMessage;

import java.util.ArrayList;
import java.util.List;

public interface ReadMessageUser {
    ArrayList<ArrayList<UserMessage>> addArrayList(ArrayList<UserMessage> message);

    ArrayList<String> readMessage();

    ArrayList<String> addArticle(String article);

    ArrayList<String> addAPathImage(String pathImage);
    ArrayList<String> nameButtonLink(String nameButtonLink);
    ArrayList<ArrayList<UserMessage>> clearArrayList();


}
