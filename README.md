# StoreFactoryDatabase
A JavaFX program to interface with a database of stores, factories, clients and transactions. This was a university project.
## Dependancies
```xml
 <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.10</version>
        </dependency>
<dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.29</version>
        </dependency>
```
## Requrements
JavaFX
enter your database information into dat/database.properties file
## tables
```sql
CREATE TABLE `address` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `STREET` VARCHAR(50) NOT NULL,
  `HOUSE_NUMBER` VARCHAR(10) NOT NULL,
  `CITY` VARCHAR(30) NOT NULL,
  `POSTAL_CODE` INT NOT NULL,
  PRIMARY KEY (`ID`)
);

CREATE TABLE `category` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(25) NOT NULL,
  `DESCRIPTION` VARCHAR(250)  NOT NULL,
  PRIMARY KEY (`ID`)
) ;

CREATE TABLE `client` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` VARCHAR(25) NOT NULL,
  `LAST_NAME` VARCHAR(25) NOT NULL,
  `DATE_OF_BIRTH` DATE NOT NULL,
  `ADDRESS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ADDRESS_ID` (`ADDRESS_ID`),
  CONSTRAINT `client_ibfk_1` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`)
);

CREATE TABLE `factory` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(50)NOT NULL,
  `ADDRESS_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ADDRESS_ID` (`ADDRESS_ID`),
  CONSTRAINT `factory_ibfk_1` FOREIGN KEY (`ADDRESS_ID`) REFERENCES `address` (`ID`)
);

CREATE TABLE `factory_item` (
  `FACTORY_ID` INT NOT NULL,
  `ITEM_ID` INT NOT NULL,
  PRIMARY KEY (`FACTORY_ID`,`ITEM_ID`),
  KEY `ITEM_ID` (`ITEM_ID`),
  CONSTRAINT `factory_item_ibfk_1` FOREIGN KEY (`FACTORY_ID`) REFERENCES `factory` (`ID`),
  CONSTRAINT `factory_item_ibfk_2` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`)
);

CREATE TABLE `item` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CATEGORY_ID` INT NOT NULL,
  `NAME` VARCHAR(25) NOT NULL,
  `WIDTH` DECIMAL(10,2) NOT NULL,
  `HEIGHT` DECIMAL(10,2) NOT NULL,
  `LENGTH` DECIMAL(10,2) NOT NULL,
  `PRODUCTION_COST` DECIMAL(15,2) NOT NULL,
  `SELLING_PRICE` DECIMAL(15,2) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `CATEGORY_ID` (`CATEGORY_ID`),
  CONSTRAINT `item_ibfk_1` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `category` (`ID`)
);

CREATE TABLE `store` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(50) NOT NULL,
  `WEB_ADDRESS` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`ID`)
);

CREATE TABLE `store_item` (
  `STORE_ID` INT NOT NULL,
  `ITEM_ID` INT NOT NULL,
  PRIMARY KEY (`STORE_ID`,`ITEM_ID`),
  KEY `ITEM_ID` (`ITEM_ID`),
  CONSTRAINT `store_item_ibfk_1` FOREIGN KEY (`STORE_ID`) REFERENCES `store` (`ID`),
  CONSTRAINT `store_item_ibfk_2` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`)
);

CREATE TABLE `transaction` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CLIENT_ID` INT NOT NULL,
  `TRANSACTION_DATE` DATE NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `CLIENT_ID` (`CLIENT_ID`),
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`CLIENT_ID`) REFERENCES `client` (`ID`)
);

CREATE TABLE `transaction_item` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `TRANSACTION_ID` INT NOT NULL,
  `ITEM_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `TRANSACTION_ID` (`TRANSACTION_ID`),
  KEY `ITEM_ID` (`ITEM_ID`),
  CONSTRAINT `transaction_item_ibfk_1` FOREIGN KEY (`TRANSACTION_ID`) REFERENCES `transaction` (`ID`),
  CONSTRAINT `transaction_item_ibfk_2` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`)
);
```
