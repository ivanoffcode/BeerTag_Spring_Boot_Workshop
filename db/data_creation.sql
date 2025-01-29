INSERT INTO Country (name)
VALUES ('USA'),
       ('Belgium'),
       ('Germany'),
       ('United Kingdom'),
       ('Canada');

INSERT INTO Style (name)
VALUES ('IPA'),
       ('Lager'),
       ('Stout'),
       ('Pilsner'),
       ('Porter');

INSERT INTO Brewery (name, country_id)
VALUES ('Stone Brewing', 1),     -- USA
       ('Trappist Brewery', 2),  -- Belgium
       ('Weihenstephan', 3),     -- Germany
       ('Fuller\'s Brewery', 4), -- UK
       ('Molson Canadian', 5); -- Canada

INSERT INTO Brewery (name, country_id)
VALUES ('Stone Brewing', 1),     -- USA
       ('Trappist Brewery', 2),  -- Belgium
       ('Weihenstephan', 3),     -- Germany
       ('Fuller\'s Brewery', 4), -- United Kingdom
       ('Molson Canadian', 5); -- Canada

INSERT INTO User (username, password, email, is_admin)
VALUES ('john_doe', 'hashed_password_123', 'john.doe@example.com', TRUE),
       ('jane_smith', 'hashed_password_456', 'jane.smith@example.com', FALSE),
       ('michael_brewer', 'hashed_password_789', 'michael.brewer@example.com', FALSE),
       ('sarah_connoisseur', 'hashed_password_321', 'sarah.connoisseur@example.com', FALSE),
       ('alice_taster', 'hashed_password_654', 'alice.taster@example.com', FALSE);

INSERT INTO Beer (name, description, style_id, abv, brewery_id, created_by)
VALUES ('Arrogant Bastard', 'Strong ale with bold flavors.', 1, 7.2, 1, 1),
       ('Westvleteren 12', 'Rich and complex quadrupel.', 3, 10.2, 2, 2),
       ('Hefeweissbier', 'Refreshing wheat beer with hints of banana.', 2, 5.4, 3, 3),
       ('London Pride', 'Smooth and balanced amber ale.', 5, 4.7, 4, 4),
       ('Molson Export', 'Crisp lager brewed in Canada.', 4, 5.0, 5, 5);

