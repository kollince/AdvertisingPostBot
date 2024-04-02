## Телеграм бот для создания рекламных постов
### Описание
Данный telegram бот публикует сообщения с кнопкой ссылкой на различные 
источники информации, включая рекламу. От пользователя требуется 
только следовать указаниям бота. Бот автоматически определяет медиа 
файлы (видео, изображения и анимацию). Есть функция предварительного 
просмотра и кнопка отмены. Целесообразно сделать этого бота частным, 
так как в нем не предусмотрена возможность многопользовательского 
использования.

[//]:![AdvertisingPostBot.png](src/main/resources/AdvertisingPostBot.png)

### Используемые инструменты:
1. [x] Maven:
    * Telegram Bot Java Library;
    * Lombok;
    * Log4j.

### Запуск
1. Для запуска телеграм бота необходимо создать нового бота через BotFather.
2. Сделать бота администратором телеграм канала, на котором хотите
   размещать информацию данного формата.
3. Получить токен созданного бота.
4. Запустить данный проект.
5. Скопировать имя бота без символа @ в первую строку файла 
application.properties.
6. Скопировать токен в файл application.properties во вторую строку.
7. Скомпилировать проект.
8. Запустить исполняемый файл AdvertisingBot-0.0.1.jar.
9. Открыть в боте меню, выполнить команду /start и следовать подсказкам.
