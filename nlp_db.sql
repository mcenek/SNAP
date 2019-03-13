-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 13, 2019 at 01:41 PM
-- Server version: 5.7.25-0ubuntu0.16.04.2
-- PHP Version: 7.0.32-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nlp_db`
--
CREATE DATABASE IF NOT EXISTS `nlp_db` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `nlp_db`;

-- --------------------------------------------------------

--
-- Table structure for table `projects`
--

DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects` (
  `project_id` int(16) NOT NULL,
  `name` varchar(28) COLLATE utf8_bin NOT NULL,
  `user_id` int(100) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `projects`
--

INSERT INTO `projects` (`project_id`, `name`, `user_id`, `created_on`) VALUES
(86, 'MobyDick', 80, '2019-01-17 22:07:25'),
(87, 'Test', 80, '2019-02-14 20:13:39'),
(88, 'SenateHearings', 80, '2019-03-04 22:27:05');

-- --------------------------------------------------------

--
-- Table structure for table `tokens`
--

DROP TABLE IF EXISTS `tokens`;
CREATE TABLE `tokens` (
  `id` int(100) NOT NULL,
  `token` varchar(255) COLLATE utf8_bin NOT NULL,
  `user_id` int(100) NOT NULL,
  `created` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `tokens`
--

INSERT INTO `tokens` (`id`, `token`, `user_id`, `created`) VALUES
(80, 'eee17c10bd8d5b904a41f0c8a5a5af', 76, '2019-01-17');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(100) NOT NULL,
  `email` varchar(50) COLLATE utf8_bin NOT NULL,
  `password` varchar(100) COLLATE utf8_bin NOT NULL,
  `firstName` varchar(50) COLLATE utf8_bin NOT NULL,
  `lastName` varchar(50) COLLATE utf8_bin NOT NULL,
  `role` varchar(10) COLLATE utf8_bin NOT NULL,
  `last_login` varchar(50) COLLATE utf8_bin NOT NULL,
  `status` varchar(10) COLLATE utf8_bin NOT NULL DEFAULT 'pending',
  `file_dir` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `use_freq` tinyint(1) DEFAULT '0',
  `freq_lower_bound` double DEFAULT '25',
  `freq_upper_bound` double DEFAULT '5',
  `sliding_window` tinyint(1) DEFAULT '3',
  `date_range` int(11) DEFAULT '91',
  `skew_x` float NOT NULL DEFAULT '1',
  `skew_y` float NOT NULL DEFAULT '1',
  `skew_z` float NOT NULL DEFAULT '1',
  `shape` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT 'hybrid',
  `layout` int(11) NOT NULL DEFAULT '0',
  `mod_resolution` double NOT NULL DEFAULT '0.4',
  `project_id` int(16) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `firstName`, `lastName`, `role`, `last_login`, `status`, `file_dir`, `use_freq`, `freq_lower_bound`, `freq_upper_bound`, `sliding_window`, `date_range`, `skew_x`, `skew_y`, `skew_z`, `shape`, `layout`, `mod_resolution`, `project_id`) VALUES
(80, 'cenek@up.edu', '$2y$10$HplA1/TD4i85zTOfJikVBOXBeWJ.Pn35r.V46XTmkZeIcEcQnw5IS', 'TestUser', 'TestUser', 'subscriber', '2019-03-08 11:32:14 AM', 'approved', '/var/www/html/SNAP/cenek@up.edu/SenateHearings', 1, 0.06, 0.6, 3, 8, 1, 1, 1, 'hybrid', 0, 1, 88);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`project_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `tokens`
--
ALTER TABLE `tokens`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `file_dir` (`file_dir`),
  ADD KEY `project_id` (`project_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `projects`
--
ALTER TABLE `projects`
  MODIFY `project_id` int(16) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=89;
--
-- AUTO_INCREMENT for table `tokens`
--
ALTER TABLE `tokens`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=81;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `projects`
--
ALTER TABLE `projects`
  ADD CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `project_id_fk` FOREIGN KEY (`project_id`) REFERENCES `projects` (`project_id`) ON DELETE SET NULL;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
