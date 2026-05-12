# Connecting EthioTour Connect To SQL Server

The current app uses `DatabaseService` as an in-memory repository. To connect it to a real database, keep the Swing screens and models as they are, then replace the internals of `DatabaseService` with JDBC queries.

## 1. Add The SQL Server JDBC Driver

Download Microsoft's JDBC driver for SQL Server and add the `.jar` file to the compile/run classpath.

Example:

```bat
javac -cp ".;lib\mssql-jdbc.jar" src\main\java\com\ethiotour\model\*.java src\main\java\com\ethiotour\util\*.java src\main\java\com\ethiotour\service\*.java src\main\java\com\ethiotour\controller\*.java src\main\java\com\ethiotour\view\*.java src\main\java\com\ethiotour\EthioTourApp.java
java -cp "src\main\java;lib\mssql-jdbc.jar" com.ethiotour.EthioTourApp
```

## 2. Create Tables

```sql
CREATE DATABASE EthioTourConnect;
GO

USE EthioTourConnect;
GO

CREATE TABLE destinations (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(120) NOT NULL,
    description NVARCHAR(500) NOT NULL,
    region NVARCHAR(80) NOT NULL,
    altitude DECIMAL(8,2) NOT NULL,
    entrance_protocol NVARCHAR(500) NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    created_date DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE)
);

CREATE TABLE tours (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(120) NOT NULL,
    description NVARCHAR(500) NOT NULL,
    destination_id INT NOT NULL,
    guide_id INT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    max_participants INT NOT NULL,
    current_participants INT NOT NULL DEFAULT 0,
    resident_price DECIMAL(10,2) NOT NULL,
    non_resident_price DECIMAL(10,2) NOT NULL,
    status NVARCHAR(30) NOT NULL DEFAULT 'PLANNED',
    created_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT fk_tours_destinations FOREIGN KEY (destination_id) REFERENCES destinations(id)
);

CREATE TABLE bookings (
    id INT IDENTITY(1,1) PRIMARY KEY,
    tour_id INT NOT NULL,
    customer_name NVARCHAR(120) NOT NULL,
    customer_email NVARCHAR(160) NOT NULL,
    customer_phone NVARCHAR(40) NOT NULL,
    is_resident BIT NOT NULL,
    participants_count INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status NVARCHAR(30) NOT NULL DEFAULT 'PENDING_CONFIRMATION',
    payment_method NVARCHAR(80) NULL,
    payment_reference NVARCHAR(120) NULL,
    booking_date DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    last_updated DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT fk_bookings_tours FOREIGN KEY (tour_id) REFERENCES tours(id)
);
```

## 3. Open A JDBC Connection

Use environment variables for credentials instead of hard-coding passwords.

```java
String url = "jdbc:sqlserver://localhost:1433;databaseName=EthioTourConnect;encrypt=true;trustServerCertificate=true";
String user = System.getenv("ETHIOTOUR_DB_USER");
String password = System.getenv("ETHIOTOUR_DB_PASSWORD");

Connection connection = DriverManager.getConnection(url, user, password);
```

## 4. Replace In-Memory Methods Gradually

Start with read-only methods:

- `getAllDestinations()`
- `getDestinationById(int id)`
- `getAllTours()`
- `getTourById(int id)`

Then move write methods:

- `addDestination(Destination destination)`
- `updateDestination(Destination destination)`
- `addTour(Tour tour)`
- `addBooking(Booking booking)`

Keep `BookingService` as the business layer. It should continue to validate availability, calculate price, and move booking statuses. The database layer should only save and load data.

## 5. Important Transaction

`addBooking` should be a transaction because it inserts a booking and increments tour participants.

```java
connection.setAutoCommit(false);
try {
    // insert booking
    // update tours set current_participants = current_participants + ?
    connection.commit();
} catch (SQLException ex) {
    connection.rollback();
    throw ex;
}
```

This preserves the check-validate-commit workflow and prevents overbooking when multiple users book the same tour.
