# Проект java-filmorate
### Template repository for Filmorate project.

Проект для хранения фильмов со следующими возможностями:
1. Найти фильм в базе
2. Узнать некоторую информацию о нем
3. Оценить понравившейся фильм

Кроме того, приложение хранит информацию о пользователях.

#### Вся информация хранится в базе данных со следующей структурой:
![Database structure](https://github.com/ol5ga/java-filmorate/blob/add-friends-likes/Database%20structure.png)

### Диаграмма сожержит следующие таблицы:

**films**  
Содержит информацию о фильмах.  
Таблица состоит из полей:  
первичный ключ _film_id_ — идентификатор фильма;  
_name_ — название фильма;  
_description_ — описание фильма;  
_releaseData_ — год выхода;  
_duration_ — продолжительность фильма в минутах;  
_rating_ — возрастной рейтинг;  
  
**film_genre**  
Сожердит информацию о жанрах фильмов  
_film_id_ — идентификатор фильма;    
_genre_id_ — идентификатор жанра;   
  
**genre**   
Содержит список жанров фильмов  
_genre_id_ — идентификатор жанра;  
_genre_ - название жанра;    
  
**likes**  
Сожержит список людей, которым понравился конкретный фильм  
_film_id_ — идентификатор фильма;  
_user_id_ — идентификатор пользователя;  
  
**users**  
_user_id_ — идентификатор пользователя;  
_email_ - почтовый адрес пользователя;  
_login_ - login пользователя;  
_name_ - имя пользователя;  
_birthday_ - дата рождения;  

**friends**  
_user_id_ — идентификатор пользователя;  
_friend_id_ — идентификатор друга пользователя;  
_status_ - статус дружбы: подтвержденная, неподтвержденная;  

### Примеры запросов  
**Список подтвержденных друзей пользователя с id = 1**  
SELECT friend_id  
FROM friends  
WHERE user_id = 1  
AND status = 'confirmed';  
  

**10 фильмов с максимальным количеством лайков**  
SELECT f.name  
FROM films AS f  
RIGHT OUTER JOIN likes AS l ON f.film_id = l.film_id  
GROUP BY l.film_id  
ORDER BY SUM(user_id) DESC  
LIMIT 10;  

  
**Список комедий**  
SELECT name  
FROM films  
WHERE film_id IN (SELECT fg.film_id  
FROM film_genre AS fg  
RIGHT OUTER JOIN genre AS g ON fg.genre_id = g.genre_id  
WHERE g.genre = 'COMEDY');  
