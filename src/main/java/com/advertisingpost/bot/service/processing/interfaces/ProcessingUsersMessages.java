package com.advertisingpost.bot.service.processing.interfaces;

import com.advertisingpost.bot.model.UserMessage;

import java.util.ArrayList;

public interface ProcessingUsersMessages {
    ArrayList<ArrayList<UserMessage>> addArrayList(ArrayList<UserMessage> message);

    ArrayList<String> readMessage();

    ArrayList<String> addArticle(String article);

    ArrayList<String> addAPathImage(String pathImage);
    ArrayList<String> nameButtonLink(String nameButtonLink);
    ArrayList<ArrayList<UserMessage>> clearArrayList();


}
