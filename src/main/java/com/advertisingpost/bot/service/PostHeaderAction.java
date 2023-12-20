package com.advertisingpost.bot.service;

import com.advertisingpost.bot.service.interfaces.Action;
import com.advertisingpost.bot.service.interfaces.InputData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Component
@AllArgsConstructor
public class PostHeaderAction implements Action {
    //@Autowired
    private InputData inputData;

//    public PostHeaderAction(InputData inputData) {
//        this.inputData = inputData;
//    }

    @Override
    public SendMessage handle(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var text = "Введите заголовок для нового поста2:";
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        var headerText = update.getMessage().getText();
        String nameButton = "Перейти к вводу текста";
        String callbackName = "INPUT_TEXT";
        var text = "Заголовок " + headerText + " добавлен, выполните команду: /postbody ";
        //transmission(chatId, text, nameButton, callbackName);
        return inputData.transmission(chatId, text, nameButton, callbackName);
    }

//        private SendMessage transmission(String chatId, String text, String nameButton, String callbackName){
//            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//            List<InlineKeyboardButton> rowInline = new ArrayList<>();
//            var linkButton = new InlineKeyboardButton();
//            linkButton.setText(nameButton);
//            linkButton.setCallbackData(callbackName);
//            //linkButton.setUrl("/postbody");
//            rowInline.add(linkButton);
//            rowsInline.add(rowInline);
//            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
//            markup.setKeyboard(rowsInline);
//            // userRepository.save(new User(email));
//            SendMessage message = new SendMessage(chatId, text);
//            message.setReplyMarkup(markup);
//            return message;
//        }


}
