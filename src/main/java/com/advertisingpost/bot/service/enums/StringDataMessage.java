package com.advertisingpost.bot.service.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum StringDataMessage {
    /*ChoosingAction*/
    CHOOSE_ACTION_BUTTON("Выберите тип поста:"),
    CHOOSE_ACTION_CREATE_ADV_POST("Этот бот помогает создавать рекламные посты, которые могут быть использованы для " +
            "продвижения вашего бизнеса или продукта. Для начала работы с ботом, необходимо отправить команду " +
            "/start, вам будет предложено выбрать тип рекламного поста, который вы хотели бы создать. " +
            "Далее просто следуйте подсказкам бота.\n"),
    CHOOSE_BUTTON1("Только текст"),
    CHOOSE_BUTTON2("Только медиа (изображения и видео)"),
    CHOOSE_BUTTON3("Текст и медиа"),

    /*PostBodyImageAction*/
    POST_BODY_IMAGE_ACTION_ENTER_ADV_TEXT("Отправьте боту текст, который хотите опубликовать. " +
            "Бот поддерживает простую HTML разметку. Более подробную информацию о синтаксисе HTML вы можете узнать, " +
            "выполнив команду /help. Обратите внимание, что после выполнения этой команды, вам придется " +
            "начать заново."),
    POST_BODY_IMAGE_ACTION_IMAGE_UPLOAD_BUTTON("Перейти к загрузке медиа-файла"),
    POST_BODY_IMAGE_ACTION_TEXT_ADDED("Текст добавлен, для продолжения нажмите на кнопку «"+
            POST_BODY_IMAGE_ACTION_IMAGE_UPLOAD_BUTTON.message+"»."),

    /*PostImageAction*/
    POST_IMAGE_ACTION_IMAGE_UPLOAD("Загрузите медиа-файл (любое изображение или видео):"),
    POST_IMAGE_ACTION_ADD_LINK_BUTTON("Добавить ссылку для кнопки"),
    POST_IMAGE_ACTION_IMAGE_ADDED("Медиа-файл добавлен, для продолжения нажмите на кнопку «"+
            POST_IMAGE_ACTION_ADD_LINK_BUTTON.message+"»."),
    /*PostAddLInkAction*/
    POST_ADD_LINK_ACTION_ADD_LINK("Добавьте название кнопки и для неё ссылку в следующем формате: " +
            "<code>название ссылки : site.com/mypage</code>."),
    POST_ADD_LINK_ACTION_OPEN_ADV_POST("Предварительный просмотр"),
    POST_ADD_LINK_ACTION_IMAGE_ADDED("Новая кнопка была создана, для продолжения нажмите на кнопку «"+
            POST_ADD_LINK_ACTION_OPEN_ADV_POST.message+"»."),
    /*PostOnlyTextAction*/
    POST_ONLY_TEXT_ACTION_ENTER_ADV_TEXT("Отправьте боту текст, который хотите опубликовать. " +
            "Бот поддерживает простую HTML разметку. Более подробную информацию о синтаксисе HTML вы можете узнать, " +
            "выполнив команду /help. Обратите внимание, что после выполнения этой команды, вам придется " +
            "начать заново."),
    POST_ONLY_TEXT_ACTION_TEXT_ADDED("Текст добавлен, для продолжения нажмите на кнопку «"+
            POST_IMAGE_ACTION_ADD_LINK_BUTTON.message+"»."),

    /*PostPreviewAction*/
    POST_BUTTON_NEXT("Далее"),
    POST_BUTTON_CANCEL("Отмена"),
    /*PostAddChannel*/
    POST_ADD_CHANNEL_ACTION("Отправьте боту название своего канала, которое следует после \"t.me/\" " +
            "или перешлите в этот чат любой опубликованный пост в своем телеграм канале."),
    POST_BUTTON_PUBLISH("Опубликовать"),
    POST_CHANNEL_ADDED("Канал добавлен, для публикации нажмите кнопку «"+POST_BUTTON_NEXT.message+"»."),
    /*PostPublishAction*/
    POST_PUBLISHED_CHANNEL("Пост успешно опубликован в Вашем телеграм канале "),
    CREATE_POST_CHANNEL("Посмотреть пост"),
    POST_BUTTON_NEXT_CHANNEL("Далее"),
    /*TelegramBot*/
    MENU_START("Чтобы приступить, в любой момент нажмите старт"),
    MENU_HELP("Помощь"),
    /*PostHelpAction*/
    HELP_TEXT("В настоящее время поддерживаются следующие HTML-теги: \n<b>жирный</b>, \n<strong>жирный</strong>\n" +
            "<i>курсив</i>, \n<em>курсив</em>, \n<u>подчёркнутый</u>, \n<ins>подчёркнутый</ins>, \n" +
            "<s>зачёркнутый</s>, \n<strike>зачёркнутый</strike>, \n<del>зачёркнутый</del>, \n" +
            "<span class=\"tg-spoiler\">скрытый</span>, \n<tg-spoiler>скрытый</tg-spoiler>\n" +
            "<b>\n" +
            "   жирный \n" +
            "       <i>жирный курсив \n" +
            "           <s>зачёркнутый жирный курсив \n" +
            "               <span class=\"tg-spoiler\">\n" +
            "                   скрытый зачёркнутый жирный курсив\n" +
            "               </span>\n" +
            "           </s> \n" +
            "           <u>подчёркнутый жирный курсив</u>\n" +
            "       </i> \n" +
            "   жирный\n" +
            "</b>\n" +
            "<a href=\"http://www.example.com/\">ссылка на ресурс</a>\n" +
            "<a href=\"tg://user?id=123456789\">упоминание пользователя</a>\n" +
            "<tg-emoji emoji-id=\"5368324170671202286\">\uD83D\uDC4D</tg-emoji>\n" +
            "<code>моноширинный</code>\n" +
            "<pre>предварительно форматированный текст</pre>\n" +
            "<pre>\n" +
            "   <code class=\"language-python\">\n" +
            "       предварительно отформатированный блок \n" +
            "       кода фиксированной ширины\n" +
            "   </code>\n" +
            "</pre>\n" +
            "<blockquote>\n" +
            "   Цитата \n" +
            "</blockquote>"),

    /*MapAction and CallbackName*/
    START("/start"),
    HELP("/help"),
    CREATE_ONLY_TEXT("CREATE_ONLY_TEXT"),
    CREATE_BODY_AND_CREATE_IMAGE("CREATE_BODY_AND_CREATE_IMAGE"),
    CREATE_BODY("CREATE_BODY"),
    CREATE_IMAGE("CREATE_IMAGE"),
    CREATE_ADD_LINK("CREATE_ADD_LINK"),
    CREATE_PREVIEW("CREATE_PREVIEW"),
    CREATE_ADD_CHANNEL("CREATE_ADD_CHANNEL"),
    CREATE_POST("CREATE_POST"),
    CANCEL_POST("CANCEL_POST"),
    VIEW_POST("VIEW_POST");
    private final String message;

}
