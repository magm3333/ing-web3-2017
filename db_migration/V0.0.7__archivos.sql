DROP TABLE IF EXISTS `archivos`;

CREATE TABLE `archivos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contenido` longblob,
  `nombre` varchar(255) DEFAULT NULL,
  `mimeType` varchar(50) DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


