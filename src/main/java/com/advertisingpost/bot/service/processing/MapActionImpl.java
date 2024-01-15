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

        map.put("/start", new InfoAction(inputData));
        map.put("QUESTION_ADD_ARTICLE", new PostBodyAction(inputData));
        //map.put("CREATE_HEADER", new PostHeaderAction(inputData));
        map.put("CREATE_BODY", new PostBodyAction(inputData));
        map.put("CREATE_IMAGE", new PostImageAction(inputData));
        map.put("CREATE_ADD_LINK", new PostAddLinkAction(inputData));
        map.put("CREATE_PREVIEW", new PostPreviewAction(inputData));
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
