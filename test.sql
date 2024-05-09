-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 09 mai 2024 à 20:41
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `test`
--

-- --------------------------------------------------------

--
-- Structure de la table `achats`
--

CREATE TABLE `achats` (
  `id` int(11) NOT NULL,
  `ref_article` varchar(255) NOT NULL,
  `quantite` int(11) NOT NULL,
  `date_achat` date NOT NULL,
  `prix_unitaire` double NOT NULL,
  `tva` double NOT NULL,
  `prix_total` double NOT NULL,
  `id_fournisseur` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `achats`
--

INSERT INTO `achats` (`id`, `ref_article`, `quantite`, `date_achat`, `prix_unitaire`, `tva`, `prix_total`, `id_fournisseur`) VALUES
(1, 'test', 20, '2024-05-03', 20, 10, 440.00000000000006, NULL),
(2, 't', 20, '2024-05-03', 20, 10, 440.00000000000006, NULL),
(3, 'ref', 20, '2024-05-10', 10, 20, 20, NULL),
(4, 'refZ', 200, '2024-05-05', 70, 20, 450, NULL),
(5, 'refArticle', 100, '2024-05-01', 250, 10, 20, NULL),
(6, 'ess', 20, '2024-05-05', 12, 20, 100, NULL),
(7, 'test', 20, '2024-05-05', 20, 20, 20, NULL),
(8, 'testt', 20, '2024-05-10', 20, 20, 100, 29),
(9, 'refZ', 45, '2024-05-06', 45, 45, 15, 31),
(10, 'refZ', 25, '2024-05-09', 70, 10, 11, 28);

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE `admin` (
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `Id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`username`, `password`, `Id`) VALUES
('boris', 'boris123', 1);

-- --------------------------------------------------------

--
-- Structure de la table `articles`
--

CREATE TABLE `articles` (
  `id` int(11) NOT NULL,
  `reference` varchar(255) NOT NULL,
  `libelle` varchar(255) NOT NULL,
  `stock` int(11) NOT NULL,
  `prix_achat` double NOT NULL,
  `prix_vente` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `articles`
--

INSERT INTO `articles` (`id`, `reference`, `libelle`, `stock`, `prix_achat`, `prix_vente`) VALUES
(4, 'ref1', 'test', 20, 100, 150),
(5, 'redf', '20', 20, 40, 45),
(6, 'refZ', 'sac sucre 50 kg', -105, 70, 450),
(7, 'refArticle', 'libArticle', 100, 250, 20),
(8, 'ess', 'ess', 20, 12, 100),
(9, 'test', 'test', 20, 20, 20),
(10, 'testt', 'Art', 0, 20, 100);

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

CREATE TABLE `clients` (
  `id` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`id`, `nom`, `adresse`, `telephone`) VALUES
(3, 'Boris', 'SAMNE', '0617386008'),
(4, 'baba prince', 'test', '015'),
(7, 'Inconnue', 'Inconnue', 'Inconnue'),
(8, 'lamiae', 'tanger', '0617');

-- --------------------------------------------------------

--
-- Structure de la table `fournisseurs`
--

CREATE TABLE `fournisseurs` (
  `Id` int(11) NOT NULL,
  `Nom` varchar(255) NOT NULL,
  `Adresse` varchar(255) NOT NULL,
  `Telephone` varchar(50) NOT NULL,
  `RIB` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `fournisseurs`
--

INSERT INTO `fournisseurs` (`Id`, `Nom`, `Adresse`, `Telephone`, `RIB`) VALUES
(26, 'Fournisseur2', 'Adresse2', '0203040506', '2345678901'),
(27, 'Fournisseur3', 'Adresse3', '0304050607', '3456789012'),
(28, 'Fournisseur4', 'Adresse4', '0405060708', '4567890123'),
(29, 'Fournisseur5', 'Adresse5', '0506070809', '5678901234'),
(30, 'Fournisseur6', 'Adresse6', '0607080910', '6789012345'),
(31, 'Fournisseur7', 'Adresse7', '0708091011', '7890123456'),
(32, 'Fournisseur8', 'Adresse8', '0809101112', '8901234567'),
(33, 'Fournisseur9', 'Adresse9', '0910111213', '9012345678'),
(34, 'Fournisseur10', 'Adresse10', '1011121314', '0123456789'),
(35, 'Fournisseur11', 'Adresse11', '1112131415', '1234567890'),
(36, 'Fournisseur12', 'Adresse12', '1213141516', '2345678901'),
(37, 'Fournisseur13', 'Adresse13', '1314151617', '3456789012'),
(38, 'Fournisseur14', 'Adresse14', '1415161718', '4567890123'),
(39, 'Fournisseur15', 'Adresse15', '1516171819', '5678901234'),
(40, 'Fournisseur16', 'Adresse16', '1617181920', '6789012345'),
(41, 'Fournisseur17', 'Adresse17', '1718192021', '7890123456'),
(42, 'Fournisseur18', 'Adresse18', '1819202122', '8901234567'),
(43, 'Fournisseur19', 'Adresse19', '1920212223', '9012345678'),
(44, 'Fournisseur20', 'Adresse20', '2021222324', '0123456789'),
(45, 'Fournisseur21', 'Adresse21', '2122232425', '1234567890'),
(46, 'Fournisseur22', 'Adresse22', '2223242526', '2345678901'),
(47, 'Fournisseur23', 'Adresse23', '2324252627', '3456789012'),
(48, 'Fournisseur24', 'Adresse24', '2425262728', '4567890123'),
(49, 'Fournisseur25', 'Adresse25', '2526272829', '5678901234'),
(50, 'Fournisseur26', 'Adresse26', '2627282930', '6789012345'),
(51, 'Fournisseur27', 'Adresse27', '2728293031', '7890123456'),
(52, 'Fournisseur28', 'Adresse28', '2829303132', '8901234567'),
(53, 'Fournisseur29', 'Adresse29', '2930313233', '9012345678'),
(54, 'Fournisseur30', 'Adresse30', '3031323334', '0123456789'),
(55, 'Fournisseur31', 'Adresse31', '3132333435', '1234567890'),
(56, 'Fournisseur32', 'Adresse32', '3233343536', '2345678901'),
(57, 'Fournisseur33', 'Adresse33', '3334353637', '3456789012'),
(58, 'Fournisseur34', 'Adresse34', '3435363738', '4567890123'),
(59, 'Fournisseur35', 'Adresse35', '3536373839', '5678901234'),
(60, 'Fournisseur36', 'Adresse36', '3637383940', '6789012345'),
(61, 'Fournisseur37', 'Adresse37', '3738394041', '7890123456'),
(62, 'Fournisseur38', 'Adresse38', '3839404142', '8901234567'),
(63, 'Fournisseur39', 'Adresse39', '3940414243', '9012345678'),
(64, 'Fournisseur40', 'Adresse40', '4041424344', '0123456789'),
(65, 'Fournisseur41', 'Adresse41', '4142434445', '1234567890'),
(66, 'Fournisseur42', 'Adresse42', '4243444546', '2345678901'),
(67, 'Fournisseur43', 'Adresse43', '4344454647', '3456789012'),
(68, 'Fournisseur44', 'Adresse44', '4445464748', '4567890123'),
(69, 'Fournisseur45', 'Adresse45', '4546474849', '5678901234'),
(70, 'Fournisseur46', 'Adresse46', '4647484950', '6789012345'),
(71, 'Fournisseur47', 'Adresse47', '4748495051', '7890123456'),
(72, 'Fournisseur48', 'Adresse48', '4849505152', '8901234567'),
(73, 'Fournisseur49', 'Adresse49', '4950515253', '9012345678'),
(74, 'Fournisseur50', 'Adresse50', '5051525354', '0123456789'),
(75, 'Fournisseur51', 'Adresse51', '5152535455', '1234567890'),
(76, 'Fournisseur52', 'Adresse52', '5253545556', '2345678901'),
(77, 'Fournisseur53', 'Adresse53', '5354555657', '3456789012'),
(78, 'Fournisseur54', 'Adresse54', '5455565758', '4567890123'),
(80, 'Fournisseur56', 'Adresse56', '5657585960', '6789012345'),
(81, 'Fournisseur57', 'Adresse57', '5758596061', '7890123456'),
(82, 'Fournisseur58', 'Adresse58', '5859606162', '8901234567'),
(83, 'Fournisseur59', 'Adresse59', '5960616263', '9012345678'),
(84, 'Fournisseur60', 'Adresse60', '6061626364', '0123456789'),
(85, 'Fournisseur61', 'Adresse61', '6162636465', '1234567890'),
(86, 'Fournisseur62', 'Adresse62', '6263646566', '2345678901'),
(87, 'Fournisseur63', 'Adresse63', '6364656667', '3456789012'),
(88, 'Fournisseur64', 'Adresse64', '6465666768', '4567890123'),
(89, 'Fournisseur65', 'Adresse65', '6566676869', '5678901234'),
(90, 'Fournisseur66', 'Adresse66', '6667686970', '6789012345'),
(91, 'Fournisseur67', 'Adresse67', '6768697071', '7890123456'),
(92, 'Fournisseur68', 'Adresse68', '6869707172', '8901234567'),
(93, 'Fournisseur69', 'Adresse69', '6970717273', '9012345678'),
(94, 'Fournisseur70', 'Adresse70', '7071727374', '0123456789'),
(95, 'Fournisseur71', 'Adresse71', '7172737475', '1234567890'),
(96, 'Fournisseur72', 'Adresse72', '7273747576', '2345678901'),
(97, 'Fournisseur73', 'Adresse73', '7374757677', '3456789012'),
(98, 'Fournisseur74', 'Adresse74', '7475767778', '4567890123'),
(99, 'Fournisseur75', 'Adresse75', '7576777879', '5678901234'),
(100, 'Fournisseur76', 'Adresse76', '7677787980', '6789012345'),
(101, 'Fournisseur77', 'Adresse77', '7778798081', '7890123456'),
(102, 'Fournisseur78', 'Adresse78', '7879808182', '8901234567'),
(103, 'Fournisseur79', 'Adresse79', '7980818283', '9012345678'),
(104, 'Fournisseur80', 'Adresse80', '8081828384', '0123456789'),
(105, 'Fournisseur81', 'Adresse81', '8182838485', '1234567890'),
(106, 'Fournisseur82', 'Adresse82', '8283848586', '2345678901'),
(107, 'Fournisseur83', 'Adresse83', '8384858687', '3456789012'),
(108, 'Fournisseur84', 'Adresse84', '8485868788', '4567890123'),
(109, 'Fournisseur85', 'Adresse85', '8586878889', '5678901234'),
(110, 'Fournisseur86', 'Adresse86', '8687888990', '6789012345'),
(111, 'Fournisseur87', 'Adresse87', '8788899091', '7890123456'),
(112, 'Fournisseur88', 'Adresse88', '8889909192', '8901234567'),
(113, 'Fournisseur89', 'Adresse89', '8990919293', '9012345678'),
(114, 'Fournisseur90', 'Adresse90', '9091929394', '0123456789'),
(115, 'Fournisseur91', 'Adresse91', '9192939495', '1234567890'),
(116, 'Fournisseur92', 'Adresse92', '9293949596', '2345678901'),
(117, 'Fournisseur93', 'Adresse93', '9394959697', '3456789012'),
(118, 'Fournisseur94', 'Adresse94', '9495969798', '4567890123'),
(119, 'Fournisseur95', 'Adresse95', '9596979899', '5678901234'),
(120, 'Fournisseur96', 'Adresse96', '9697989910', '6789012345'),
(121, 'Fournisseur97', 'Adresse97', '9798999101', '7890123456'),
(122, 'Fournisseur98', 'Adresse98', '9899101102', '8901234567'),
(123, 'Fournisseur99', 'Adresse99', '9910111020', '9012345678'),
(124, 'Fournisseur100', 'Adresse100', '0101102030', '0123456789'),
(125, 'Fournisseur72', 'Adresse72', '0123456789', '9876543210'),
(127, 'dtrhdtf', 'kutfck', 'kj bjk', 'ubu'),
(129, 'Fou', 'tanger', '06120', '020');

-- --------------------------------------------------------

--
-- Structure de la table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `message` varchar(500) NOT NULL,
  `date` date NOT NULL DEFAULT current_timestamp(),
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Structure de la table `ventes`
--

CREATE TABLE `ventes` (
  `id` int(11) NOT NULL,
  `idArticle` int(11) DEFAULT NULL,
  `idClient` int(11) DEFAULT 7,
  `quantite` int(11) DEFAULT 1,
  `date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Déchargement des données de la table `ventes`
--

INSERT INTO `ventes` (`id`, `idArticle`, `idClient`, `quantite`, `date`) VALUES
(11, 9, 7, 20, '2024-05-08 14:00:07'),
(12, 6, 7, 10, '2024-05-08 15:18:18'),
(13, 4, 7, 10, '2024-05-08 15:18:18'),
(14, 7, 7, 15, '2024-05-08 16:41:47'),
(15, 6, 7, 10, '2024-05-08 16:41:47'),
(16, 5, 7, 10, '2024-05-09 09:23:46'),
(17, 7, 7, 15, '2024-05-09 09:23:46'),
(18, 8, 7, 15, '2024-05-09 09:23:46'),
(19, 8, 7, 35, '2024-05-09 09:23:46'),
(20, 10, 7, 12, '2024-05-09 09:23:46'),
(21, 10, 7, 20, '2024-05-09 09:23:46'),
(22, 9, 7, 12, '2024-05-09 09:25:34'),
(23, 9, 7, 5, '2024-05-09 09:25:34'),
(24, 10, 7, 20, '2024-05-09 09:25:34'),
(25, 7, 7, 65, '2024-05-09 09:25:34'),
(26, 9, 7, 65, '2024-05-09 09:25:34'),
(27, 6, 7, 20, '2024-05-09 09:33:41'),
(28, 6, 7, 20, '2024-05-09 09:33:45'),
(29, 6, 7, 20, '2024-05-09 09:33:47'),
(30, 6, 7, 20, '2024-05-09 09:33:48'),
(31, 10, 7, 2, '2024-05-09 09:34:58'),
(32, 6, 7, 30, '2024-05-09 09:35:01'),
(33, 7, 7, 40, '2024-05-09 09:35:01'),
(34, 8, 7, 20, '2024-05-09 09:39:01'),
(35, 6, 7, 35, '2024-05-09 09:39:01'),
(36, 6, 7, 10, '2024-05-09 09:41:04'),
(37, 8, 7, 20, '2024-05-09 09:41:04'),
(50, 6, 7, 20, '2024-05-09 12:04:19'),
(54, 6, 3, 10, '2024-05-09 12:10:51'),
(55, 6, 3, 10, '2024-05-09 12:12:01'),
(56, 6, 3, 10, '2024-05-09 12:12:27'),
(57, 7, 3, 1, '2024-05-09 12:12:27'),
(58, 7, 7, 12, '2024-05-09 12:13:03'),
(59, 6, 7, 70, '2024-05-09 12:18:14'),
(60, 6, 4, 200, '2024-05-09 12:20:35'),
(61, 6, 4, 200, '2024-05-09 12:26:46'),
(63, 10, 3, 20, '2024-05-09 14:05:21'),
(64, 5, 4, 10, '2024-05-09 15:18:09'),
(65, 6, 4, 15, '2024-05-09 15:18:09'),
(66, 6, 4, 70, '2024-05-09 15:18:10'),
(67, 6, 4, 70, '2024-05-09 15:18:10');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `achats`
--
ALTER TABLE `achats`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`Id`);

--
-- Index pour la table `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `fournisseurs`
--
ALTER TABLE `fournisseurs`
  ADD PRIMARY KEY (`Id`);

--
-- Index pour la table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `ventes`
--
ALTER TABLE `ventes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idArticle` (`idArticle`),
  ADD KEY `idClient` (`idClient`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `achats`
--
ALTER TABLE `achats`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `admin`
--
ALTER TABLE `admin`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `articles`
--
ALTER TABLE `articles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `clients`
--
ALTER TABLE `clients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `fournisseurs`
--
ALTER TABLE `fournisseurs`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=130;

--
-- AUTO_INCREMENT pour la table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `ventes`
--
ALTER TABLE `ventes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=68;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `ventes`
--
ALTER TABLE `ventes`
  ADD CONSTRAINT `ventes_ibfk_1` FOREIGN KEY (`idArticle`) REFERENCES `articles` (`id`),
  ADD CONSTRAINT `ventes_ibfk_2` FOREIGN KEY (`idClient`) REFERENCES `clients` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
