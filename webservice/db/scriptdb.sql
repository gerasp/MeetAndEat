

CREATE TABLE `meeting` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`title` varchar(255) NOT NULL,
	`location` varchar(255) NOT NULL,
	`datetime` DATETIME NOT NULL,
	`color` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`username` varchar(255) NOT NULL UNIQUE,
	`email` varchar(255) NOT NULL UNIQUE,
	`password` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `food` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`icon` varchar(255) NOT NULL,
	`description` varchar(255) NOT NULL,
	`amount` INT NOT NULL,
	`meeting_id` INT NOT NULL,
	`user_id` INT NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `message` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`content` varchar(255) NOT NULL,
	`timestamp` INT NOT NULL,    
	`meeting_id` INT NOT NULL,
	`user` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `user_meeting` (
	`user_id` INT NOT NULL,
	`meeting_id` INT NOT NULL,
	`isAdmin` BOOLEAN NOT NULL,
	`isAccepted` BOOLEAN NOT NULL,
	PRIMARY KEY (`user_id`,`meeting_id`)
);

CREATE TABLE `user_user` (
	`user1_id` INT NOT NULL,
	`user2_id` INT NOT NULL,
    `isAccepted` BOOLEAN NOT NULL,
	PRIMARY KEY (`user1_id`,`user2_id`)
);

ALTER TABLE `food` ADD CONSTRAINT `food_fk0` FOREIGN KEY (`meeting_id`) REFERENCES `meeting`(`id`) ON DELETE CASCADE;

ALTER TABLE `food` ADD CONSTRAINT `food_fk1` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE;

ALTER TABLE `message` ADD CONSTRAINT `message_fk0` FOREIGN KEY (`meeting_id`) REFERENCES `meeting`(`id`) ON DELETE CASCADE;

ALTER TABLE `user_meeting` ADD CONSTRAINT `user_meeting_fk0` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE;

ALTER TABLE `user_meeting` ADD CONSTRAINT `user_meeting_fk1` FOREIGN KEY (`meeting_id`) REFERENCES `meeting`(`id`) ON DELETE CASCADE;

ALTER TABLE `user_user` ADD CONSTRAINT `user_user_fk0` FOREIGN KEY (`user1_id`) REFERENCES `user`(`id`) ON DELETE CASCADE;

ALTER TABLE `user_user` ADD CONSTRAINT `user_user_fk1` FOREIGN KEY (`user2_id`) REFERENCES `user`(`id`) ON DELETE CASCADE;

CREATE INDEX index_datetime
ON meeting (datetime)