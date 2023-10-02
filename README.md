# <span style="color: white">**JAVA-explore-with-me**
____
![JAVA-explore-with-me](/explore-with-me.png)

 ### <span style="color: white">Данное приложение предназначено для создания/поиска событий или мероприятий. ###
<span style="color: white">Приложение использует слудющие технологии:
- Java version 11
- Spring Boot (web, validation, data-jpa)
- Maven
- используемая БД - PostgreSQL
- Docker
- Lombok
- Json-Simple
- тестирование Postman коллекцией

<span style="color: white">Приложение разделено на 2 сервера, общающихся между собой по API:
- main (обрабатывает запросы от пользователя и все бизнес логика) 
- stats 
(собирает статистику http запросов к main сервису)

<span style="color: white">Так же приложение обращается к внешему серверу (Яндекс.Карты) посредствам API для получение 
информации о локации мероприятия по ее координатам (!) Если данный функциона не работает, необходимо обновить 
подписку API Яндекс.Карты

<span style="color: white">Инструкция по развертыванию приложения:

Так как проект разработан для развертывания на удаленном сервере, запуск его настроен через Docker. 
В файле docker-compose.yml прописаны инструкции для приложения по развертыванию и запуску как серверов,
так и необходим БД к ним. Перед запуском убедится что Maven собрал необходимые компоненты, если нет, то произвести 
clean - package. Дале сформировать Image в Docker, можно как при помощи средств IDEA (перейти в файл docker-compose.yml 
и запустить) или через командную строку, переместившись в нужную директорию и выполнить команды docker-compose build и 
docker-compose up. После чего будут собраны Image, на их основе созданы и запущены соответствующие контейнеры.



<span style="color: white">___Ниже приведены эндпоинты и кратное описаних их функционала:___
### <span style="color: white">User:
- создания пользователем учетной записи от имини администратора. <span style="color: green">(POST "/admin/users")
- получение списка всех зарегистрированных пользователей администратиором. <span style="color: green">(GET "/admin/users?from&size")
- удаление пользователя администратором по его id <span style="color: green">(DELETE "/admin/users/{userId}")

### <span style="color: white">Category:
- добавление новой категории мероприятия от имени администратора <span style="color: green"> (POST "/admin/categories")
- удаление категории мероприятия по ее id от имени администратора <span style="color: green">(DELETE "/admin/categories/{catId}")
- обновления данных о категории мероприятия от имени администратора <span style="color: green">(PATCH "/admin/categories/{catId}")
- получение списка доступных категорий мероприятий от пользователя <span style="color: green">(GET "/categories?from&size")
- получение данных о конкретной категории пероприятия по ее id от пользователя <span style="color: green">(GET "/categories/{catId}")

### <span style="color: white">Event:
- поиск мероприятий по заданным параметрам  <span style="color: green">GET "/events?text&categories&paid&rangeStart&rangeEnd&onlyAvailable&sort&from&size")</span>, таким как:
  - описание мероприятия, по ключевым слова (text)
  - по категориям мероприятий (categoriesId)
  - плата за вход (paid)
  - временной интервал поиска мероприятия (rangeStart, rangeEnd)
  - доступно ли мероприятия, не исчерпан ли лимит заявок на участие (onlyAvailable)
  - пагинация, вывод количества удовлетворяющий параметрам поиска мероприятий (from, size)
- получение информации о конкретном мероприятии по его id <span style="color: green">(GET "/events/{id}")
- получение списка мероприятий, созданных конкретным пользователем <span style="color: green">(GET "/users/{userId}/events?from&size")
- запрос на создание мероприятия от пользователя, требующее подтверждения администратором <span style="color: green">(POST "/users/{userId}/events")
- получение подробной информации о мероприятии по ее id от ползьмователя <span style="color: green">(GET "/users/{userId}/events/{eventId}")
- запрос на обновлене информации о мероприятии от пользователя <span style="color: green">(PATCH "/users/{userId}/events/{eventId}")
- получение информации о запросах пользователя на участие в мероприятии <span style="color: green">(GET "/users/{userId}/events/{eventId}/requests")
- запрос на обновление информации о заявке на участие в событии <span style="color: green">(PATCH "/users/{userId}/events/{eventId}/requests")
- получение списка мероприятий от администратора по параметрам <span style="color: green">(GET "/admin/events?users&states&categories&rangeStart&rangeEnd&from&size")
- обновление информации о мероприятии от имени администратора <span style="color: green">(PATCH "/admin/events/{eventId}")

### <span style="color: white">Location:
- создание локации администратором по ее координатам <span style="color: green">(POST "/admin/location?lat&lon")
- получение информации о всех созданных локациях от администратора <span style="color: green">(GET "/admin/location")
- поиск мероприя по ее местопроведению и дате <span style="color: green">(GET "/events/location?locationText&rangeStart&rangeEnd&from&size")
- 
### <span style="color: white">Compilation:
- получение подборки мероприятий <span style="color: green">(GET "/compilations?pinned&from&size")
- получение подборки мероприятий по id <span style="color: green">(GET "/compilations/{compId}")
- создание подборки мероприятий администратором <span style="color: green">(POST "/admin/compilations")
- удаление подборки мероприятий администратором <span style="color: green">(DELETE "/admin/compilations/{compId}")
- обновление информации о подборке от администратора <span style="color: green">(PATCH "/admin/compilations/{compId}")

### <span style="color: white">Participation Request:
- получение информации о заявказ пользователя на участие в чужых событиях <span style="color: green">(GET "/users/{userId}/requests")
- создание запроса от пользователя на участие в событии <span style="color: green">(POST "/users/{userId}/requests?userId&eventId")
- отмена запроса на участие в событии от пользователя <span style="color: green">(PATCH "/users/{userId}/requests/{requestId}/cancel")

<span style="color: white">Для тестирования данного приложения к корневой папке проекта имееста Postman коллекция.txt







