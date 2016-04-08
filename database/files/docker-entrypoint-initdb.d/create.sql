/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for tgp
CREATE DATABASE IF NOT EXISTS `tgp` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `tgp`;


-- Dumping structure for table tgp.game
CREATE TABLE IF NOT EXISTS `game` (
  `id` int(11) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `box_small` varchar(256) DEFAULT NULL,
  `box_medium` varchar(256) DEFAULT NULL,
  `box_large` varchar(256) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table tgp.game_stats
CREATE TABLE IF NOT EXISTS `game_stats` (
  `id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `viewers` int(11) NOT NULL DEFAULT '0',
  `channels` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table tgp.run_stats
CREATE TABLE IF NOT EXISTS `run_stats` (
  `date` datetime NOT NULL,
  `total_games` int(11) NOT NULL,
  `total_viewers` int(11) NOT NULL,
  `total_channels` int(11) NOT NULL,
  `games_created` int(11) NOT NULL,
  `stats_created` int(11) NOT NULL,
  `requests_made` int(11) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
