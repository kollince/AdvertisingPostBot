package com.advertisingpost.bot.model;

import lombok.*;
import lombok.extern.log4j.Log4j;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Log4j
public class UserMessage {
    private String article;
    private String pathImage;
    private String nameButtonLink;

}
