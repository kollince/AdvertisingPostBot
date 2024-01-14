package com.advertisingpost.bot.service.processing.interfaces;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.messaging.interfaces.Action;

import java.util.Map;

public interface MapAction {
    Map<String, Action> generalMapPut (InputData inputData);
    Map<String, Action> generalMapRead();
    Map<Long, String> bindingByRead();
    Map<Long, String> bindingByRemove(long chatId);
    Map<Long,String> bindingByPut(long chatId, String key);
}
