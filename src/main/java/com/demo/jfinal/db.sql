
use jfinal_demo;

CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8

+----+------------------------+--------------------------+
| id | title                  | content                  |
+----+------------------------+--------------------------+
|  1 | JFinal Demo Title here | JFinal Demo Content here |
|  2 | test 1                 | test 1                   |
|  3 | test 2                 | test 2                   |
|  4 | test 3                 | test 3                   |
|  5 | test 4                 | test 4                   |
+----+------------------------+--------------------------+

CREATE TABLE `log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL,
  `log` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8
