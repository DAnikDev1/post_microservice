Второй микросервис, содержащий в себе работу с Постами, Комментариями, Лайками.
Для проверки существования юзера, микросервис может отправить http запрос по feign, чтобы узнать существует ли юзер.
Имеется глобальный обработчик. При определённых условиях, создаётся ивент - нотификация, их есть 5 видов. Они 
отправляются по кафке в Третий микросервис (NotificationService). Ивенты отправляются ассинхроно, ибо зачем
юзеру ждать, когда уведомление доставится? Это ему не нужно абсолютно. Имеется MapStruct для автоматической конвертации
сущности в dto. Есть Swagger/OpenAPI для автоматической документации контроллеров

Раз в 10 минут кеширует топ 100 самых популярных постов на 10 минут на основе лайков
Обычные запросы GET кешируется на 1 минуту, при изменении (PUT/PATCH/DELETE) кеш обнуляется
