ссылка на пул-реквест:

### https://github.com/AlexanderKolnakov/java-explore-with-me/pull/5

В последнем пул-реквест реализована фича по установлению локации на основе переданных значение события 
(широты (lat) и долготы (lon)) путем передачи данных параметров по API сервису Яндекс.Карты.

Переработана сущность Location и завязанные на ней методы и БД

Добавлены новые эндпоинты:

GET  "/admin/location" - получение списка всех созданных и сохраненных в БД локаций

POST  "/admin/location" - по переданным параметра lat, lon позволят создать новую локацию

GET  "/events/location" - по параметрам поиска (locationText - описание локации/город/страна, 
rangeStart,rangeEnd - временной интервал для поиска мероприятия, from,size - возможность постраничного вывода информации)
выдает список мероприятий


TO DO: разобраться и доделать Postman коллекцию для тестирования новой фичи



