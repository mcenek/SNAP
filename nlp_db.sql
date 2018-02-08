-- phpMyAdmin SQL Dump
-- version 4.4.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Feb 08, 2018 at 10:23 PM
-- Server version: 5.5.42
-- PHP Version: 5.6.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `nlp_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `projects`
--

CREATE TABLE `projects` (
  `project_id` int(16) NOT NULL,
  `name` varchar(28) COLLATE utf8_bin NOT NULL,
  `user_id` int(100) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `projects`
--

INSERT INTO `projects` (`project_id`, `name`, `user_id`, `created_on`) VALUES
(43, 'project_one', 54, '2017-05-14 09:13:15'),
(44, 'new', 54, '2017-05-14 09:35:17'),
(45, 'asdf', 54, '2017-05-14 09:46:39'),
(46, 'project_one', 55, '2017-05-15 21:14:01'),
(47, 'project_one', 56, '2017-05-15 21:14:52'),
(50, 'klkl', 33, '2017-05-17 22:14:53'),
(51, 'fads', 33, '2017-05-17 22:29:22'),
(53, 'qwer', 33, '2017-05-18 08:18:25'),
(54, 'new', 33, '2017-05-18 08:23:47'),
(55, 'new', 15, '2017-05-22 00:13:41'),
(56, 'ussenate', 15, '2017-06-30 20:02:10'),
(58, 'bear', 15, '2017-07-01 22:24:15'),
(59, 'abc', 15, '2017-07-01 22:26:55'),
(62, 'SenateHearings', 16, '2017-07-18 20:45:09'),
(63, 'project_one', 57, '2017-08-01 04:21:51'),
(64, 'project_one', 58, '2017-08-05 05:57:03'),
(65, 'project_one', 59, '2017-08-05 06:03:09'),
(66, 'project_one', 60, '2017-08-07 08:49:39'),
(67, 'poop', 60, '2017-08-07 08:50:05'),
(68, 'PublicationExample', 16, '2017-08-10 01:10:44'),
(69, 'project_one', 62, '2017-08-16 17:24:08'),
(70, 'project_one', 63, '2017-08-16 23:07:03'),
(71, 'project_one', 64, '2017-08-21 21:40:32'),
(73, 'AustraliaBroadcast', 16, '2017-08-22 01:48:23'),
(74, 'dummyTest', 16, '2017-08-22 23:13:07'),
(76, 'test', 15, '2017-08-23 00:32:12'),
(77, 'project_one', 68, '2017-09-24 20:54:01'),
(78, 'project_one', 69, '2017-09-24 20:54:22'),
(79, 'TestProject', 62, '2017-10-08 21:48:23'),
(80, 'dummyTest', 62, '2017-10-23 22:27:16'),
(81, 'MobyDick', 16, '2017-10-26 19:39:25'),
(82, 'newest', 69, '2017-10-29 20:23:09'),
(83, 'project_one', 72, '2018-01-24 01:35:40'),
(84, 'project_one', 73, '2018-01-24 01:36:52'),
(85, 'project_one', 74, '2018-02-05 01:31:46');

-- --------------------------------------------------------

--
-- Table structure for table `tokens`
--

CREATE TABLE `tokens` (
  `id` int(100) NOT NULL,
  `token` varchar(255) COLLATE utf8_bin NOT NULL,
  `user_id` int(100) NOT NULL,
  `created` date NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `tokens`
--

INSERT INTO `tokens` (`id`, `token`, `user_id`, `created`) VALUES
(7, '94ac29e1edefcb0d1a0de1dd7c0f56', 4, '2016-06-29'),
(11, '823c0ad3c4522d025373404cef5d6b', 8, '2016-07-13'),
(12, '1e03206e5c39f2264c905e4585425f', 9, '2016-08-15'),
(13, '6de23529d0f017b41e2d809183ea4b', 10, '2016-09-30'),
(14, '6457150ba1c180dffc9bd1097a5217', 11, '2016-10-15'),
(15, '2eb7edf73b6c088edbbd7bc4f3b4dd', 12, '2017-03-14'),
(16, '077629cc76601f408aebaa60c04523', 13, '2017-03-14'),
(17, '6c069bac9978101b0b44c6c7ce944a', 14, '2017-03-14'),
(18, 'ac7a593c9a6b0d60a841d8119aa5f1', 15, '2017-03-14'),
(19, 'b8f548139ca55da682bc85f5e11e21', 16, '2017-03-16'),
(20, '061874a4d458b7081524c34aa78b8e', 17, '2017-04-10'),
(21, '0742e67817797091c016b213743d5a', 18, '2017-04-10'),
(22, 'c35440ef28af9e802dd82e68ebafc8', 19, '2017-04-10'),
(23, '758a6c6be95bf69371bccbcf9bc367', 20, '2017-04-10'),
(24, '7386b3175d129b446d1c5e57efb94d', 21, '2017-04-10'),
(25, 'bc49270f8d405d18b3716d03149a71', 22, '2017-04-10'),
(26, '31a02d221c9d81ae91adcbf81a60ba', 23, '2017-04-10'),
(27, '6b0edd77416d59c7d049f282e7318c', 24, '2017-04-10'),
(28, '391d60a8d0f9027beeb9652c6d9509', 25, '2017-04-10'),
(29, '29215c49ac3b7edc4bfd710446138b', 26, '2017-04-10'),
(30, 'b2eef06daae1f4c4516b1532117477', 27, '2017-04-11'),
(31, 'bc1665b1a5e238fce9950c12d1a8e9', 28, '2017-04-11'),
(32, '7cab70badcc0d090bc99fe601ba354', 29, '2017-04-11'),
(33, 'ef6ea71f60d2410c8dafabafc4e882', 30, '2017-04-11'),
(34, 'b7cb34165f40cd5c8e379123dc700a', 31, '2017-04-11'),
(35, 'f22f287e2a8792bf29ba8e701e3c26', 32, '2017-04-17'),
(36, '6307e10f659c04972745516889cbbc', 33, '2017-05-01'),
(37, '5426b0a2ce9d59c2d55db0d2f87f11', 34, '2017-05-13'),
(38, '3d4951f85f39fd627671c5ef69126d', 35, '2017-05-13'),
(39, '64a773d952ceb806caf78e5a412b60', 36, '2017-05-13'),
(40, '28eb23a039134d766fb217c040b78e', 37, '2017-05-13'),
(41, 'f3448f850a91e2f7931f224cbf4da4', 38, '2017-05-14'),
(42, '3973cc8d68df86537349fa1f5d4893', 39, '2017-05-14'),
(43, '42b7b969a3def43bcbb01910e2a53f', 40, '2017-05-14'),
(44, '361c08ba28918201bcb90e180489c8', 41, '2017-05-14'),
(45, '732e8d7a3b184f4cc74eabee7266b7', 42, '2017-05-14'),
(46, '485223bc39feffab51b699db5f325e', 43, '2017-05-14'),
(47, '9cfa3c00e0e0d0487517cb9b1899a5', 44, '2017-05-14'),
(48, '78052af15cef9883a88d24d5dbce36', 45, '2017-05-14'),
(49, '892d80ef11f11fc780a5aaddf019e7', 46, '2017-05-14'),
(50, 'b96e044088e100b9e1ec404ca36d65', 47, '2017-05-14'),
(51, 'c71dc663bfb5d1043ab3cd7857ecf9', 48, '2017-05-14'),
(52, '702dbb41dd7d48a3a58c3b4b8e9ff6', 49, '2017-05-14'),
(53, '47ffce46f9d6225e00922b97bd9735', 50, '2017-05-14'),
(54, '25604f8089b35da58e6123c77829b3', 51, '2017-05-14'),
(55, '5fe5c6a13fdcfe007ee66c63da102d', 52, '2017-05-14'),
(56, 'eba1618a23dbd58af35b99bc02def9', 53, '2017-05-14'),
(57, 'c6716f96690c86076727c5f900ece0', 54, '2017-05-14'),
(58, 'fa1b0d0ed8666887d9e48866cfae62', 55, '2017-05-15'),
(59, '3081cb9300b52d7fc53a9ba5008767', 56, '2017-05-15'),
(60, 'ad516e65eed54788676cea5dc7f378', 57, '2017-08-01'),
(61, '7408ef4936b4f5a107983aecf41223', 58, '2017-08-05'),
(62, '89e425a339bc6373e56eb02ff72173', 59, '2017-08-05'),
(63, 'd70d1fdf9c6ee91485f02b72c5a708', 60, '2017-08-07'),
(64, '721bb0f63c858e0c92dba7809cd3be', 61, '2017-08-11'),
(65, '60e62994c89b848310ceca83b2cf88', 0, '2017-08-16'),
(66, 'e52408500c39346d7210313f9839a7', 62, '2017-08-16'),
(67, '2e8ff41096e68aad025eacba0a65e7', 63, '2017-08-17'),
(68, 'f3fe62f246b7dadf2bb7fde579db36', 64, '2017-08-21'),
(69, '7de9fec0ad5e4ad6805fa0544c1b11', 65, '2017-08-23'),
(70, 'eb22f82348e20727207cadef746413', 66, '2017-08-23'),
(71, '3547e47619e6dc185f7db5ebe430f9', 67, '2017-08-23'),
(72, '0e09ce1d3ca2315160d7a64dd2fd27', 68, '2017-09-24'),
(73, 'b342336e1dad086cb107c6b6bd5081', 69, '2017-09-24'),
(74, '646efc4a03bc1f02b580b35e00dae5', 70, '2017-10-20'),
(75, '19de7090358b2bee7c0951a9fd2eb6', 71, '2017-11-21'),
(76, 'a15602bb1443b30d34c9835ed2cf79', 72, '2018-01-24'),
(77, 'a65d00a74cce6b5a6f4bd6406fd072', 73, '2018-01-24'),
(78, '9bfdc9a95cc99a92b70151c299de58', 74, '2018-02-05');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

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
  `date_range` int(11) DEFAULT '91',
  `skew_x` float NOT NULL DEFAULT '1',
  `skew_y` float NOT NULL DEFAULT '1',
  `skew_z` float NOT NULL DEFAULT '1',
  `shape` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT 'hybrid',
  `layout` int(11) NOT NULL DEFAULT '0',
  `mod_resolution` double NOT NULL DEFAULT '0.4',
  `project_id` int(16) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `firstName`, `lastName`, `role`, `last_login`, `status`, `file_dir`, `use_freq`, `freq_lower_bound`, `freq_upper_bound`, `date_range`, `skew_x`, `skew_y`, `skew_z`, `shape`, `layout`, `mod_resolution`, `project_id`) VALUES
(4, 'ching@gmail.com', '$2y$10$khPMD7VjuxpH2vGyPwiLeONet3yrABElw8VgvhFNxTRr2JOvic026', 'Boyd', 'Ching', 'subscriber', '2016-06-29 06:07:18 AM', 'approved', '/Users/jessgrunblatt/users-uaa/ching@gmail.com', 0, 0.1, 0.9, 0, 0, 0, 0, '', 0, 0, NULL),
(8, 'byching@alaska.edu', '$2y$10$OIJlLw6m57OooQ3FVZjIr.Ldgb1euP6onfgO2amkRPNFYylr5fzNu', 'Boyd', 'Ching', 'subscriber', '2016-07-21 10:14:41 PM', 'approved', '/Users/jessgrunblatt/users-uaa/byching@alaska.edu', 0, 0.1, 0.9, 0, 0, 0, 0, '', 0, 0, NULL),
(9, 'loyster1@alaska.edu', '$2y$10$N6yjQm0Bv4oAYXDg04c33.qNVFRuOFBP802OAqjbW6cwPVQWRa1o6', 'Levi', 'Oyster', 'subscriber', '2017-07-20 11:00:30 PM', 'approved', '/Users/jessgrunblatt/users-uaa/loyster1@alaska.edu', 0, 0.1, 0.9, 12, 0, 0, 0, '', 0, 0, NULL),
(11, 'motion411@gmail.com', '$2y$10$paOhccdordpY7bdKVchlYOKfbjUU7rYa4th3EZldVOp8pWUfD3T6O', 'Levi', 'Oyster', 'subscriber', '2016-10-15 04:04:30 AM', 'approved', '/Users/jessgrunblatt/users-uaa/motion411@gmail.com', 0, 0.1, 0.9, 0, 0, 0, 0, '', 0, 0, NULL),
(15, 'peter@dadJokes.com', '$2y$10$xa01oz7Ljq8.hFKlh.OtO.9oDEJ.z7bJoO7FPEM5kZNGnsyv/X2h2', 'Peter', 'Peter', 'subscriber', '2017-08-23 04:56:51 AM', 'approved', '/Users/jessgrunblatt/users-uaa/peter@dadJokes.com/test', 0, 0.1, 0.9, 90, 2, 2, 10000, 'hybrid', 0, 0.4, 76),
(16, 'mcenek@alaska.edu', '$2y$10$oaTxT9Uey9UYL/yYxjunLujkDEEBNcfN5z4RpcXsdPXsfr1FcM2Im', 'Martin', 'Cenek', 'subscriber', '2018-01-26 03:06:01 AM', 'approved', '/Users/jessgrunblatt/users-uaa/mcenek@alaska.edu/dummyTest', 1, 0.05, 0.95, 1, 5, 5, 200, 'hybrid', 0, 0.75, 74),
(33, 'Mc@Donald.com', '$2y$10$vN46vxjgQ5Brm4icLSY.OeQ2Lfr1d16k.zqQ5/QINHDGzVaXDDOl.', 'Ronald', 'McDonald', 'subscriber', '2017-05-22 05:33:16 AM', 'approved', '/Users/jessgrunblatt/users-uaa/Mc@Donald.com/klkl', 0, 0.1, 0.9, 91, 1, 1, 80, 'hybrid', 0, 0.4, 50),
(54, 'bob@bob.com', '$2y$10$p4aaRFVfPv9HwMfqAQZqOevVUGCRBcATfzRSvUbETqK4F.eGHqVge', 'bob', 'bob', 'subscriber', '2017-05-14 11:43:48 AM', 'approved', '/Users/jessgrunblatt/users-uaa/bob@bob.com/new', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 44),
(55, 'newguya@safd.com', '$2y$10$DwHZx661nBvhRR7IyvwkhOduMUopcIcQnihqPqOgv3ec4iPnCUJEi', 'asdfag', 'gsd', 'subscriber', '2017-05-15 11:14:01 PM', 'approved', '/Users/jessgrunblatt/users-uaa/newguya@safd.com/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 46),
(56, 'pak.eric@gmail.com1', '$2y$10$2FQTycRvkmlaT6Yxu/bNI.iMDztIXQcXTQ82ZtSNGTbLTkkaBLinK', 'eric', 'pak', 'subscriber', '2017-05-15 11:15:05 PM', 'approved', '/Users/jessgrunblatt/users-uaa/pak.eric@gmail.com1/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 47),
(57, 'rowan.bulkow@gmail.com', '$2y$10$c0hPUuQHqifotnFFzBJiE..esMY17FRBAOqOL9ZOHBDFW6w0CzytG', 'rowan', 'bulkow', 'subscriber', '2017-08-01 06:22:45 AM', 'approved', '/Users/jessgrunblatt/users-uaa/rowan.bulkow@gmail.com/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 63),
(58, 'james@lown.com', '$2y$10$5QsC6eooxcqA5/oxIpdQ6.BYNg.pn8BAWmXDxHO..9ZYR58OYFlfG', 'james', 'lown', 'subscriber', '2017-08-05 07:57:03 AM', 'approved', '/Users/jessgrunblatt/users-uaa/james@lown.com/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 64),
(59, 'james@pak.com', '$2y$10$6mfogNMfbOvvbeO11iXb7eg7gp249IdsfxcWeajBziNemw8C1.aCe', 'James', 'Pak', 'subscriber', '2017-08-05 08:03:23 AM', 'approved', '/Users/jessgrunblatt/users-uaa/james@pak.com/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 65),
(60, 'snap@snap.com', '$2y$10$Eo1P3vhn5AvgKn9gvFRcdevKDo7sE0y/pLCKbpcZdAfOKbggDPSAS', 'snap', 'snap', 'subscriber', '2017-08-07 10:49:48 AM', 'approved', '/Users/jessgrunblatt/users-uaa/snap@snap.com/poop', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 67),
(61, 'jgrunbla@alaska.edu', '', 'Jess', 'Grunblatt', 'subscriber', '', 'pending', NULL, 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, NULL),
(62, 'rbulkow2@alaska.edu', '$2y$10$uZ8xjk0fMjKfaDwKZPZEM.VUCFs2BgksIvf/NOwagYWLgJUFhbjXC', 'Rowan', 'Bulkow', 'subscriber', '2018-02-08 10:21:49 PM', 'approved', '/Users/jessgrunblatt/users-uaa/rbulkow2@alaska.edu/dummyTest', 1, 0.1, 0.8, 91, 1, 1, 1, 'hybrid', 0, 0.4, 80),
(63, 'drcenek@gmail.com', '$2y$10$401QxlwsXMNTemH1cYKw2uOp3ttLbGHWFsO9HXq.inETV3DYF5Li6', 'Martin', 'MartinC', 'subscriber', '2017-08-20 02:28:35 AM', 'approved', '/Users/jessgrunblatt/users-uaa/drcenek@gmail.com/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 70),
(64, 'jess@gina.alaska.edu', '$2y$10$cKmCojCIjPxmQmROt91HWu1liOEmddleqA/MNNo5bDfhDIeXxbtaa', 'Jess', 'Grunblatt', 'subscriber', '2018-01-25 12:15:12 AM', 'approved', '/Users/jessgrunblatt/users-uaa/jess@gina.alaska.edu/project_one', 1, 0.1, 0.5, 91, 1, 1, 1, 'hybrid', 0, 0.4, 71),
(65, 'eric@test.com', '', 'eric', 'test', 'subscriber', '', 'pending', NULL, 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, NULL),
(66, 'this@test.com', '', 'this', 'test', 'subscriber', '', 'pending', NULL, 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, NULL),
(67, 'first@last.com', '', 'first', 'last', 'subscriber', '', 'pending', NULL, 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, NULL),
(68, 'drodriguez9@alaska.edu', '$2y$10$v7fp1zZ1rg3puuE.hiXqpuK/80ptxvGotUkaKnK6aL9KmX.ww10p6', 'David', 'Rodriguez', 'subscriber', '2017-09-24 10:54:19 PM', 'approved', '/Users/jessgrunblatt/users-uaa/drodriguez9@alaska.edu/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 77),
(69, 'josephf95@gmail.com', '$2y$10$fDCTw7Qtd2AHS8QcwXIlaehyNPv2tHQOlDpS1OaglHVG1R9op2Fem', 'Joe', 'Fiskeaux', 'subscriber', '2017-12-23 05:51:23 AM', 'approved', '/Users/jessgrunblatt/users-uaa/josephf95@gmail.com/project_one', 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, 78),
(70, 'howyouvegrown@email.net', '', 'Mackenzie', 'Zaleska', 'subscriber', '', 'pending', NULL, 0, 0.1, 0.9, 91, 1, 1, 1, 'hybrid', 0, 0.4, NULL),
(71, '', '', '', '', 'subscriber', '', 'pending', NULL, 0, 25, 5, 91, 1, 1, 1, 'hybrid', 0, 0.4, NULL),
(72, 'pereilly@alaska.edu', '$2y$10$qfJdI9ue.M4Ghs8dB.RqJeYzNB6f.isFm5a4fm90uDRTfbZ8269rK', 'Patricia', 'Reilly', 'subscriber', '2018-01-24 02:37:08 AM', 'approved', '/Users/jessgrunblatt/users-uaa/pereilly@alaska.edu/project_one', 0, 25, 5, 91, 1, 1, 1, 'hybrid', 0, 0.4, 83),
(73, 'ddonnally@alaska.edu', '$2y$10$VKWBi.K/Tg2VNvQRfXwrD.vFQLLqGIJPEtksG2uURWOW4RcComaUq', 'Dave', 'Donnally', 'subscriber', '2018-02-05 07:28:00 AM', 'approved', '/Users/jessgrunblatt/users-uaa/ddonnally@alaska.edu/project_one', 0, 25, 5, 91, 1, 1, 1, 'hybrid', 0, 0.4, 84),
(74, 'Triciaereilly@gmail.com', '$2y$10$ecXtlNheMOx9Xe0ZqLyZNuRYkpP9cuiOdMhqqsvIZpfNlh.aRqhJ2', 'Patricia', 'Reilly', 'subscriber', '2018-02-05 02:36:10 AM', 'approved', '/Users/jessgrunblatt/users-uaa/Triciaereilly@gmail.com/project_one', 0, 25, 5, 91, 1, 1, 1, 'hybrid', 0, 0.4, 85);

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
  MODIFY `project_id` int(16) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=86;
--
-- AUTO_INCREMENT for table `tokens`
--
ALTER TABLE `tokens`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=79;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=75;
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
