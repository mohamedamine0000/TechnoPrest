-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 30, 2025 at 07:17 PM
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

--
-- Dumping data for table `article`
--

INSERT INTO `article` (`article_id`, `added_by`, `alert_before_datelimit_number`, `alert_before_datelimit_unit`, `code_article`, `date`, `datelimit_number`, `datelimit_unit`, `designation`, `etagere`, `etat`, `fast_sales_threshold`, `location`, `medium_sales_threshold`, `minmum_stock`, `movement_timeframe`, `qte`, `reserved_by_who`, `reserved_to_who`, `um`, `unite`, `role`) VALUES
(2, '12345678', 3, 'MONTHS', '1111', '2025-08-25 00:00:00.000000', 1, 'YEARS', 'Yogurt', 'A4', 'Disponible', 800, 'Tunis mall', 400, 30, 'YEAR', 210, '', '', 'kilo', 'pack', 2),
(3, '12345678', NULL, NULL, '1112', '2025-08-25 10:23:07.000000', NULL, NULL, 'T1', 'A3', 'Reserve', 500, 'Africa Mall', 100, 60, 'YEAR', 100, 'khaled', 'mohsin', 'um t1', 'K', 3),
(4, '11111111', NULL, NULL, 'ART-001', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Widget A', 'A1-01', 'Disponible', 300, 'Warehouse A', 100, 10, 'WEEK', 100, NULL, NULL, 'pcs', 'Unit A', 3),
(5, '11111111', 7, 'DAYS', 'ART-002', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Gadget B', 'A1-02', 'Disponible', 60, 'Warehouse A', 20, 5, 'MONTH', 600, NULL, NULL, 'kg', 'Unit B', 3),
(6, '11111111', 7, 'DAYS', 'ART-003', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Component C', 'A1-03', 'Disponible', 80, 'Warehouse A', 60, 20, 'YEAR', 305, NULL, NULL, 'boxes', 'Unit C', 3),
(7, '11111111', 7, 'DAYS', 'ART-004', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Part D', 'A1-04', 'Disponible', 600, 'Warehouse A', 250, 50, 'WEEK', 500, NULL, NULL, 'units', 'Unit D', 3),
(8, '33333333', 7, 'DAYS', 'ART-005', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Tool E', 'B2-01', 'Disponible', 80, 'Warehouse B', 50, 15, 'MONTH', 90, NULL, NULL, 'pcs', 'Unit E', 2),
(9, '33333333', 7, 'DAYS', 'ART-006', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Fixture F', 'B2-02', 'Disponible', 80, 'Warehouse B', 75, 10, 'WEEK', 120, NULL, NULL, 'units', 'Unit F', 2),
(10, '33333333', 7, 'DAYS', 'ART-007', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Material G', 'B2-03', 'Disponible', 300, 'Warehouse B', 150, 40, 'YEAR', 0, NULL, NULL, 'm', 'Unit G', 2),
(11, '33333333', 7, 'DAYS', 'ART-008', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Item H', 'B2-04', 'Disponible', 30, 'Warehouse B', 15, 5, 'MONTH', 25, NULL, NULL, 'units', 'Unit H', 2),
(12, '44444444', 7, 'DAYS', 'ART-009', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Component I', 'C3-01', 'Disponible', 80, 'Warehouse C', 50, 12, 'WEEK', 40, NULL, NULL, 'pcs', 'Unit I', 4),
(13, '44444444', 7, 'DAYS', 'ART-010', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Product J', 'C3-02', 'Disponible', 45, 'Warehouse C', 30, 8, 'MONTH', 8, NULL, NULL, 'units', 'Unit J', 4),
(14, '44444444', 7, 'DAYS', 'ART-011', '2025-08-28 00:00:00.000000', 30, 'DAYS', 'Supply K', 'C3-03', 'Disponible', 300, 'Warehouse C', 100, 25, 'YEAR', 200, NULL, NULL, 'kg', 'Unit K', 4),
(15, '55555555', NULL, NULL, 'ART-012', '2025-08-28 00:00:00.000000', NULL, NULL, 'Material L', 'D4-01', 'Disponible', 80, 'Warehouse D', 70, 15, 'WEEK', 175, NULL, NULL, 'pcs', 'Unit L', 3),
(16, '55555555', NULL, NULL, 'ART-013', '2025-08-28 00:00:00.000000', NULL, NULL, 'Part M', 'D4-02', 'Disponible', 700, 'Warehouse D', 125, 20, 'MONTH', 250, NULL, NULL, 'units', 'Unit M', 3),
(17, '55555555', NULL, NULL, 'ART-014', '2025-08-28 00:00:00.000000', NULL, NULL, 'Item N', 'D4-03', 'Disponible', 40, 'Warehouse D', 30, 10, 'YEAR', 50, NULL, NULL, 'kg', 'Unit N', 3),
(18, '55555555', NULL, NULL, 'ART-015', '2025-08-28 00:00:00.000000', NULL, NULL, 'Supply O', 'D4-04', 'Disponible', 50, 'Warehouse D', 40, 12, 'WEEK', 80, NULL, NULL, 'boxes', 'Unit O', 3),
(19, '66666666', NULL, NULL, 'ART-016', '2025-08-28 00:00:00.000000', NULL, NULL, 'Product P', 'E5-01', 'Disponible', 80, 'Warehouse E', 60, 20, 'MONTH', 120, NULL, NULL, 'pcs', 'Unit P', 2),
(20, '66666666', NULL, NULL, 'ART-017', '2025-08-28 00:00:00.000000', NULL, NULL, 'Component Q', 'E5-02', 'Disponible', 300, 'Warehouse E', 150, 50, 'WEEK', 300, NULL, NULL, 'units', 'Unit Q', 2),
(21, '66666666', NULL, NULL, 'ART-018', '2025-08-28 00:00:00.000000', NULL, NULL, 'Material R', 'E5-03', 'Disponible', 300, 'Warehouse E', 200, 60, 'YEAR', 400, NULL, NULL, 'm', 'Unit R', 2),
(22, '77777777', NULL, NULL, 'ART-019', '2025-08-28 00:00:00.000000', NULL, NULL, 'Item S', 'F6-01', 'Disponible', 60, 'Warehouse F', 35, 10, 'WEEK', 70, NULL, NULL, 'pcs', 'Unit S', 4),
(23, '77777777', NULL, NULL, 'ART-020', '2025-08-28 00:00:00.000000', NULL, NULL, 'Part T', 'F6-02', 'Disponible', 300, 'Warehouse F', 90, 25, 'MONTH', 957, NULL, NULL, 'kg', 'Unit T', 4),
(24, '77777777', NULL, NULL, 'ART-021', '2025-08-28 00:00:00.000000', NULL, NULL, 'Product U', 'F6-03', 'Disponible', 80, 'Warehouse F', 55, 18, 'YEAR', 110, NULL, NULL, 'units', 'Unit U', 4),
(25, '11111111', NULL, NULL, 'ART-022', '2025-08-28 00:00:00.000000', NULL, NULL, 'Supply V', 'A1-05', 'Disponible', 80, 'Warehouse A', 50, 15, 'WEEK', 65, NULL, NULL, 'pcs', 'Unit V', 3),
(26, '33333333', NULL, NULL, 'ART-023', '2025-08-28 00:00:00.000000', NULL, NULL, 'Tool W', 'B2-05', 'Disponible', 90, 'Warehouse B', 30, 10, 'MONTH', 40, NULL, NULL, 'units', 'Unit W', 2),
(27, '44444444', 1, 'DAYS', 'ART-B00', '2025-08-29 17:16:48.000000', 1, 'DAYS', 'tools', 'A4', 'Disponible', 900, 'Mall B', 400, 60, 'YEAR', 0, '', '', 'Um-V2', 'PCs', 4),
(28, '12345678', 1, 'DAYS', 'Ba-001', '2025-08-30 15:54:37.000000', 1, 'DAYS', 'Ba', 'A3', 'Disponible', 200, 'Mall B', 100, 30, 'MONTH', 290, '', '', 'Ki', 'PCs', 2);

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

--
-- Dumping data for table `article_batch`
--

INSERT INTO `article_batch` (`id`, `expiry_alert`, `expiry_date`, `purchase_date`, `quantity`, `article_id`) VALUES
(2, '2025-09-11', '2025-09-18', '2025-08-18', 50, 5),
(3, '2025-09-16', '2025-09-23', '2025-08-23', 75, 6),
(4, '2025-09-21', '2025-09-28', '2025-08-28', 120, 7),
(5, '2025-09-01', '2025-09-08', '2025-08-08', 30, 8),
(6, '2025-09-06', '2025-09-13', '2025-08-13', 90, 9),
(7, '2025-09-18', '2025-09-25', '2025-08-25', 45, 10),
(9, '2025-09-13', '2025-09-20', '2025-08-20', 90, 12),
(10, '2025-09-19', '2025-09-26', '2025-08-26', 150, 13),
(11, '2025-09-09', '2025-09-16', '2025-08-16', 60, 14),
(12, '2025-09-21', '2025-09-28', '2025-08-29', 6789, 13),
(15, '2025-09-22', '2025-09-29', '2025-08-30', 600, 5),
(16, '2025-09-03', '2025-09-04', '2025-08-30', 100, 2),
(17, '2025-09-22', '2025-09-29', '2025-08-30', 200, 11),
(18, '2025-09-03', '2025-09-04', '2025-08-30', 100, 2),
(19, '2025-09-03', '2025-09-04', '2025-08-30', 60, 2),
(21, '2025-09-22', '2025-09-29', '2025-08-30', 300, 6),
(22, '2025-08-30', '2025-08-31', '2025-08-30', 290, 28),
(23, '2025-09-22', '2025-09-29', '2025-08-30', 300, 12),
(24, '2025-09-22', '2025-09-29', '2025-08-30', 30, 12),
(25, '2025-09-22', '2025-09-29', '2025-08-30', 90, 12);

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
  `recorded_by` varchar(255) DEFAULT NULL,
  `reserved_by` varchar(255) DEFAULT NULL,
  `reserved_to` varchar(255) DEFAULT NULL,
  `timestamp` datetime(6) DEFAULT NULL,
  `to_location` varchar(255) DEFAULT NULL,
  `article_id` bigint(20) NOT NULL,
  `reason` enum('Bought','ChangeDepo','CommandeEnCours','Poubelle','Sold') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `article_movement`
--

INSERT INTO `article_movement` (`movement_id`, `client`, `client_activity_domain`, `client_zone`, `from_location`, `prise_en_charge`, `quantity_change`, `recorded_by`, `reserved_by`, `reserved_to`, `timestamp`, `to_location`, `article_id`, `reason`) VALUES
(2, NULL, NULL, NULL, '', NULL, 120, '12345678', NULL, NULL, '2025-08-25 10:20:01.000000', 'Tunis mall', 2, 'Bought'),
(3, NULL, NULL, NULL, '', NULL, 600, '12345678', NULL, NULL, '2025-08-25 10:23:07.000000', 'Africa Mall', 3, 'Bought'),
(4, 'Khaled bo salem', 'PCs', 'Manouba', 'Africa Mall', 'Lotfi', -100, '12345678', NULL, NULL, '2025-08-28 17:07:27.000000', 'Africa Mall', 3, 'Bought'),
(5, 'Samir', 'PCs', 'Ariana', 'Africa Mall', 'Lotfi', -100, '12345678', NULL, NULL, '2025-08-28 17:07:45.000000', 'Africa Mall', 3, 'Bought'),
(6, 'Salma', 'PCs', 'Sidi Bouzid', 'Africa Mall', 'Amir', -200, '12345678', NULL, NULL, '2025-08-28 17:08:10.000000', 'Africa Mall', 3, 'Bought'),
(7, 'Karim', 'PCs', 'Ariana', 'Africa Mall', 'Asma', -100, '12345678', NULL, NULL, '2025-08-28 17:08:50.000000', 'Africa Mall', 3, 'Bought'),
(9, NULL, NULL, NULL, 'Warehouse A', NULL, 60, '12345678', 'Khaled', 'Amir', '2025-08-28 17:09:37.000000', 'Warehouse A', 4, 'Bought'),
(10, NULL, NULL, NULL, 'Warehouse A', NULL, 100, '12345678', '', '', '2025-08-28 17:09:47.000000', 'Warehouse A', 5, 'Bought'),
(11, NULL, NULL, NULL, 'Warehouse B', NULL, 100, '12345678', '', '', '2025-08-28 17:09:57.000000', 'Warehouse B', 8, 'Bought'),
(12, NULL, NULL, NULL, 'Warehouse A', NULL, 25, '12345678', 'Hassen', 'Karim', '2025-08-28 17:10:13.000000', 'Warehouse A', 6, 'Bought'),
(13, NULL, NULL, NULL, 'Warehouse C', NULL, 100, '12345678', '', '', '2025-08-28 17:10:23.000000', 'Warehouse C', 13, 'Bought'),
(14, 'Samir', 'TOOLS', 'Ariana', 'Warehouse B', 'Karim', -20, '12345678', NULL, NULL, '2025-08-28 17:11:11.000000', 'Warehouse B', 26, 'Bought'),
(15, 'Amir', 'Supplies', 'Ariana', 'Warehouse A', 'Amir', -30, '12345678', NULL, NULL, '2025-08-28 17:11:30.000000', 'Warehouse A', 25, 'Bought'),
(16, 'Samir', 'Widgets', 'Ariana', 'Warehouse A', 'LasVegas Samir', -100, '55555555', NULL, NULL, '2025-08-28 17:19:06.000000', 'Warehouse A', 4, 'Bought'),
(17, NULL, NULL, NULL, 'Warehouse A', NULL, 50, '12345678', NULL, NULL, '2025-08-28 17:27:08.000000', 'Warehouse A', 4, 'Bought'),
(18, 'Samir', 'PCs', 'Ariana', 'Warehouse A', 'Ahmed', -70, '12345678', NULL, NULL, '2025-08-29 07:06:09.000000', 'Warehouse A', 6, 'Bought'),
(19, 'Samira', 'Mask', 'Ariana', 'Warehouse C', 'Po', -95, '44444444', NULL, NULL, '2025-08-29 07:18:58.000000', 'Warehouse C', 12, 'Bought'),
(20, NULL, NULL, NULL, 'Warehouse C', NULL, -50, '44444444', NULL, NULL, '2025-08-29 07:19:28.000000', 'Mall B', 13, 'Bought'),
(21, NULL, NULL, NULL, 'Mall C', NULL, 150, '44444444', NULL, NULL, '2025-08-29 07:19:47.000000', 'Warehouse C', 13, 'Bought'),
(22, NULL, NULL, NULL, 'Warehouse C', NULL, 90, '44444444', '', '', '2025-08-29 07:20:04.000000', 'Warehouse C', 12, 'Bought'),
(24, NULL, NULL, NULL, 'Warehouse C', NULL, 6789, '44444444', NULL, NULL, '2025-08-29 07:36:39.000000', 'Warehouse C', 13, 'Bought'),
(26, NULL, NULL, NULL, 'Warehouse F', NULL, 777, '44444444', NULL, NULL, '2025-08-29 08:02:12.000000', 'Warehouse F', 23, 'Bought'),
(27, 'Samir', 'PCs', 'Ariana', 'Warehouse A', 'Lamia', -25, '12345678', NULL, NULL, '2025-08-29 10:06:29.000000', 'Warehouse A', 5, 'Bought'),
(28, NULL, NULL, NULL, 'Warehouse C', NULL, -6930, '12345678', NULL, NULL, '2025-08-29 10:07:00.000000', 'Mall CC', 13, 'Bought'),
(29, 'Karam', 'PCs', 'Ariana', 'Warehouse C', 'Makrem', -1, '12345678', NULL, NULL, '2025-08-29 10:07:26.000000', 'Warehouse C', 13, 'Bought'),
(30, NULL, NULL, NULL, '', NULL, 500, '44444444', NULL, NULL, '2025-08-29 17:16:48.000000', 'Mall B', 27, 'Bought'),
(31, NULL, NULL, NULL, 'Tunis mall', NULL, 1, '12345678', '', '', '2025-08-29 19:00:48.000000', 'Tunis mall', 2, 'Bought'),
(32, NULL, NULL, NULL, 'Tunis mall', NULL, 33, '12345678', '', '', '2025-08-29 19:02:40.000000', 'Tunis mall', 2, 'Bought'),
(34, NULL, NULL, NULL, 'Tunis mall', NULL, 300, '12345678', NULL, NULL, '2025-08-30 08:09:22.000000', 'Tunis mall', 2, 'Bought'),
(35, NULL, NULL, NULL, 'Mall B', NULL, -500, '12345678', NULL, NULL, '2025-08-30 08:11:37.000000', NULL, 27, 'Poubelle'),
(37, NULL, NULL, NULL, 'Warehouse A', NULL, 600, '12345678', NULL, NULL, '2025-08-30 08:31:25.000000', 'Warehouse A', 5, 'Bought'),
(39, NULL, NULL, NULL, 'Warehouse A', NULL, 30, '12345678', '', '', '2025-08-30 08:41:23.000000', 'Warehouse A', 6, 'CommandeEnCours'),
(41, NULL, NULL, NULL, 'Tunis mall', NULL, -120, '12345678', NULL, NULL, '2025-08-30 10:28:15.000000', NULL, 2, 'Poubelle'),
(42, 'Mall CE', 'PCs ', 'Ariana', 'Tunis mall', 'Zoe', -300, '12345678', NULL, NULL, '2025-08-30 10:29:40.000000', 'Tunis mall', 2, 'Sold'),
(44, NULL, NULL, NULL, 'Tunis mall', NULL, 300, '12345678', NULL, NULL, '2025-08-30 10:31:40.000000', 'Tunis mall', 2, 'Bought'),
(45, NULL, NULL, NULL, 'Kali', NULL, 200, '12345678', NULL, NULL, '2025-08-30 10:35:07.000000', 'Warehouse B', 11, 'ChangeDepo'),
(46, 'Khaled ', 'Boga', 'Ariana', 'Warehouse B', 'mOI', -200, '12345678', NULL, NULL, '2025-08-30 10:35:31.000000', 'Warehouse B', 11, 'Sold'),
(47, 'kHaled', 'PCS', 'Nabeul', 'Tunis mall', 'Zoeo', -200, '12345678', NULL, NULL, '2025-08-30 10:37:23.000000', 'Tunis mall', 2, 'Sold'),
(48, NULL, NULL, NULL, 'Tunis mall', NULL, -50, '12345678', NULL, NULL, '2025-08-30 10:37:34.000000', 'Kali', 2, 'ChangeDepo'),
(49, NULL, NULL, NULL, 'Kalimatija', NULL, 100, '12345678', NULL, NULL, '2025-08-30 10:37:49.000000', 'Tunis mall', 2, 'ChangeDepo'),
(50, NULL, NULL, NULL, 'Tunis mall', NULL, 60, '12345678', NULL, NULL, '2025-08-30 10:38:15.000000', 'Tunis mall', 2, 'Bought'),
(51, NULL, NULL, NULL, '', NULL, 600, '12345678', NULL, NULL, '2025-08-30 15:54:37.000000', 'Mall B', 28, 'Bought'),
(52, NULL, NULL, NULL, 'Warehouse A', NULL, 300, '12345678', NULL, NULL, '2025-08-30 16:40:51.000000', 'Warehouse A', 6, 'Bought'),
(53, 'Khalid', 'Kilo', 'Ariana', 'Mall B', 'KK', -300, '12345678', NULL, NULL, '2025-08-30 16:46:53.000000', 'Mall B', 28, 'Sold'),
(55, NULL, NULL, NULL, 'Mall B', NULL, 300, '12345678', NULL, NULL, '2025-08-30 16:47:13.000000', 'Mall B', 28, 'Bought'),
(56, 'Kilouer', 'Maghriban', 'Siliana', 'Mall B', 'Babour', -310, '12345678', NULL, NULL, '2025-08-30 16:47:44.000000', 'Mall B', 28, 'Sold'),
(57, NULL, NULL, NULL, 'Milka', NULL, 300, '12345678', NULL, NULL, '2025-08-30 16:49:43.000000', 'Warehouse C', 12, 'ChangeDepo'),
(58, NULL, NULL, NULL, 'Warehouse B', NULL, -300, '12345678', NULL, NULL, '2025-08-30 16:50:26.000000', 'Mhamdiya', 10, 'ChangeDepo'),
(59, NULL, NULL, NULL, 'Warehouse C', NULL, -305, '12345678', NULL, NULL, '2025-08-30 16:50:50.000000', 'Mhamdia original', 12, 'ChangeDepo'),
(60, NULL, NULL, NULL, 'Mil', NULL, 30, '12345678', NULL, NULL, '2025-08-30 17:07:39.000000', 'Warehouse C', 12, 'ChangeDepo'),
(61, 'Khaliu', 'Mamilia', 'Sidi Bouzid', 'Warehouse C', 'MBB', -20, '12345678', NULL, NULL, '2025-08-30 17:08:11.000000', 'Warehouse C', 12, 'Sold'),
(62, NULL, NULL, NULL, 'Warehouse B', NULL, 90, '12345678', '', '', '2025-08-30 17:08:31.000000', 'Warehouse B', 11, 'CommandeEnCours'),
(64, NULL, NULL, NULL, 'Warehouse C', NULL, 90, '12345678', NULL, NULL, '2025-08-30 17:08:58.000000', 'Warehouse C', 12, 'Bought'),
(65, NULL, NULL, NULL, 'Warehouse C', NULL, -60, '12345678', NULL, NULL, '2025-08-30 17:09:28.000000', 'Malizia', 12, 'ChangeDepo');

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
  `article_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `commande_articlemv`
--

INSERT INTO `commande_articlemv` (`id`, `original_movement_id`, `quantity`, `reserved_by`, `reserved_to`, `article_id`) VALUES
(1, 8, 50, 'Khaled', 'Salma', 4),
(3, 38, 300, 'Khaled', 'Mall A', 6);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `cin` varchar(8) DEFAULT NULL,
  `diploma_proof` longblob DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` enum('Accepted','Refused','Waiting') NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `role` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `cin`, `diploma_proof`, `email`, `password`, `status`, `username`, `role`) VALUES
(1, '12345678', NULL, 'admin@gmail.com', '123', 'Accepted', 'admin', 1),
(2, '22222222', NULL, 'FATI@gmail.com', '123', 'Accepted', 'fatoum', 2),
(3, '11111111', NULL, 'john.doe@example.com', '123', 'Accepted', 'JohnDoe', 3),
(4, '33333333', NULL, 'jane.smith@example.com', '123', 'Accepted', 'JaneSmith', 2),
(5, '44444444', NULL, 'peter.jones@example.com', '123', 'Accepted', 'PeterJones', 4),
(6, '55555555', NULL, 'mary.williams@example.com', '123', 'Accepted', 'MaryWilliams', 3),
(7, '66666666', NULL, 'chris.brown@example.com', '123', 'Waiting', 'ChrisBrown', 2),
(8, '77777777', NULL, 'sarah.davis@example.com', '123', 'Waiting', 'SarahDavis', 4);

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
  ADD UNIQUE KEY `UK74u0aip4xj0n5mv3aooxd304a` (`cin`),
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
  MODIFY `article_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `article_batch`
--
ALTER TABLE `article_batch`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `article_movement`
--
ALTER TABLE `article_movement`
  MODIFY `movement_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- AUTO_INCREMENT for table `commande_articlemv`
--
ALTER TABLE `commande_articlemv`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

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
