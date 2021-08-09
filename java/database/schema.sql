BEGIN TRANSACTION;
ROLLBACK;

DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS seq_user_id;

CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;


CREATE TABLE users (
	user_id int DEFAULT nextval('seq_user_id'::regclass) NOT NULL,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');


DROP TABLE IF EXISTS recipe_ingredient;
DROP TABLE IF EXISTS recipe_meal;
DROP TABLE IF EXISTS meal_plan_meal;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS meal;
DROP TABLE IF EXISTS meal_plan;


CREATE TABLE recipe (
        recipe_id serial primary key,
        user_id int not null,
        name varchar(255) not null unique,
        category varchar(255) not null,
        difficulty_level varchar(255) not null,
        prep_time_min int not null check (prep_time_min >= 0),
        cook_time_min int not null check (cook_time_min >= 0),
        serving_size int not null check (serving_size > 0),
        instructions text not null,
        date_created date not null,
        image_file_name text,
        CONSTRAINT fk_users_recipe_user_id foreign key (user_id) references users (user_id)
);

CREATE TABLE ingredient (
        ingredient_id serial primary key,
        user_id int not null,
        name varchar(255) not null unique,
        category varchar(255) not null,
        CONSTRAINT fk_users_ingredient_user_id foreign key (user_id) references users (user_id)
);

CREATE TABLE recipe_ingredient (
        recipe_id int not null,
        ingredient_id int not null,
        quantity decimal(5,2) not null check (quantity > 0),
        unit_measurement varchar(255) not null,
        
        CONSTRAINT pk_recipe_ingredient primary key (recipe_id, ingredient_id),
        CONSTRAINT fk_recipe_ingredient_recipe_id foreign key (recipe_id) references recipe (recipe_id),
        CONSTRAINT fk_recipe_ingredient_ingredient_id foreign key( ingredient_id) references ingredient (ingredient_id)
);

CREATE TABLE meal (
        meal_id serial primary key,
        user_id int not null,
        name varchar(255) not null unique,
        category varchar(255) not null,
        image_file_name text,
        CONSTRAINT fk_users_meal_user_id foreign key (user_id) references users (user_id)  
);

CREATE TABLE recipe_meal (
        recipe_id int not null,
        meal_id int not null,
        
        CONSTRAINT pk_recipe_meal primary key (recipe_id, meal_id),
        CONSTRAINT fk_recipe_meal_recipe_id foreign key (recipe_id) references recipe (recipe_id),
        CONSTRAINT fk_recipe_meal_meal_id foreign key (meal_id) references meal (meal_id)
);

CREATE TABLE meal_plan (
        meal_plan_id serial primary key,
        user_id int not null,
        name varchar(255) not null unique,
        description text,
        image_file_name text,
        CONSTRAINT fk_users_meal_plan_user_id foreign key (user_id) references users (user_id)  
);

CREATE TABLE meal_plan_meal (
        meal_plan_id int not null,
        meal_id int not null,
        
        CONSTRAINT pk_meal_plan_meal primary key (meal_plan_id, meal_id),
        CONSTRAINT fk_meal_plan_meal_meal_plan_id foreign key (meal_plan_id) references meal_plan (meal_plan_id),
        CONSTRAINT fk_meal_plan_meal_meal_id foreign key (meal_id) references meal (meal_id)
);

INSERT INTO ingredient (ingredient_id, user_id, name, category) VALUES (DEFAULT, 1, 'Sourdough Bread', 'Bread');
INSERT INTO ingredient (ingredient_id, user_id, name, category) VALUES (DEFAULT, 1, 'Unsalted Butter', 'Dairy');
INSERT INTO ingredient (ingredient_id, user_id, name, category) VALUES (DEFAULT, 1, 'Mayonnaise', 'Condiment');
INSERT INTO ingredient (ingredient_id, user_id, name, category) VALUES (DEFAULT, 1, 'Cheddar Cheese', 'Dairy');

INSERT INTO recipe (recipe_id, user_id, name, category, difficulty_level, prep_time_min, cook_time_min, serving_size, instructions, date_created, image_file_name) 
VALUES (DEFAULT, 1, 'Grilled Cheese', 'Entree', 'Easy', 5, 7, 1, 'On a cutting board, butter each piece of bread with butter on one side. 
Flip the bread over and spread each piece of bread with mayonnaise. 
Place the cheese on the buttered side of one piece of bread. Top it with the second piece of bread, mayonnaise side out. 
Heat a nonstick pan over medium low heat. 
Place the sandwich on the pan, mayonnaise side down. 
Cook for 3-4 minutes, until golden brown. 
Using a spatula, flip the sandwich over and continue cooking until golden brown, about 2-3 minutes.', '2021-07-31', 'grilled_cheese.jpg');

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (4, 13, 2, 'Slices');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (4, 14, 1.5, 'Tablespoons');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (4, 15, 1.5, 'Tablespoons');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (4, 16, 3, 'Slices');

COMMIT TRANSACTION;
