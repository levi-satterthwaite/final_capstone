BEGIN TRANSACTION;

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
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS ingredient;

CREATE TABLE recipe (
        recipe_id serial primary key,
        name varchar(255) not null,
        category varchar(255) not null,
        difficulty_level varchar(255) not null,
        prep_time_min int not null,
        cook_time_min int not null,
        serving_size int not null,
        instructions text not null,
        date_created date not null,
        image_file_name text
);

CREATE TABLE ingredient (
        ingredient_id serial primary key,
        name varchar(255) not null,
        category varchar(255) not null
);

CREATE TABLE recipe_ingredient (
        recipe_id int not null,
        ingredient_id int not null,
        quantity decimal(5,2) not null,
        unit_measurement varchar(255) not null,
        
        CONSTRAINT pk_recipe_ingredient primary key(recipe_id, ingredient_id),
        CONSTRAINT fk_recipe_ingredient_recipe_id foreign key(recipe_id) references recipe(recipe_id),
        CONSTRAINT fk_recipe_ingredient_ingredient_id foreign key(ingredient_id) references ingredient(ingredient_id)
);

INSERT INTO ingredient (ingredient_id, name, category) VALUES (DEFAULT, 'Sourdough Bread', 'Bread');
INSERT INTO ingredient (ingredient_id, name, category) VALUES (DEFAULT, 'Unsalted Butter', 'Dairy');
INSERT INTO ingredient (ingredient_id, name, category) VALUES (DEFAULT, 'Mayonnaise', 'Condiment');
INSERT INTO ingredient (ingredient_id, name, category) VALUES (DEFAULT, 'Cheddar Cheese', 'Dairy');

INSERT INTO recipe (recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, serving_size, instructions, date_created, image_file_name) 
VALUES (DEFAULT, 'Grilled Cheese', 'Comfort Food', 'Easy', 5, 7, 1, 'On a cutting board, butter each piece of bread with butter on one side. 
Flip the bread over and spread each piece of bread with mayonnaise. 
Place the cheese on the buttered side of one piece of bread. Top it with the second piece of bread, mayonnaise side out. 
Heat a nonstick pan over medium low heat. 
Place the sandwich on the pan, mayonnaise side down. 
Cook for 3-4 minutes, until golden brown. 
Using a spatula, flip the sandwich over and continue cooking until golden brown, about 2-3 minutes.', '2021-07-31', 'grilled_cheese.jpg');

INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (1, 1, 2, 'Slices');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (1, 2, 1.5, 'Tablespoons');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (1, 3, 1.5, 'Tablespoons');
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit_measurement) VALUES (1, 4, 3, 'Slices');

COMMIT TRANSACTION;
