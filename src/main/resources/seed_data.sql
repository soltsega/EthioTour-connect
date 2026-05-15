-- EthioTour Seed Data
-- Destinations
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (1, 'Lalibela Rock Churches', '11 medieval monolithic cave churches in a mountainous region.', 'Amhara', 2500, 'Modest dress required.', 1);
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (2, 'Simien Mountains', 'Stunning mountain landscapes and endemic wildlife.', 'Amhara', 4500, 'Park scout mandatory.', 1);
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (3, 'Axum Obelisks', 'Ancient stelae marking the heart of the Axumite Empire.', 'Tigray', 2100, 'Museum pass required.', 1);
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (4, 'Harar Jugol', 'The walled city with unique cultural traditions.', 'Harari', 1885, 'Evening hyena feeding.', 1);
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (5, 'Bale Mountains', 'Home to the Ethiopian Wolf and rare Afro-alpine flora.', 'Oromia', 4377, '4x4 vehicle recommended.', 1);
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (6, 'Danakil Depression', 'The hottest and lowest place on Earth.', 'Afar', -125, 'Guided expeditions only.', 1);
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (7, 'Blue Nile Falls', 'Dramatic waterfalls on the Blue Nile River.', 'Amhara', 1788, 'Best seen in wet season.', 1);
INSERT OR IGNORE INTO Destinations (id, name, description, region, altitude, protocol, active) VALUES (8, 'Lake Tana Monasteries', 'Historic island monasteries with ancient manuscripts.', 'Amhara', 1840, 'Boat rental needed.', 1);

-- Tours
INSERT OR IGNORE INTO Tours (id, name, description, destinationId, startDate, endDate, maxParticipants, residentPrice, nonResidentPrice, status) VALUES (1, 'Spiritual Lalibela', 'A 3-day spiritual tour of the rock churches.', 1, '2026-06-15', '2026-06-18', 12, 3500.0, 15000.0, 'PLANNED');
INSERT OR IGNORE INTO Tours (id, name, description, destinationId, startDate, endDate, maxParticipants, residentPrice, nonResidentPrice, status) VALUES (2, 'Summit of Africa Trek', '5-day trek in the Simien Mountains.', 2, '2026-07-01', '2026-07-06', 8, 6000.0, 28000.0, 'PLANNED');
INSERT OR IGNORE INTO Tours (id, name, description, destinationId, startDate, endDate, maxParticipants, residentPrice, nonResidentPrice, status) VALUES (3, 'Gates of Harar', 'A cultural walk through the ancient city.', 4, '2026-06-20', '2026-06-22', 15, 2000.0, 8000.0, 'PLANNED');
INSERT OR IGNORE INTO Tours (id, name, description, destinationId, startDate, endDate, maxParticipants, residentPrice, nonResidentPrice, status) VALUES (4, 'The Afar Adventure', 'Explore the salt flats and Erta Ale volcano.', 6, '2026-08-10', '2026-08-14', 6, 18000.0, 75000.0, 'PLANNED');

-- Users
INSERT OR IGNORE INTO Users (id, username, email, phone, role) VALUES (1, 'admin', 'admin@ethiotour.com', '+251911000000', 'ADMIN');
INSERT OR IGNORE INTO Users (id, username, email, phone, role) VALUES (2, 'tsega_guide', 'tsega@ethiotour.com', '+251911223344', 'TOUR_GUIDE');

-- Bookings
INSERT OR IGNORE INTO Bookings (tourId, customerName, customerEmail, customerPhone, isResident, participantsCount, totalPrice, status) VALUES (1, 'John Doe', 'john@example.com', '+123456789', 0, 2, 30000.0, 'CONFIRMED');
INSERT OR IGNORE INTO Bookings (tourId, customerName, customerEmail, customerPhone, isResident, participantsCount, totalPrice, status) VALUES (2, 'Abebe Bikila', 'abebe@example.com', '+251911911911', 1, 1, 6000.0, 'CONFIRMED');
