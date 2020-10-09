INSERT INTO USERENTITY (userId, firstName, lastName, ssn, address, email, phoneNumber, username, password)
VALUES (1, 'Shane', 'Reilly', '123-45-6789', '123 Street Rd, Wilmington, DE 19803', 'shane@gmail.com', '302-555-5555', 'Codingrulez67', 'Shm00ples!');

INSERT INTO ACCOUNTENTITY (accountNumber, type, routingNumber, balance, userId, nickname)
VALUES (100, 1, 394058927, 2000.00, 1, 'House Down Payment');

INSERT INTO ACCOUNTENTITY (accountNumber, type, routingNumber, balance, userId)
VALUES (104, 0, 394058927, 1000.00, 1);

