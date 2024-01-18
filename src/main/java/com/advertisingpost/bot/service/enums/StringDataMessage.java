package com.advertisingpost.bot.service.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum StringDataMessage {
    /*common*/
    COMMON_INPUT_TEXT_BUTTON("Перейти к вводу текста"),
    /*ChoosingAction*/
    CHOOSE_ACTION_BUTTON("Выберите тип поста:"),
    CHOOSE_BUTTON1("Только текст"),
    CHOOSE_BUTTON2("Только мультимедиа"),
    CHOOSE_BUTTON3("Текст и мультимедиа"),
    /*InfoAction*/
    INFO_ACTION_SELECT_ACTION(""),
    INFO_ACTION_HELP("Для продолжения нажмите на кнопку «" + COMMON_INPUT_TEXT_BUTTON.message +"»."),
    INFO_ACTION_CREATE_ADV_POST("Этот бот предназначен для создания статей с inline - кнопкой."),

    /*PostHeaderAction*/
    POST_HEADER_ACTION_ENTER_ADV_HEADER("Введите заголовок для поста:"),
    POST_HEADER_ACTION_HEADER_ADDED("Заголовок добавлен, для продолжения " +
            "нажмите на кнопку «"+COMMON_INPUT_TEXT_BUTTON.message+"»."),

    /*PostBodyImageAction*/
    POST_BODY_IMAGE_ACTION_ENTER_ADV_TEXT("Введите текст для статьи:"),
    POST_BODY_IMAGE_ACTION_IMAGE_UPLOAD_BUTTON("Перейти к загрузке изображения"),
    POST_BODY_IMAGE_ACTION_TEXT_ADDED("Текст добавлен, для продолжения нажмите на кнопку «"+
            POST_BODY_IMAGE_ACTION_IMAGE_UPLOAD_BUTTON.message+"»."),

    /*PostBodyAction*/
    POST_BODY_ACTION_ENTER_ADV_TEXT("Введите текст для статьи:"),
    POST_BODY_ACTION_IMAGE_UPLOAD_BUTTON("Перейти к загрузке изображения"),
    POST_BODY_ACTION_TEXT_ADDED("Текст добавлен, для продолжения нажмите на кнопку «"+
            POST_BODY_ACTION_IMAGE_UPLOAD_BUTTON.message+"»."),
    /*PostImageAction*/
    POST_IMAGE_ACTION_IMAGE_UPLOAD("Загрузите изображение:"),
    POST_IMAGE_ACTION_ADD_LINK_BUTTON("Добавить ссылку для кнопки"),
    POST_IMAGE_ACTION_IMAGE_ADDED("Изображение добавлено, для продолжения нажмите на кнопку «"+
            POST_IMAGE_ACTION_ADD_LINK_BUTTON.message+"»."),
    /*PostAddLInkAction*/
    POST_ADD_LINK_ACTION_ADD_LINK("Добавьте ссылку:   <code>название ссылки : site.com/mypage</code>"),
    POST_ADD_LINK_ACTION_OPEN_ADV_POST("Открыть созданный пост"),
    POST_ADD_LINK_ACTION_IMAGE_ADDED("Ссылка добавлена, для продолжения нажмите на кнопку «"+
            POST_ADD_LINK_ACTION_OPEN_ADV_POST.message+"»."),

    /*PostPreviewAction*/
    POST_PREVIEW_ACTION_LINK_BUTTON("Перейти"),
    /*MapAction and CallbackName*/
    CREATE_BODY("CREATE_BODY"),
    CREATE_IMAGE("CREATE_IMAGE"),
    CREATE_BODY_AND_CREATE_IMAGE("CREATE_BODY_AND_CREATE_IMAGE");
    private final String message;

}
