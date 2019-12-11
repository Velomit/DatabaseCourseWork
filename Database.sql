-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия сервера:               10.3.13-MariaDB-log - mariadb.org binary distribution
-- Операционная система:         Win64
-- HeidiSQL Версия:              10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Дамп структуры базы данных telephone_company
CREATE DATABASE IF NOT EXISTS `telephone_company` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `telephone_company`;

-- Дамп структуры для таблица telephone_company.calls
CREATE TABLE IF NOT EXISTS `calls` (
  `call_id` int(11) NOT NULL AUTO_INCREMENT,
  `dir_id` int(11) NOT NULL,
  `call_date` date NOT NULL,
  `call_length` time NOT NULL,
  PRIMARY KEY (`call_id`),
  KEY `dc` (`dir_id`),
  CONSTRAINT `dc` FOREIGN KEY (`dir_id`) REFERENCES `direction` (`dir_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.calls: ~13 rows (приблизительно)
/*!40000 ALTER TABLE `calls` DISABLE KEYS */;
INSERT INTO `calls` (`call_id`, `dir_id`, `call_date`, `call_length`) VALUES
	(1, 1, '2019-12-05', '00:01:00'),
	(2, 2, '2019-12-04', '00:01:30'),
	(3, 5, '2019-12-04', '01:00:11'),
	(4, 3, '2019-12-04', '00:01:05'),
	(5, 4, '2019-12-04', '00:05:50'),
	(6, 1, '2019-12-03', '00:00:51'),
	(7, 3, '2019-12-03', '00:10:15'),
	(8, 2, '2019-12-02', '00:02:47'),
	(9, 1, '2019-12-02', '00:25:11'),
	(10, 1, '2019-12-01', '00:03:30'),
	(53, 4, '2019-08-22', '00:11:23'),
	(54, 2, '2019-08-22', '00:15:55'),
	(55, 1, '2006-04-12', '01:06:03'),
	(59, 4, '2019-06-14', '00:59:13');
/*!40000 ALTER TABLE `calls` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.direction
CREATE TABLE IF NOT EXISTS `direction` (
  `dir_id` int(11) NOT NULL AUTO_INCREMENT,
  `cost` float NOT NULL,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`dir_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.direction: ~5 rows (приблизительно)
/*!40000 ALTER TABLE `direction` DISABLE KEYS */;
INSERT INTO `direction` (`dir_id`, `cost`, `name`) VALUES
	(1, 100, 'Киев'),
	(2, 50, 'Львов'),
	(3, 75, 'Ровно'),
	(4, 60, 'Черновцы'),
	(5, 40, 'Николаев');
/*!40000 ALTER TABLE `direction` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.journal
CREATE TABLE IF NOT EXISTS `journal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datetime` datetime NOT NULL,
  `data` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.journal: ~60 rows (приблизительно)
/*!40000 ALTER TABLE `journal` DISABLE KEYS */;
INSERT INTO `journal` (`id`, `datetime`, `data`) VALUES
	(2, '2019-12-08 21:40:54', 'Пользователь с логином Vova добавил звонок длительностью 01:06:03 и датой 2006-04-12 пользователю с логином Kolya'),
	(3, '2019-12-08 21:41:58', 'Пользователь с логином Vova добавил платёж суммой 100 и датой 2019-06-12 пользователю с логином Vanya'),
	(4, '2019-12-08 21:42:23', 'Пользователь с логином Vova добавил льготу \'АТО\' пользователю с логином Vova'),
	(6, '2019-12-08 22:22:32', 'Администратор с логином Velomit обновил данные пользователя Имечко Фамилия с логином null'),
	(7, '2019-12-08 22:23:03', 'Пользователь с логином Velomit удалил абонента Bvmtwt Фамилия с логином null'),
	(8, '2019-12-08 22:24:17', 'Пользователь с логином Vanya добавил звонок длительностью 00:00:59 и датой 2019-05-20 пользователю с логином Boris'),
	(9, '2019-12-08 22:24:41', 'Пользователь с логином Vanya обновил данные о звонке длительностью 00:00:59 датой 2019-05-20 и направлением Львов'),
	(10, '2019-12-08 22:24:53', 'Пользователь с логином Vanya добавил звонок длительностью 00:00:59 и датой 2019-05-20 пользователю с логином Boris'),
	(11, '2019-12-08 22:25:00', 'Пользователь с логином Vanya удалил звонок с датой2019-05-20 и направлением Львов'),
	(12, '2019-12-08 22:31:40', 'Пользователь с логином Vanya добавил платёж суммой 10000 и датой 2019-11-13 пользователю с логином Boris'),
	(13, '2019-12-08 22:31:50', 'Пользователь с логином Vanya обновил данные платежы суммой 10000.0 и датой 2019-11-13 у пользователя с логином Boris'),
	(14, '2019-12-08 22:31:54', 'Пользователь с логином Vanya удалил платёж суммой 100000.0 и датой 2019-11-13 у пользователя с логином Boris'),
	(15, '2019-12-08 22:32:11', 'Пользователь с логином Vanya добавил льготу \'АТО\' пользователю с логином Boris'),
	(16, '2019-12-08 22:32:13', 'Пользователь с логином Vanya удалил льготу \'АТО\' у пользователя с логином Boris'),
	(17, '2019-12-09 11:47:26', 'Пользователь с логином Vova добавил тариф \'Ultra\' со стоимостью 10.0'),
	(18, '2019-12-09 11:49:42', 'Пользователь с логином Vova добавил тариф \'кря\' со стоимостью 1.0'),
	(19, '2019-12-09 12:00:07', 'Пользователь с логином Vova удалил тариф \'кря\''),
	(20, '2019-12-09 12:05:48', 'Пользователь с логином Vova обновил данные тарифа \'Comfort\''),
	(21, '2019-12-09 12:09:11', 'Пользователь с логином Vova обновил данные тарифа \'Comfort\''),
	(22, '2019-12-09 12:10:06', 'Пользователь с логином Vova обновил данные тарифа \'Comfort\''),
	(23, '2019-12-09 13:03:56', 'Пользователь с логином Vova обновил данные тарифа \'Comfort\''),
	(24, '2019-12-09 13:04:10', 'Пользователь с логином Vova обновил данные тарифа \'Черновцы\''),
	(25, '2019-12-09 13:45:04', 'Пользователь с логином Vova обновил данные льготы \'Сирота\''),
	(26, '2019-12-09 13:45:50', 'Пользователь с логином Vova обновил данные льготы \'Сирота\''),
	(27, '2019-12-09 13:50:21', 'Пользователь с логином Vova добавил льготу \'Тестовая льгота\' со скидкой 1.0'),
	(28, '2019-12-09 13:50:39', 'Пользователь с логином Vova добавил льготу \'Тестовая льгота\' пользователю с логином Velomit'),
	(29, '2019-12-09 13:51:00', 'Пользователь с логином Vova удалил льготу \'Тестовая льгота\' у пользователя с логином Velomit'),
	(30, '2019-12-09 19:44:15', 'Пользователь с логином Vova добавил абонента Владислав Стрелецкий с логином Vlad и тарифомSuper'),
	(31, '2019-12-09 19:54:32', 'Пользователь с логином Vova обновил данные абонента Владислав Стрелецкий с логином null'),
	(32, '2019-12-09 19:59:52', 'Пользователь с логином Vova обновил данные абонента Владислав Стрелецкий с логином null'),
	(33, '2019-12-09 20:02:26', 'Пользователь с логином Vova обновил данные абонента Владислав Стрелецкий'),
	(34, '2019-12-10 20:19:02', 'Пользователь с логином Vova добавил платёж суммой 100 и датой 2018-09-17 пользователю с логином Bogdan'),
	(35, '2019-12-10 20:33:02', 'Пользователь с логином Vova добавил платёж суммой 50 и датой 2019-09-23 пользователю с логином Bogdan'),
	(36, '2019-12-10 21:46:20', 'Пользователь с логином Vova обновил данные абонента Николай Мандзяк'),
	(37, '2019-12-10 21:47:10', 'Пользователь с логином Vova добавил абонента Анастасия Богаченко с логином Pusya и тарифом Best'),
	(38, '2019-12-10 21:47:18', 'Пользователь с логином Vova обновил данные абонента Анастасия Богаченко'),
	(39, '2019-12-10 21:48:29', 'Пользователь с логином Vova добавил звонок длительностью 00:04:56 и датой 2017-04-13 пользователю с логином Pusya'),
	(40, '2019-12-10 21:48:37', 'Пользователь с логином Vova обновил данные о звонке длительностью 00:04:56 датой 2017-04-13 и направлением Киев'),
	(41, '2019-12-10 21:50:01', 'Пользователь с логином Vova добавил платёж суммой 2000 и датой 2019-11-23 пользователю с логином Pusya'),
	(42, '2019-12-10 21:50:25', 'Пользователь с логином Vova обновил данные платежа суммой 2000.0 и датой 2019-11-23 у пользователя с логином Pusya'),
	(43, '2019-12-10 21:50:55', 'Пользователь с логином Vova добавил льготу \'АТО\' пользователю с логином Pusya'),
	(44, '2019-12-10 21:50:59', 'Пользователь с логином Vova удалил льготу \'АТО\' у пользователя с логином Pusya'),
	(45, '2019-12-10 21:51:03', 'Пользователь с логином Vova добавил льготу \'АТО\' пользователю с логином Pusya'),
	(46, '2019-12-10 21:52:24', 'Пользователь с логином Vova удалил звонок с датой 2017-04-13 и направлением Киев'),
	(47, '2019-12-10 21:54:35', 'Администратор с логином Velomit обновил данные пользователя Анастасия Богаченко с логином null'),
	(48, '2019-12-10 22:05:12', 'Пользователь с логином Vova добавил платёж суммой 100 и датой 2019-08-19 пользователю с логином Vlad'),
	(49, '2019-12-10 22:05:23', 'Пользователь с логином Vova добавил платёж суммой 110 и датой 2019-08-18 пользователю с логином Vlad'),
	(50, '2019-12-10 22:06:22', 'Пользователь с логином Vova добавил платёж суммой 75.0 и датой 2019-06-18 пользователю с логином Vlad'),
	(51, '2019-12-10 22:07:31', 'Пользователь с логином Vova обновил данные платежа суммой 2050.0 и датой 2019-11-23 у пользователя с логином Pusya'),
	(52, '2019-12-10 22:07:36', 'Пользователь с логином Vova обновил данные платежа суммой 2050.0 и датой 2019-11-23 у пользователя с логином Pusya'),
	(53, '2019-12-10 22:10:17', 'Пользователь с логином Vova добавил платёж суммой 2000 и датой 2019-06-17 пользователю с логином Vlad'),
	(54, '2019-12-10 22:10:40', 'Пользователь с логином Vova добавил платёж суммой 2000 и датой 2019-06-17 пользователю с логином Pusya'),
	(55, '2019-12-10 22:11:02', 'Пользователь с логином Vova удалил платёж суммой 2000.0 и датой 2019-06-17 у пользователя с логином Pusya'),
	(56, '2019-12-10 22:11:56', 'Пользователь с логином Vova добавил платёж суммой 2000 и датой 2019-11-23 пользователю с логином Max'),
	(57, '2019-12-10 22:12:31', 'Пользователь с логином Vova удалил платёж суммой 2000.0 и датой 2019-11-23 у пользователя с логином Max'),
	(58, '2019-12-10 22:16:55', 'Пользователь с логином Vova добавил звонок длительностью 00:59:13 и датой 2019-06-14 пользователю с логином Pusya'),
	(59, '2019-12-10 22:17:45', 'Пользователь с логином Vova добавил платёж суммой 0 и датой 2020-01-01 пользователю с логином Velomit'),
	(60, '2019-12-11 21:44:52', 'Пользователь с логином Velomit удалил абонента Анастасия Богаченко'),
	(61, '2019-12-11 22:10:06', 'Пользователь с логином Vova обновил данные платежа суммой 0.0 и датой 2020-01-01 у пользователя с логином Velomit'),
	(62, '2019-12-11 22:10:10', 'Пользователь с логином Vova обновил данные платежа суммой 0.0 и датой 2020-01-01 у пользователя с логином Velomit'),
	(63, '2019-12-11 22:10:16', 'Пользователь с логином Vova обновил данные платежа суммой 10.0 и датой 2020-01-01 у пользователя с логином Velomit'),
	(64, '2019-12-11 22:10:26', 'Пользователь с логином Vova обновил данные платежа суммой 100.0 и датой 2020-01-01 у пользователя с логином Velomit');
/*!40000 ALTER TABLE `journal` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.payment
CREATE TABLE IF NOT EXISTS `payment` (
  `pay_id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_date` date NOT NULL,
  `amount` float NOT NULL,
  `sub_id` int(11) NOT NULL,
  PRIMARY KEY (`pay_id`),
  KEY `ps` (`sub_id`),
  CONSTRAINT `ps` FOREIGN KEY (`sub_id`) REFERENCES `subscriber` (`sub_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.payment: ~18 rows (приблизительно)
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` (`pay_id`, `pay_date`, `amount`, `sub_id`) VALUES
	(1, '2019-12-05', 100, 2),
	(2, '2019-12-05', 500, 5),
	(3, '2019-12-04', 450, 8),
	(4, '2019-12-04', 1000, 4),
	(5, '2019-12-03', 50, 9),
	(6, '2019-12-03', 300, 3),
	(8, '2019-12-02', 200, 7),
	(9, '2019-12-01', 150, 3),
	(10, '2019-12-01', 700, 9),
	(11, '2019-09-17', 150, 8),
	(12, '2019-06-12', 100, 4),
	(14, '2018-09-17', 100, 8),
	(15, '2019-09-23', 50, 8),
	(17, '2019-08-19', 100, 41),
	(18, '2019-08-18', 110, 41),
	(19, '2019-06-18', 75, 41),
	(20, '2019-06-17', 2000, 41),
	(23, '2019-01-01', 100, 1);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.privilege
CREATE TABLE IF NOT EXISTS `privilege` (
  `priv_id` int(11) NOT NULL AUTO_INCREMENT,
  `priv_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `discount` float NOT NULL DEFAULT 0 CHECK (`discount` between 0 and 1),
  PRIMARY KEY (`priv_id`),
  UNIQUE KEY `priv_name` (`priv_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.privilege: ~4 rows (приблизительно)
/*!40000 ALTER TABLE `privilege` DISABLE KEYS */;
INSERT INTO `privilege` (`priv_id`, `priv_name`, `discount`) VALUES
	(1, 'Инвалид', 0.5),
	(2, 'АТО', 0.1),
	(3, 'Сирота', 0.15);
/*!40000 ALTER TABLE `privilege` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.subscriber
CREATE TABLE IF NOT EXISTS `subscriber` (
  `sub_id` int(11) NOT NULL AUTO_INCREMENT,
  `tar_id` int(11) NOT NULL DEFAULT 4,
  `login` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `password` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Subscriber',
  `first_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `account_balance` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`sub_id`),
  UNIQUE KEY `login` (`login`),
  KEY `st` (`tar_id`),
  CONSTRAINT `st` FOREIGN KEY (`tar_id`) REFERENCES `tariff` (`tar_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.subscriber: ~11 rows (приблизительно)
/*!40000 ALTER TABLE `subscriber` DISABLE KEYS */;
INSERT INTO `subscriber` (`sub_id`, `tar_id`, `login`, `password`, `type`, `first_name`, `last_name`, `account_balance`) VALUES
	(1, 1, 'Velomit', '1', 'Admin', 'Дмитрий', 'Уланов', 1000),
	(2, 2, 'Vova', '1', 'Employee', 'Владимир', 'Пилипенко', 500),
	(3, 4, 'Kolya', '1', 'Employee', 'Николай', 'Мандзяк', 100),
	(4, 2, 'Vanya', '1', 'Employee', 'Иван', 'Кучеренко', 475),
	(5, 3, 'Boris', '1', 'Employee', 'Борис', 'Дротенко', 900),
	(6, 4, 'Max', '1', 'Subscriber', 'Максим', 'Ближников', 300),
	(7, 2, 'Andrey', '1', 'Subscriber', 'Андрей', 'Варзарь', 275),
	(8, 4, 'Bogdan', '1', 'Subscriber', 'Богдан', 'Осняков', 150),
	(9, 4, 'Ilya', '1', 'Subscriber', 'Илья', 'Максименко', 800),
	(10, 3, 'Denis', '1', 'Subscriber', 'Денис', 'Заплаткин', 400),
	(41, 2, 'Vlad', '1', 'Subscriber', 'Владислав', 'Стрелецкий', 228);
/*!40000 ALTER TABLE `subscriber` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.sub_calls
CREATE TABLE IF NOT EXISTS `sub_calls` (
  `sub_call_id` int(11) NOT NULL AUTO_INCREMENT,
  `sub_id` int(11) NOT NULL,
  `call_id` int(11) NOT NULL,
  PRIMARY KEY (`sub_call_id`),
  KEY `scs` (`sub_id`),
  KEY `scc` (`call_id`),
  CONSTRAINT `scc` FOREIGN KEY (`call_id`) REFERENCES `calls` (`call_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `scs` FOREIGN KEY (`sub_id`) REFERENCES `subscriber` (`sub_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.sub_calls: ~13 rows (приблизительно)
/*!40000 ALTER TABLE `sub_calls` DISABLE KEYS */;
INSERT INTO `sub_calls` (`sub_call_id`, `sub_id`, `call_id`) VALUES
	(1, 1, 1),
	(2, 1, 2),
	(3, 1, 3),
	(4, 1, 4),
	(5, 1, 5),
	(6, 3, 10),
	(7, 3, 8),
	(8, 3, 5),
	(9, 4, 6),
	(10, 6, 7),
	(24, 6, 53),
	(25, 6, 54),
	(26, 3, 55);
/*!40000 ALTER TABLE `sub_calls` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.sub_priv
CREATE TABLE IF NOT EXISTS `sub_priv` (
  `sub_priv_id` int(11) NOT NULL AUTO_INCREMENT,
  `sub_id` int(11) NOT NULL,
  `priv_id` int(11) NOT NULL,
  PRIMARY KEY (`sub_priv_id`),
  KEY `sps` (`sub_id`),
  KEY `spp` (`priv_id`),
  CONSTRAINT `spp` FOREIGN KEY (`priv_id`) REFERENCES `privilege` (`priv_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sps` FOREIGN KEY (`sub_id`) REFERENCES `subscriber` (`sub_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.sub_priv: ~7 rows (приблизительно)
/*!40000 ALTER TABLE `sub_priv` DISABLE KEYS */;
INSERT INTO `sub_priv` (`sub_priv_id`, `sub_id`, `priv_id`) VALUES
	(1, 1, 1),
	(2, 1, 2),
	(4, 3, 2),
	(5, 6, 2),
	(9, 10, 1),
	(10, 1, 3),
	(11, 7, 3),
	(12, 2, 2);
/*!40000 ALTER TABLE `sub_priv` ENABLE KEYS */;

-- Дамп структуры для таблица telephone_company.tariff
CREATE TABLE IF NOT EXISTS `tariff` (
  `tar_id` int(11) NOT NULL AUTO_INCREMENT,
  `tar_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tar_cost` float NOT NULL,
  `tar_info` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT 'None',
  PRIMARY KEY (`tar_id`),
  UNIQUE KEY `tar_name` (`tar_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Дамп данных таблицы telephone_company.tariff: ~5 rows (приблизительно)
/*!40000 ALTER TABLE `tariff` DISABLE KEYS */;
INSERT INTO `tariff` (`tar_id`, `tar_name`, `tar_cost`, `tar_info`) VALUES
	(1, 'Best', 0, 'Наибольшее кол-во звонков'),
	(2, 'Super', 50, 'Наибольшая сумма платежей'),
	(3, 'VIP', 1000, ' -'),
	(4, 'Comfort', 12, ' -');
/*!40000 ALTER TABLE `tariff` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
