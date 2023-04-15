DROP TABLE friends,likes,users, genre, film_genre, MPA, films;

CREATE TABLE IF NOT EXISTS MPA(
MPA_id INTEGER primary key auto_increment,
MPA_name varchar(10),
constraint MPA_PK primary key (MPA_id)
);

CREATE TABLE IF NOT EXISTS films (
        film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
        name varchar(40) NOT NULL,
        description varchar(200) NOT NULL,
        release_data date NOT NULL,
        duration integer,
        MPA INTEGER REFERENCES MPA(MPA_id)
);
CREATE TABLE IF NOT EXISTS genre(
genre_id int not null primary key auto_increment,
name varchar(255),
constraint GENRE_PK primary key (genre_id)
);

CREATE TABLE  IF NOT EXISTS film_genre(
	film_id INTEGER REFERENCES films (film_id),
	genre_id INTEGER REFERENCES genre (genre_id),
	PRIMARY KEY (film_id,genre_id)
);



CREATE TABLE IF NOT EXISTS users(
	user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	email varchar(20) NOT NULL,
	login varchar(20) NOT NULL,
	name varchar(20),
	birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS likes(
	film_id INTEGER REFERENCES films (film_id),
	user_id INTEGER REFERENCES users (user_id),
	PRIMARY KEY (user_id,film_id)
);

CREATE TABLE IF NOT EXISTS friends(
	user_id INTEGER REFERENCES users (user_id),
	friend_id INTEGER REFERENCES users (user_id),
	status varchar(12),
	PRIMARY KEY (USER_ID,FRIEND_ID)
);