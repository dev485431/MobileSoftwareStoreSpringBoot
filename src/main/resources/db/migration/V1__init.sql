CREATE TABLE `categories` (
  `id` int(11) AUTO_INCREMENT PRIMARY KEY NOT NULL ,
  `name` varchar(150) NOT NULL,
  `description` varchar(250) NOT NULL
);

CREATE TABLE `statistics` (
  `id` int(11) AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `time_uploaded` timestamp NOT NULL,
  `downloads` bigint(20) NOT NULL
);

CREATE TABLE `programs` (
  `id` int(11) AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `name` varchar(200)NOT NULL,
  `description` varchar(250) NOT NULL,
  `img128` varchar(150) DEFAULT NULL,
  `img512` varchar(150) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `statistics_id` int(11) NOT NULL,
  FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  FOREIGN KEY (`statistics_id`) REFERENCES `statistics` (`id`)
);


CREATE TABLE `ratings` (
  `id` int(11) AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `rating` float DEFAULT NULL,
  `statistics_id` int(11) NOT NULL,
  FOREIGN KEY (`statistics_id`) REFERENCES `statistics` (`id`)
);

CREATE TABLE `users` (
  `id` int(11) AUTO_INCREMENT PRIMARY KEY NOT NULL,
  `username` varchar(60) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `authority` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL
);
