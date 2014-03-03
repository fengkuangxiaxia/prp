-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2014 年 03 月 03 日 06:31
-- 服务器版本: 5.5.24-log
-- PHP 版本: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `prp`
--

-- --------------------------------------------------------

--
-- 表的结构 `admin_user`
--

CREATE TABLE IF NOT EXISTS `admin_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `password` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `admin_user`
--

INSERT INTO `admin_user` (`id`, `username`, `password`) VALUES
(1, 'admin', '21232f297a57a5a743894a0e4a801fc3');

-- --------------------------------------------------------

--
-- 表的结构 `command`
--

CREATE TABLE IF NOT EXISTS `command` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `command` text NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `result_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- 表的结构 `device`
--

CREATE TABLE IF NOT EXISTS `device` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `IMEI` text NOT NULL,
  `phoneNumber` text NOT NULL,
  `phoneType` text NOT NULL,
  `system` text NOT NULL,
  `updateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- 转存表中的数据 `device`
--

INSERT INTO `device` (`id`, `IMEI`, `phoneNumber`, `phoneType`, `system`, `updateAt`) VALUES
(2, '000000000000000', '15555215554', 'sdk', '2.2', '2014-02-10 08:54:48');

-- --------------------------------------------------------

--
-- 表的结构 `info_table`
--

CREATE TABLE IF NOT EXISTS `info_table` (
  `device_id` int(11) NOT NULL,
  `info_id` int(11) NOT NULL COMMENT '信息的id，可能是不同表的id',
  `info_type` int(11) NOT NULL COMMENT '信息类型，不同的类型对应查找不同的表',
  PRIMARY KEY (`device_id`,`info_id`,`info_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `info_table`
--

INSERT INTO `info_table` (`device_id`, `info_id`, `info_type`) VALUES
(2, 1, 2),
(2, 1, 3),
(2, 1, 4),
(2, 2, 2);

-- --------------------------------------------------------

--
-- 表的结构 `phone_calling_record`
--

CREATE TABLE IF NOT EXISTS `phone_calling_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `time` text NOT NULL,
  `type` text NOT NULL,
  `duration` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `phone_calling_record`
--

INSERT INTO `phone_calling_record` (`id`, `name`, `time`, `type`, `duration`) VALUES
(1, '13788979253', '2014-02-12 03:15:16', '呼出', '16');

-- --------------------------------------------------------

--
-- 表的结构 `phone_contact`
--

CREATE TABLE IF NOT EXISTS `phone_contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `phoneNumber` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- 转存表中的数据 `phone_contact`
--

INSERT INTO `phone_contact` (`id`, `name`, `phoneNumber`) VALUES
(3, 'A B', '13788979253'),
(4, 'C', '1334567123');

-- --------------------------------------------------------

--
-- 表的结构 `phone_location`
--

CREATE TABLE IF NOT EXISTS `phone_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `latitude` text NOT NULL COMMENT '经度',
  `longitude` text NOT NULL COMMENT '纬度',
  `updateAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- 转存表中的数据 `phone_location`
--

INSERT INTO `phone_location` (`id`, `latitude`, `longitude`, `updateAt`) VALUES
(1, '31.1', '121.6', '2014-02-11 09:06:54'),
(2, '30', '120', '2014-02-11 09:08:06');

-- --------------------------------------------------------

--
-- 表的结构 `phone_sms_record`
--

CREATE TABLE IF NOT EXISTS `phone_sms_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phoneNumber` text NOT NULL,
  `time` text NOT NULL,
  `type` text NOT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `phone_sms_record`
--

INSERT INTO `phone_sms_record` (`id`, `phoneNumber`, `time`, `type`, `content`) VALUES
(1, '13788979253', '2014-02-12 04:05:16', '发送', '123');

-- --------------------------------------------------------

--
-- 表的结构 `result`
--

CREATE TABLE IF NOT EXISTS `result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `result` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `result`
--

INSERT INTO `result` (`id`, `result`) VALUES
(1, '暂无结果');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
