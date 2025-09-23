-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 04, 2025 at 06:32 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `summer`
--

-- --------------------------------------------------------

--
-- Table structure for table `article`
--

CREATE TABLE `article` (
  `article_id` bigint(20) NOT NULL,
  `added_by` varchar(255) DEFAULT NULL,
  `alert_before_datelimit_number` int(11) DEFAULT NULL,
  `alert_before_datelimit_unit` enum('DAYS','MONTHS','WEEKS','YEARS') DEFAULT NULL,
  `code_article` varchar(255) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `datelimit_number` int(11) DEFAULT NULL,
  `datelimit_unit` enum('DAYS','MONTHS','WEEKS','YEARS') DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `etagere` varchar(255) DEFAULT NULL,
  `etat` enum('CommandeEnCours','Disponible','PartiellementDisponible','Reserve','Rupture') NOT NULL,
  `fast_sales_threshold` int(11) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `medium_sales_threshold` int(11) DEFAULT NULL,
  `minmum_stock` int(11) NOT NULL,
  `movement_timeframe` enum('MONTH','WEEK','YEAR') DEFAULT NULL,
  `qte` int(11) NOT NULL,
  `reserved_by_who` varchar(255) DEFAULT NULL,
  `reserved_to_who` varchar(255) DEFAULT NULL,
  `um` varchar(255) DEFAULT NULL,
  `unite` varchar(255) DEFAULT NULL,
  `role` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `article_batch`
--

CREATE TABLE `article_batch` (
  `id` bigint(20) NOT NULL,
  `expiry_alert` date DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `article_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `article_movement`
--

CREATE TABLE `article_movement` (
  `movement_id` bigint(20) NOT NULL,
  `client` varchar(255) DEFAULT NULL,
  `client_activity_domain` varchar(255) DEFAULT NULL,
  `client_zone` varchar(255) DEFAULT NULL,
  `from_location` varchar(255) DEFAULT NULL,
  `prise_en_charge` varchar(255) DEFAULT NULL,
  `quantity_change` int(11) NOT NULL,
  `reason` enum('Bought','ChangeDepo','CommandeEnCours','Poubelle','Sold') NOT NULL,
  `recorded_by` varchar(255) DEFAULT NULL,
  `reserved_by` varchar(255) DEFAULT NULL,
  `reserved_to` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `to_location` varchar(255) DEFAULT NULL,
  `article_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `commande_articlemv`
--

CREATE TABLE `commande_articlemv` (
  `id` bigint(20) NOT NULL,
  `original_movement_id` bigint(20) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `reserved_by` varchar(255) DEFAULT NULL,
  `reserved_to` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `article_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `cin` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` enum('Accepted','Refused','Waiting') NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `role` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `cin`, `email`, `password`, `status`, `username`, `role`) VALUES
(1, '12345678', 'stalzrka@gmail.com', '123', 'Accepted', 'Admin', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`id`, `name`) VALUES
(1, 'Admin'),
(2, 'Chef de coupe-soudure'),
(3, 'Chef de robotique'),
(4, 'Chef de traitement de surface'),
(5, 'Chef de process-maintenance');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `article`
--
ALTER TABLE `article`
  ADD PRIMARY KEY (`article_id`),
  ADD KEY `FKdsw5pbdgdyd6j4ddvo39eqqli` (`role`);

--
-- Indexes for table `article_batch`
--
ALTER TABLE `article_batch`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKh9akxlv4ukxbwb1b2lsmday2t` (`article_id`);

--
-- Indexes for table `article_movement`
--
ALTER TABLE `article_movement`
  ADD PRIMARY KEY (`movement_id`),
  ADD KEY `FKor9lh6hsidvqoilol0xngkf2j` (`article_id`);

--
-- Indexes for table `commande_articlemv`
--
ALTER TABLE `commande_articlemv`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKfdx0aeuw31sjiklibw01xqoex` (`article_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `FK4ck7eairsdxa7mynqxua798w4` (`role`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `article`
--
ALTER TABLE `article`
  MODIFY `article_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `article_batch`
--
ALTER TABLE `article_batch`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `article_movement`
--
ALTER TABLE `article_movement`
  MODIFY `movement_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `commande_articlemv`
--
ALTER TABLE `commande_articlemv`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `user_role`
--
ALTER TABLE `user_role`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `article`
--
ALTER TABLE `article`
  ADD CONSTRAINT `FKdsw5pbdgdyd6j4ddvo39eqqli` FOREIGN KEY (`role`) REFERENCES `user_role` (`id`);

--
-- Constraints for table `article_batch`
--
ALTER TABLE `article_batch`
  ADD CONSTRAINT `FKh9akxlv4ukxbwb1b2lsmday2t` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE CASCADE;

--
-- Constraints for table `article_movement`
--
ALTER TABLE `article_movement`
  ADD CONSTRAINT `FKor9lh6hsidvqoilol0xngkf2j` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`);

--
-- Constraints for table `commande_articlemv`
--
ALTER TABLE `commande_articlemv`
  ADD CONSTRAINT `FKfdx0aeuw31sjiklibw01xqoex` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE CASCADE;

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FK4ck7eairsdxa7mynqxua798w4` FOREIGN KEY (`role`) REFERENCES `user_role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
