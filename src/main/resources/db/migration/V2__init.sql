INSERT INTO `categories` (`id`, `name`, `description`) VALUES
(null, 'Games', 'Gaming software'),
(null, 'Education', 'Educational software'),
(null, 'Multimedia', 'Multimedia'),
(null, 'Productivity', 'Productivity'),
(null, 'Tools', 'Tools'),
(null, 'Health', 'Health'),
(null, 'Lifestyle', 'Lifestyle');

INSERT INTO `users` (`id`, `username`, `password`, `email`, `authority`, `enabled`) VALUES
(null, 'dev', '$2a$10$.gxm39Wpqx8Rrz/5nMHh4u7lfGkRX9Ny3FfkDvbQuHJKEIbxNIkCq', 'dev@dev.pl', 'ROLE_DEVELOPER', 1),
(null, 'admin', '$2a$10$m1fQb64Ga1T3xosqzh601Ok/105Ir6fwLk5wr.vuif5Xbs2BqXXce', 'admin@admin.pl', 'ROLE_ADMIN', 1),
(null, 'user', '$2a$10$n2ZwN7dQIzIjBsqsdRjMvO5OEGjoe4xXNeXZcrtAH3WYRRjlSuI5i', 'user@user.pl', 'ROLE_USER', 1);
