package com.advertisingpost.bot.service.messaging;

import com.advertisingpost.bot.service.buttonsUsers.interfaces.InputData;
import com.advertisingpost.bot.service.enums.StringDataMessage;
import com.advertisingpost.bot.service.messaging.interfaces.Action;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
@Log4j
@Component
@AllArgsConstructor
public class ChoosingAction implements Action {
    private InputData inputData;
    @Override
    public SendMessage handleText(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        String out = StringDataMessage.CHOOSE_ACTION_BUTTON.getMessage();
        String nameButton1 = StringDataMessage.CHOOSE_BUTTON1.getMessage();
        String nameButton2 = StringDataMessage.CHOOSE_BUTTON2.getMessage();
        String nameButton3 = StringDataMessage.CHOOSE_BUTTON3.getMessage();
        String nameButton = nameButton1+":"+nameButton2+":"+nameButton3;

        String callbackName1 = StringDataMessage.CREATE_BODY.getMessage();
        String callbackName2 = StringDataMessage.CREATE_IMAGE.getMessage();
        String callbackName3 = StringDataMessage.CREATE_BODY_AND_CREATE_IMAGE.getMessage();
        String callbackName = callbackName1+":"+callbackName2+":"+callbackName3;
        //TODO
        //Добавить три кнопки для стиля поста: текст, медиа, текст и медиа.
        //Добавить определение типа загружаемого файла, возможность загрузка gif, видео, картинка
        //Добавить проверку на ввод текста и загрузки файла, если нет текста или нет файла, повторно вызывать нужное действие.
        //Добавить правильное описание действий и хелп
        //Добавить иконку для бота
        //Добавить регистрацию пользователя, для каждого пользователя свой ArrayList (article+photo, перед этим сделать тест работы
        //без регистрации на добавление чужих элементов в ArrayList от других пользователей.
        //Добавить управляемую рекламу на начальную страницу
        //Добавить о боте, контакты разработчика.
        //String callbackName = "CREATE_HEADER";

        var text = StringDataMessage.INFO_ACTION_CREATE_ADV_POST.getMessage()+"\n"+ out;
        return inputData.transmission(chatId, text, nameButton, callbackName, null, null);
    }

    @Override
    public SendMessage callback(Update update) throws MalformedURLException, URISyntaxException {
        return null;
    }

    @Override
    public SendPhoto handlePhoto(Update update, ArrayList<String> textCreatePost) throws MalformedURLException, URISyntaxException {
        return null;
    }
}
