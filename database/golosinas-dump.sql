-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: golosinas
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `categoriaID` int NOT NULL AUTO_INCREMENT,
  `categoria` varchar(50) NOT NULL,
  PRIMARY KEY (`categoriaID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Chocolates'),(2,'Chupetines'),(3,'Caramelos'),(4,'Gomitas'),(8,'Galletitas'),(9,'Chicles'),(11,'Gelatinas');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `clienteID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `localidadID` int DEFAULT NULL,
  `direccion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`clienteID`),
  KEY `localidadID` (`localidadID`),
  CONSTRAINT `clientes_ibfk_1` FOREIGN KEY (`localidadID`) REFERENCES `localidad` (`localidadID`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Maria López','236489756',1,'Arias 659'),(2,'Carlos Pérez','236458987',5,'Direc 1'),(3,'Maria Maria','236458987',5,'Direc 2'),(4,'Carlos López','236458987',5,'Direc 1'),(5,'María López','123456789',1,'Calle 123'),(7,'Ana Martínez','345678901',3,'Calle Principal'),(8,'Pedro Rodríguez','456789012',4,'Avenida Central'),(10,'Diego Sánchez','678901234',1,'Avenida Central'),(11,'Laura Hernández','789012345',2,'Calle 456'),(12,'Carlos Fernández','890123456',3,'Avenida Principal'),(13,'Sofía Díaz','901234567',4,'Calle 789'),(14,'Alejandro González','012345678',5,'Avenida Norte'),(15,'Marta Ruiz','123450987',1,'Calle Sur'),(17,'Isabel García','345098765',3,'Calle Oeste'),(18,'Daniel Martín','450987654',4,'Avenida 789'),(19,'Carmen Pérez','509876543',5,'Calle 123'),(20,'Luisa Sánchez','098765432',1,'Avenida 456'),(21,'Roberto Rodríguez','987654321',2,'Calle Principal'),(22,'Elena Gómez','876543210',3,'Avenida Central'),(23,'Miguel Fernández','765432109',4,'Calle Secundaria'),(24,'Ana María Díaz','654321098',5,'Avenida Central');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalleventa`
--

DROP TABLE IF EXISTS `detalleventa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalleventa` (
  `detalleID` int NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `ventaID` int NOT NULL,
  `productoID` int NOT NULL,
  PRIMARY KEY (`detalleID`),
  KEY `ventaID` (`ventaID`),
  KEY `productoID` (`productoID`),
  CONSTRAINT `detalleventa_ibfk_1` FOREIGN KEY (`ventaID`) REFERENCES `venta` (`ventaID`),
  CONSTRAINT `detalleventa_ibfk_2` FOREIGN KEY (`productoID`) REFERENCES `productos` (`productoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalleventa`
--

LOCK TABLES `detalleventa` WRITE;
/*!40000 ALTER TABLE `detalleventa` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalleventa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `localidad`
--

DROP TABLE IF EXISTS `localidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `localidad` (
  `localidadID` int NOT NULL AUTO_INCREMENT,
  `localidad` varchar(50) NOT NULL,
  PRIMARY KEY (`localidadID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `localidad`
--

LOCK TABLES `localidad` WRITE;
/*!40000 ALTER TABLE `localidad` DISABLE KEYS */;
INSERT INTO `localidad` VALUES (1,'Junín'),(2,'Alem'),(3,'Pergamino'),(4,'Chacabuco'),(5,'Pehuajó');
/*!40000 ALTER TABLE `localidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marca`
--

DROP TABLE IF EXISTS `marca`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marca` (
  `marcaID` int NOT NULL AUTO_INCREMENT,
  `marca` varchar(50) NOT NULL,
  PRIMARY KEY (`marcaID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marca`
--

LOCK TABLES `marca` WRITE;
/*!40000 ALTER TABLE `marca` DISABLE KEYS */;
INSERT INTO `marca` VALUES (1,'Arcor'),(2,'Billiken'),(3,'Oreo'),(6,'Kinder'),(7,'Topline'),(8,'Stork'),(9,'Beldent'),(10,'Pico Dulce');
/*!40000 ALTER TABLE `marca` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `productoID` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(50) NOT NULL,
  `precio_unidad` decimal(10,2) NOT NULL,
  `precio_caja` decimal(10,2) DEFAULT NULL,
  `stock` int NOT NULL,
  `marcaID` int DEFAULT NULL,
  `categoriaID` int NOT NULL,
  PRIMARY KEY (`productoID`),
  KEY `marcaID` (`marcaID`),
  KEY `categoriaID` (`categoriaID`),
  CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`marcaID`) REFERENCES `marca` (`marcaID`),
  CONSTRAINT `productos_ibfk_2` FOREIGN KEY (`categoriaID`) REFERENCES `categorias` (`categoriaID`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (2,'Caramelos Yoghurt',125.56,1500.25,5,2,3),(4,'Caramelos Frutales',75.56,1500.00,13,1,3),(10,'Galletitas 120gr',125.00,5231.25,30,3,8),(11,'Gall. 250gr',890.36,6000.00,12,3,8),(12,'Chocolates 23gr.',150.89,12566.00,12,3,1),(13,'Kinder Bueno 3 unidades',250.00,2400.00,50,6,1),(14,'Chicles Topline Menta',30.00,300.00,200,7,9),(15,'Chupetín Stork Frutal',15.00,150.00,100,8,2),(16,'Chicles Beldent Energía',40.00,400.00,150,9,9),(17,'Caramelos Pico Dulce Frutilla',10.00,100.00,300,10,3),(18,'Huevitos de Chocolate Kinder Sorpresa',150.00,1400.00,80,6,1),(19,'Chicles Topline Mora',30.00,300.00,200,7,9),(20,'Chupetín Stork Cola',15.00,150.00,100,8,2),(21,'Chicles Beldent Relax',40.00,400.00,150,9,9),(22,'Caramelos Pico Dulce Manzana',10.00,100.00,300,10,3),(23,'Chocolate Kinder Joy 1 unidad',70.00,650.00,100,6,1),(24,'Chicles Topline Tropical',30.00,300.00,200,7,9),(25,'Chupetín Stork Tutti Frutti',15.00,150.00,100,8,2),(26,'Chicles Beldent Breathe',40.00,400.00,150,9,9),(27,'Caramelos Pico Dulce Limón',10.00,100.00,300,10,3),(29,'Chicles Topline Sandía',30.00,300.00,200,7,9),(30,'Chupetín Stork Naranja',15.00,150.00,100,8,2),(31,'Chicles Beldent White',40.00,400.00,150,9,9),(32,'Caramelos Pico Dulce Uva',10.00,100.00,300,10,3),(33,'Gomitas Ácidas',570.15,6500.99,20,1,4);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productosproveedores`
--

DROP TABLE IF EXISTS `productosproveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productosproveedores` (
  `productoProveedorID` int NOT NULL AUTO_INCREMENT,
  `costo` decimal(10,2) DEFAULT NULL,
  `productoID` int NOT NULL,
  `proveedorID` int NOT NULL,
  PRIMARY KEY (`productoProveedorID`),
  KEY `productoID` (`productoID`),
  KEY `proveedorID` (`proveedorID`),
  CONSTRAINT `productosproveedores_ibfk_1` FOREIGN KEY (`productoID`) REFERENCES `productos` (`productoID`),
  CONSTRAINT `productosproveedores_ibfk_2` FOREIGN KEY (`proveedorID`) REFERENCES `proveedores` (`proveedorID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productosproveedores`
--

LOCK TABLES `productosproveedores` WRITE;
/*!40000 ALTER TABLE `productosproveedores` DISABLE KEYS */;
INSERT INTO `productosproveedores` VALUES (10,8.50,11,5),(18,9.99,11,3),(19,11.25,11,4),(20,8.50,11,5),(21,2500.00,16,7),(23,145.26,2,9),(24,25000.00,18,10),(25,258.90,26,11);
/*!40000 ALTER TABLE `productosproveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedores` (
  `proveedorID` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `correo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`proveedorID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES (3,'Proveedor3','555555555','proveedor3@example.com'),(4,'Proveedor4','777777777','proveedor4@example.com'),(5,'Proveedor5','999999999','proveedor5@example.com'),(7,'Marta','23645897878','marta@gmail.com'),(8,'Marta','23645897878','marta@gmail.com'),(9,'Camile','2365487989','cami@gmail.coom'),(10,'Camile','2365487989','cami@gmail.coom'),(11,'Camile','2365487989','cami@gmail.coom');
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venta`
--

DROP TABLE IF EXISTS `venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venta` (
  `ventaID` int NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `clienteID` int NOT NULL,
  PRIMARY KEY (`ventaID`),
  KEY `clienteID` (`clienteID`),
  CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`clienteID`) REFERENCES `clientes` (`clienteID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venta`
--

LOCK TABLES `venta` WRITE;
/*!40000 ALTER TABLE `venta` DISABLE KEYS */;
/*!40000 ALTER TABLE `venta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-05 17:39:05
