create sequence library_id_seq;

create table literature
(
isbn varchar(17) not null,
title varchar(200) not null,
edition varchar(25) not null,

primary key(isbn)
);

create table direction_of_training -- Направление подготовки, например 09.03.03 Прикладная информатика
(
code varchar(9) not null,
title varchar(100) not null,

primary key (code)
);

create table "class" -- Конкретный курс на направлении подготовки. Например, разработка программного обеспечения
(
title varchar(100) not null,
code_direction_training_FK varchar(9) not null,
student_amount integer not null,

primary key (title),
foreign key (code_direction_training_FK)
	references direction_of_training (code)
		on delete cascade
		on update cascade
);

create table student
(
id serial not null constraint id primary key,
last_name varchar(25) not null,
first_name varchar(25) not null,
middle_name varchar(25) not null,
class_title_FK varchar(100) not null,

foreign key (class_title_FK) 
	references "class" (title) 
		on delete cascade
		on update cascade
);

create table accounting_book
(
id serial, -- id операции с книгой. Еще один вариант создания первичного ключа.
isbn_FK varchar(17) not null, 
student_id_FK integer not null, -- Кто взял книгу
date_take date not null, -- Когда взял книгу
return_date date not null, -- Когда вернули книгу

primary key (id),
foreign key (isbn_FK) 
	references literature (isbn) 
		on update cascade
		on delete cascade,
foreign key (student_id_FK)
	references student (id)
		on update cascade
		on delete cascade
);

create table "library"
(
id integer not null default nextval('library_id_seq'::regclass), -- Создание первичного ключа с автоинкрементом
title_FK varchar(100) not null, -- Наименование конкретной специальности на каком-либо направлении подготовки
isbn_FK varchar(17) not null,

primary key (id),
foreign key (title_FK)
	references "class" (title)
		on update cascade
		on delete cascade,
foreign key (isbn_FK)
	references literature (isbn)
	on update cascade
	on delete cascade
);