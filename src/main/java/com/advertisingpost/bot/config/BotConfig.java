package com.advertisingpost.bot.config;


import com.advertisingpost.bot.controller.TelegramBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
//@PropertySource("application.properties")
@Data
public class BotConfig {
    @Value("${bot.name}")
    public String botName;
    @Value("${bot.token}")
    public String token;
    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(telegramBot);
        return api;
    }


}