package com.ethiotour.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.ethiotour.config.DatabaseConfig;
import com.ethiotour.model.Booking;
import com.ethiotour.model.Destination;
import com.ethiotour.model.Tour;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MSSQLDatabaseService implements IDatabaseService {
    private static MSSQLDatabaseService instance;
    private HikariDataSource dataSource;
    private boolean connected = false;

    private MSSQLDatabaseService() {
        initializeConnection();
    }

    public static MSSQLDatabaseService getInstance() {
        if (instance == null) {
            instance = new MSSQLDatabaseService();
        }
        return instance;
    }

    private void initializeConnection() {
        try {
            HikariConfig config = new HikariConfig();
            
            // Basic connection properties
            config.setJdbcUrl(String.format("jdbc:sqlserver://%s:%d;databaseName=%s",
                DatabaseConfig.getMSSQLServer(),
                DatabaseConfig.getMSSQLPort(),
                DatabaseConfig.getMSSQLDatabase()
            ));
            
            // Authentication and additional properties
            config.setUsername(DatabaseConfig.getMSSQLUsername());
            config.setPassword(DatabaseConfig.getMSSQLPassword());
            
            // Driver specific properties
            config.addDataSourceProperty("encrypt", String.valueOf(DatabaseConfig.getMSSQLEncrypt()));
            config.addDataSourceProperty("trustServerCertificate", String.valueOf(DatabaseConfig.getMSSQLTrustServerCertificate()));
            config.addDataSourceProperty("loginTimeout", String.valueOf(DatabaseConfig.getMSSQLLoginTimeout()));

            // Pool configuration
            config.setMaximumPoolSize(DatabaseConfig.getPoolMaxSize());
            config.setMinimumIdle(DatabaseConfig.getPoolInitialSize());
            config.setConnectionTimeout(DatabaseConfig.getPoolConnectionTimeout());
            config.setIdleTimeout(DatabaseConfig.getPoolIdleTimeout());
            config.setMaxLifetime(DatabaseConfig.getPoolMaxLifetime());
            config.setPoolName("EthioTourPool");

            dataSource = new HikariDataSource(config);
            
            // Test connection
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("[OK] Connected to MS SQL Server: " + meta.getDatabaseProductName() 
                    + " " + meta.getDatabaseProductVersion());
                connected = true;
                initializeDatabaseSchema();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to connect to MS SQL Server: " + e.getMessage());
            connected = false;
            if (dataSource != null) {
                dataSource.close();
            }
            throw new RuntimeException("Database connection failed", e);
        }
    }

    private void initializeDatabaseSchema() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create tables if they don't exist
            String createDestinationsTable = """
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Destinations' AND xtype='U')
                CREATE TABLE Destinations (
                    id INT PRIMARY KEY IDENTITY(1,1),
                    name NVARCHAR(255) NOT NULL,
                    description NVARCHAR(MAX),
                    region NVARCHAR(100),
                    altitude INT,
                    protocol NVARCHAR(MAX),
                    active BIT DEFAULT 1,
                    createdAt DATETIME DEFAULT GETDATE()
                )
                """;
            
            String createToursTable = """
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Tours' AND xtype='U')
                CREATE TABLE Tours (
                    id INT PRIMARY KEY IDENTITY(1,1),
                    name NVARCHAR(255) NOT NULL,
                    description NVARCHAR(MAX),
                    destinationId INT,
                    guideId INT,
                    startDate DATE,
                    endDate DATE,
                    currentParticipants INT DEFAULT 0,
                    maxParticipants INT,
                    residentPrice DECIMAL(10,2),
                    nonResidentPrice DECIMAL(10,2),
                    status NVARCHAR(50) DEFAULT 'PLANNED',
                    createdAt DATETIME DEFAULT GETDATE(),
                    FOREIGN KEY (destinationId) REFERENCES Destinations(id)
                )
                """;
            
            String createUsersTable = """
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Users' AND xtype='U')
                CREATE TABLE Users (
                    id INT PRIMARY KEY IDENTITY(1,1),
                    username NVARCHAR(100) UNIQUE NOT NULL,
                    email NVARCHAR(255) UNIQUE NOT NULL,
                    phone NVARCHAR(20),
                    role NVARCHAR(50) DEFAULT 'USER',
                    createdAt DATETIME DEFAULT GETDATE()
                )
                """;
            
            String createBookingsTable = """
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Bookings' AND xtype='U')
                CREATE TABLE Bookings (
                    id INT PRIMARY KEY IDENTITY(1,1),
                    tourId INT,
                    customerName NVARCHAR(255) NOT NULL,
                    customerEmail NVARCHAR(255) NOT NULL,
                    customerPhone NVARCHAR(20),
                    isResident BIT DEFAULT 0,
                    participantsCount INT,
                    totalPrice DECIMAL(10,2),
                    status NVARCHAR(50) DEFAULT 'PENDING_CONFIRMATION',
                    paymentMethod NVARCHAR(100),
                    paymentReference NVARCHAR(255),
                    bookingDate DATETIME DEFAULT GETDATE(),
                    lastUpdated DATETIME DEFAULT GETDATE(),
                    FOREIGN KEY (tourId) REFERENCES Tours(id)
                )
                """;

            stmt.executeUpdate(createDestinationsTable);
            stmt.executeUpdate(createToursTable);
            stmt.executeUpdate(createUsersTable);
            stmt.executeUpdate(createBookingsTable);
            
            System.out.println("[OK] Database schema initialized successfully");
        } catch (SQLException e) {
            System.err.println("Warning: Could not initialize database schema: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public List<Destination> getAllDestinations() {
        List<Destination> destinations = new ArrayList<>();
        String query = "SELECT * FROM Destinations WHERE active = 1";
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                destinations.add(mapRowToDestination(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching destinations: " + e.getMessage());
        }
        return destinations;
    }

    @Override
    public Destination getDestinationById(int id) {
        String query = "SELECT * FROM Destinations WHERE id = ? AND active = 1";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToDestination(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching destination: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addDestination(Destination destination) {
        String query = "INSERT INTO Destinations (name, description, region, altitude, protocol) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, destination.getName());
            pstmt.setString(2, destination.getDescription());
            pstmt.setString(3, destination.getRegion());
            pstmt.setDouble(4, destination.getAltitude());
            pstmt.setString(5, destination.getEntranceProtocol());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    destination.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding destination: " + e.getMessage());
        }
    }

    @Override
    public void updateDestination(Destination destination) {
        String query = "UPDATE Destinations SET name = ?, description = ?, region = ?, altitude = ?, protocol = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, destination.getName());
            pstmt.setString(2, destination.getDescription());
            pstmt.setString(3, destination.getRegion());
            pstmt.setDouble(4, destination.getAltitude());
            pstmt.setString(5, destination.getEntranceProtocol());
            pstmt.setInt(6, destination.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating destination: " + e.getMessage());
        }
    }

    @Override
    public void deleteDestination(int id) {
        String query = "UPDATE Destinations SET active = 0 WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting destination: " + e.getMessage());
        }
    }

    @Override
    public List<Tour> getAllTours() {
        List<Tour> tours = new ArrayList<>();
        String query = "SELECT t.*, d.name as destName, d.description as destDesc, d.region, d.altitude, d.protocol " +
                      "FROM Tours t LEFT JOIN Destinations d ON t.destinationId = d.id";
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                tours.add(mapRowToTour(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tours: " + e.getMessage());
        }
        return tours;
    }

    @Override
    public Tour getTourById(int id) {
        String query = "SELECT t.*, d.name as destName, d.description as destDesc, d.region, d.altitude, d.protocol " +
                      "FROM Tours t LEFT JOIN Destinations d ON t.destinationId = d.id WHERE t.id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToTour(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tour: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addTour(Tour tour) {
        String query = "INSERT INTO Tours (name, description, destinationId, guideId, startDate, endDate, maxParticipants, residentPrice, nonResidentPrice, status) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, tour.getName());
            pstmt.setString(2, tour.getDescription());
            pstmt.setInt(3, tour.getDestination().getId());
            pstmt.setInt(4, tour.getGuideId());
            pstmt.setDate(5, Date.valueOf(tour.getStartDate()));
            pstmt.setDate(6, Date.valueOf(tour.getEndDate()));
            pstmt.setInt(7, tour.getMaxParticipants());
            pstmt.setDouble(8, tour.getResidentPrice());
            pstmt.setDouble(9, tour.getNonResidentPrice());
            pstmt.setString(10, tour.getStatus().toString());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tour.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding tour: " + e.getMessage());
        }
    }

    @Override
    public void updateTour(Tour tour) {
        String query = "UPDATE Tours SET name = ?, description = ?, destinationId = ?, guideId = ?, startDate = ?, endDate = ?, " +
                      "maxParticipants = ?, residentPrice = ?, nonResidentPrice = ?, currentParticipants = ?, status = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, tour.getName());
            pstmt.setString(2, tour.getDescription());
            pstmt.setInt(3, tour.getDestination().getId());
            pstmt.setInt(4, tour.getGuideId());
            pstmt.setDate(5, Date.valueOf(tour.getStartDate()));
            pstmt.setDate(6, Date.valueOf(tour.getEndDate()));
            pstmt.setInt(7, tour.getMaxParticipants());
            pstmt.setDouble(8, tour.getResidentPrice());
            pstmt.setDouble(9, tour.getNonResidentPrice());
            pstmt.setInt(10, tour.getCurrentParticipants());
            pstmt.setString(11, tour.getStatus().toString());
            pstmt.setInt(12, tour.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating tour: " + e.getMessage());
        }
    }

    @Override
    public void deleteTour(int id) {
        String query = "DELETE FROM Tours WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting tour: " + e.getMessage());
        }
    }

    @Override
    public List<Tour> getToursByDestination(int destinationId) {
        List<Tour> tours = new ArrayList<>();
        String query = "SELECT t.*, d.name as destName, d.description as destDesc, d.region, d.altitude, d.protocol " +
                      "FROM Tours t LEFT JOIN Destinations d ON t.destinationId = d.id WHERE t.destinationId = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, destinationId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tours.add(mapRowToTour(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tours by destination: " + e.getMessage());
        }
        return tours;
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Bookings";
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                bookings.add(mapRowToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
        return bookings;
    }

    @Override
    public Booking getBookingById(int id) {
        String query = "SELECT * FROM Bookings WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToBooking(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching booking: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void addBooking(Booking booking) {
        String query = "INSERT INTO Bookings (tourId, customerName, customerEmail, customerPhone, isResident, participantsCount, totalPrice, status, paymentMethod, paymentReference) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, booking.getTourId());
            pstmt.setString(2, booking.getCustomerName());
            pstmt.setString(3, booking.getCustomerEmail());
            pstmt.setString(4, booking.getCustomerPhone());
            pstmt.setBoolean(5, booking.isResident());
            pstmt.setInt(6, booking.getParticipantsCount());
            pstmt.setDouble(7, booking.getTotalPrice());
            pstmt.setString(8, booking.getStatus().toString());
            pstmt.setString(9, booking.getPaymentMethod());
            pstmt.setString(10, booking.getPaymentReference());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                }
            }
            // Update tour participants
            updateTourParticipants(booking.getTourId(), booking.getParticipantsCount());
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
    }

    @Override
    public void updateBooking(Booking booking) {
        String query = "UPDATE Bookings SET tourId = ?, customerName = ?, customerEmail = ?, customerPhone = ?, " +
                      "isResident = ?, participantsCount = ?, totalPrice = ?, status = ?, paymentMethod = ?, paymentReference = ?, lastUpdated = GETDATE() WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, booking.getTourId());
            pstmt.setString(2, booking.getCustomerName());
            pstmt.setString(3, booking.getCustomerEmail());
            pstmt.setString(4, booking.getCustomerPhone());
            pstmt.setBoolean(5, booking.isResident());
            pstmt.setInt(6, booking.getParticipantsCount());
            pstmt.setDouble(7, booking.getTotalPrice());
            pstmt.setString(8, booking.getStatus().toString());
            pstmt.setString(9, booking.getPaymentMethod());
            pstmt.setString(10, booking.getPaymentReference());
            pstmt.setInt(11, booking.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
        }
    }

    @Override
    public List<Booking> getBookingsByCustomer(String customerEmail) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM Bookings WHERE customerEmail = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, customerEmail);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(mapRowToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings by customer: " + e.getMessage());
        }
        return bookings;
    }

    private void updateTourParticipants(int tourId, int participantCount) {
        String query = "UPDATE Tours SET currentParticipants = currentParticipants + ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, participantCount);
            pstmt.setInt(2, tourId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating tour participants: " + e.getMessage());
        }
    }

    private Destination mapRowToDestination(ResultSet rs) throws SQLException {
        return new Destination(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("region"),
            rs.getInt("altitude"),
            rs.getString("protocol")
        );
    }

    private Tour mapRowToTour(ResultSet rs) throws SQLException {
        Destination destination = new Destination(
            rs.getInt("destinationId"),
            rs.getString("destName"),
            rs.getString("destDesc"),
            rs.getString("region"),
            rs.getDouble("altitude"),
            rs.getString("protocol")
        );
        
        Tour tour = new Tour(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            destination,
            rs.getDate("startDate").toLocalDate(),
            rs.getDate("endDate").toLocalDate(),
            rs.getInt("maxParticipants"),
            rs.getDouble("residentPrice"),
            rs.getDouble("nonResidentPrice")
        );
        
        tour.setCurrentParticipants(rs.getInt("currentParticipants"));
        tour.setGuideId(rs.getInt("guideId"));
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            tour.setStatus(Tour.TourStatus.valueOf(statusStr));
        }
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            tour.setCreatedDate(createdAt.toLocalDateTime());
        }
        return tour;
    }

    private Booking mapRowToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setTourId(rs.getInt("tourId"));
        booking.setCustomerName(rs.getString("customerName"));
        booking.setCustomerEmail(rs.getString("customerEmail"));
        booking.setCustomerPhone(rs.getString("customerPhone"));
        booking.setParticipantsCount(rs.getInt("participantsCount"));
        booking.setTotalPrice(rs.getDouble("totalPrice"));
        booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
        booking.setPaymentMethod(rs.getString("paymentMethod"));
        booking.setPaymentReference(rs.getString("paymentReference"));
        booking.setResident(rs.getBoolean("isResident"));
        
        Timestamp bookingDate = rs.getTimestamp("bookingDate");
        if (bookingDate != null) {
            booking.setBookingDate(bookingDate.toLocalDateTime());
        }
        Timestamp lastUpdated = rs.getTimestamp("lastUpdated");
        if (lastUpdated != null) {
            booking.setLastUpdated(lastUpdated.toLocalDateTime());
        }
        return booking;
    }

    public void closeConnection() {
        if (dataSource != null) {
            dataSource.close();
            connected = false;
            System.out.println("Database connection closed");
        }
    }
}
