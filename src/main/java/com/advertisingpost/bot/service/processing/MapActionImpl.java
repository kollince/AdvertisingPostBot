package com.advertisingpost.bot.service.processing;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.messaging.*;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.processing.interfaces.MapAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Log4j
@AllArgsConstructor
@Data
@Component
public class MapActionImpl implements MapAction {
    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private final Map<String, Action> map = new HashMap<>();
    private final String START = StringDataMessage.START.getMessage();
    private final String HELP = StringDataMessage.HELP.getMessage();
    private final String CANCEL_POST = StringDataMessage.CANCEL_POST.getMessage();
    private final String CREATE_ONLY_TEXT = StringDataMessage.CREATE_ONLY_TEXT.getMessage();
    private final String CREATE_BODY_AND_CREATE_IMAGE = StringDataMessage.CREATE_BODY_AND_CREATE_IMAGE.getMessage();
    private final String CREATE_IMAGE = StringDataMessage.CREATE_IMAGE.getMessage();
    private final String CREATE_ADD_LINK = StringDataMessage.CREATE_ADD_LINK.getMessage();
    private final String CREATE_PREVIEW = StringDataMessage.CREATE_PREVIEW.getMessage();
    private final String CREATE_ADD_CHANNEL = StringDataMessage.CREATE_ADD_CHANNEL.getMessage();
    private final String CREATE_POST = StringDataMessage.CREATE_POST.getMessage();
    private final String VIEW_POST = StringDataMessage.VIEW_POST.getMessage();

    @Override
    public Map<String, Action> generalMapPut (InputData inputData) {
        map.put(START, new ChoosingAction(inputData));
        map.put(HELP, new PostHelpAction(inputData));
        map.put(CANCEL_POST, new ChoosingAction(inputData));
        map.put(CREATE_ONLY_TEXT, new PostOnlyTextAction(inputData));//text1
        map.put(CREATE_BODY_AND_CREATE_IMAGE, new PostBodyImageAction(inputData));//text_image1
        map.put(CREATE_IMAGE, new PostImageAction(inputData));//text_image2 /image1
        map.put(CREATE_ADD_LINK, new PostAddLinkAction(inputData));//text_image3 //text2 //image2
        map.put(CREATE_PREVIEW, new PostPreviewAction(inputData));//text_image4 //text3 //image3
        map.put(CREATE_ADD_CHANNEL, new PostAddChannel(inputData));
        map.put(CREATE_POST, new PostPublishAction(inputData));
        map.put(VIEW_POST, new PostViewAction(inputData));
        return map;
    }
    @Override
    public Map<String, Action> generalMapRead(){
        return map;
    }
    @Override
    public Map<String, String> bindingByRead(){
        return bindingBy;
    }
    @Override
    public Map<String, String> bindingByRemove(String chatId){
        bindingBy.remove(chatId);
        return bindingBy;
    }
    @Override
    public Map<String,String> bindingByPut(String chatId, String key){
        bindingBy.put(chatId, key);
        return bindingBy;
    }


}
