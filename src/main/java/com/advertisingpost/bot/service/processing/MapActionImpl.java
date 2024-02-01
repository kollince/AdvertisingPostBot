package com.advertisingpost.bot.service.processing;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.messaging.*;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import com.advertisingpost.bot.service.processing.interfaces.MapAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Data
@Component
public class MapActionImpl implements MapAction {
    private final Map<Long, String> bindingBy = new ConcurrentHashMap<>();
    private final Map<String, Action> map = new HashMap<>();
    @Override
    public Map<String, Action> generalMapPut (InputData inputData) {
        map.put("/start", new ChoosingAction(inputData));
        map.put("CANCEL", new ChoosingAction(inputData));
        map.put("CREATE_ONLY_TEXT", new PostOnlyTextAction(inputData));//text1
        map.put("CREATE_BODY_AND_CREATE_IMAGE", new PostBodyImageAction(inputData));//text_image1
        map.put("CREATE_IMAGE", new PostImageAction(inputData));//text_image2 /image1
        map.put("CREATE_ADD_LINK", new PostAddLinkAction(inputData));//text_image3 //text2 //image2
        map.put("CREATE_PREVIEW", new PostPreviewAction(inputData));//text_image4 //text3 //image3
        map.put("POST", new ChoosingAction(inputData));
        return map;
    }
    @Override
    public Map<String, Action> generalMapRead(){
        return map;
    }
    @Override
    public Map<Long, String> bindingByRead(){
        return bindingBy;
    }
    @Override
    public Map<Long, String> bindingByRemove(long chatId){
        bindingBy.remove(chatId);
        return bindingBy;
    }
    @Override
    public Map<Long,String> bindingByPut(long chatId, String key){
        bindingBy.put(chatId, key);
        return bindingBy;
    }


}
