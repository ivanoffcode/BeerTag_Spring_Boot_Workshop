-- Create the 'Country' table
CREATE TABLE Country
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE CHECK (CHAR_LENGTH(name) BETWEEN 2 AND 50) -- Country name must be unique and valid length
);

-- Create the 'Style' table
CREATE TABLE Style
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE CHECK (CHAR_LENGTH(name) BETWEEN 2 AND 20) -- Style name must be unique and valid length
);

-- Create the 'Brewery' table
CREATE TABLE Brewery
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(20) NOT NULL UNIQUE CHECK (CHAR_LENGTH(name) BETWEEN 2 AND 20), -- Brewery name must be unique and valid length
    country_id INT         NOT NULL,                                                   -- Reference to Country table

    -- Foreign key relationship with Country
    CONSTRAINT fk_brewery_country FOREIGN KEY (country_id) REFERENCES Country (id) ON DELETE CASCADE
);

-- Create the 'User' table
CREATE TABLE User
(
    id       INT AUTO_INCREMENT PRIMARY KEY,                               -- Primary key for User
    username VARCHAR(50)  NOT NULL UNIQUE,                                 -- Username must be unique and not null
    password VARCHAR(255) NOT NULL,                                        -- Password (hashed, length adjusted for security)
    email    VARCHAR(255) NOT NULL UNIQUE CHECK (email LIKE '%_@__%.__%'), -- Email must be unique and valid format
    is_admin BOOLEAN      NOT NULL DEFAULT FALSE                           -- Boolean field to indicate admin status
);

-- Create the 'Beer' table
CREATE TABLE Beer
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE CHECK (CHAR_LENGTH(name) BETWEEN 2 AND 100), -- Beer name must be unique and valid length
    description TEXT         NOT NULL,                                                    -- Beer description
    style_id    INT          NOT NULL,                                                    -- Foreign key to Style table
    abv         DOUBLE       NOT NULL CHECK (abv > 0),                                    -- Alcohol by volume must be positive
    brewery_id  INT          NOT NULL,                                                    -- Foreign key to Brewery table
    created_by  INT          NOT NULL,                                                    -- Foreign key to User table
-- Define foreign key relationships
    CONSTRAINT fk_beer_style FOREIGN KEY (style_id) REFERENCES Style (id) ON DELETE CASCADE,
    CONSTRAINT fk_beer_brewery FOREIGN KEY (brewery_id) REFERENCES Brewery (id) ON DELETE CASCADE,
    CONSTRAINT fk_beer_user FOREIGN KEY (created_by) REFERENCES User (id) ON DELETE CASCADE
);

