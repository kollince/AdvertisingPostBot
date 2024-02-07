package com.advertisingpost.bot.service.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum StringDataMessage {
    /*ChoosingAction*/
    CHOOSE_ACTION_BUTTON("Выберите тип поста:"),
    CHOOSE_ACTION_CREATE_ADV_POST("Этот бот предназначен для создания статей с inline - кнопкой."),
    CHOOSE_BUTTON1("Только текст"),
    CHOOSE_BUTTON2("Только мультимедиа"),
    CHOOSE_BUTTON3("Текст и мультимедиа"),

    /*PostBodyImageAction*/
    POST_BODY_IMAGE_ACTION_ENTER_ADV_TEXT("Введите текст для статьи:"),
    POST_BODY_IMAGE_ACTION_IMAGE_UPLOAD_BUTTON("Перейти к загрузке изображения"),
    POST_BODY_IMAGE_ACTION_TEXT_ADDED("Текст добавлен, для продолжения нажмите на кнопку «"+
            POST_BODY_IMAGE_ACTION_IMAGE_UPLOAD_BUTTON.message+"»."),

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
    /*PostOnlyTextAction*/
    POST_ONLY_TEXT_ACTION_ENTER_ADV_TEXT("Введите текст для статьи:"),
    POST_ONLY_TEXT_ACTION_TEXT_ADDED("Текст добавлен, для продолжения нажмите на кнопку «"+
            POST_IMAGE_ACTION_ADD_LINK_BUTTON.message+"»."),

    /*PostPreviewAction*/
    POST_BUTTON_NEXT("Далее"),
    POST_BUTTON_CANCEL("Отмена"),
    /*PostAddChannel*/
    POST_ADD_CHANNEL_ACTION("Отправьте сюда наименование своего канала, которое идет после \"t.me/\" или перешлите в этот чат любой опубликованный пост."),
    POST_BUTTON_PUBLISH("Опубликовать"),
    POST_CHANNEL_ADDED("Канал добвален"),

    /*PostPublishAction*/
    POST_PUBLISHED_CHANNEL("Пост успешно опубликован."),
    CREATE_POST_CHANNEL("Посмотреть пост"),

    /*MapAction and CallbackName*/
    CREATE_ONLY_TEXT("CREATE_ONLY_TEXT"),
    CREATE_BODY_AND_CREATE_IMAGE("CREATE_BODY_AND_CREATE_IMAGE"),
    CREATE_BODY("CREATE_BODY"),
    CREATE_IMAGE("CREATE_IMAGE"),
    CREATE_ADD_LINK("CREATE_ADD_LINK"),
    CREATE_PREVIEW("CREATE_PREVIEW"),
    CREATE_ADD_CHANNEL("CREATE_ADD_CHANNEL"),
    CREATE_POST("CREATE_POST"),
    CANCEL_POST("CANCEL_POST");
    private final String message;

}
