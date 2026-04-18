CREATE DATABASE hotel_management;
USE hotel_management;

CREATE TABLE guests (
    guest_id VARCHAR(10) PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    id_proof VARCHAR(50)
);

CREATE TABLE employees (
    employee_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    password VARCHAR(100),
    role VARCHAR(20)
);

CREATE TABLE rooms (
    room_id VARCHAR(10) PRIMARY KEY,
    room_number VARCHAR(10),
    floor INT,
    room_type VARCHAR(30),
    price_per_night DOUBLE,
    status VARCHAR(20) DEFAULT 'AVAILABLE'
);

CREATE TABLE reservations (
    reservation_id VARCHAR(10) PRIMARY KEY,
    guest_id VARCHAR(10),
    room_id VARCHAR(10),
    check_in DATE,
    check_out DATE,
    nights INT,
    number_of_guests INT,
    status VARCHAR(20) DEFAULT 'CREATED',
    FOREIGN KEY (guest_id) REFERENCES guests(guest_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

CREATE TABLE invoices (
    invoice_id VARCHAR(10) PRIMARY KEY,
    reservation_id VARCHAR(10),
    issue_date DATE,
    subtotal DOUBLE,
    tax_amount DOUBLE,
    total_amount DOUBLE,
    paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

CREATE TABLE payments (
    payment_id VARCHAR(10) PRIMARY KEY,
    invoice_id VARCHAR(10),
    amount DOUBLE,
    payment_date DATE,
    method VARCHAR(20),
    status VARCHAR(20),
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
);

CREATE TABLE housekeeping_tasks (
    task_id VARCHAR(10) PRIMARY KEY,
    room_number VARCHAR(10),
    assigned_to VARCHAR(100),
    assigned_date DATE,
    status VARCHAR(20) DEFAULT 'PENDING'
);

CREATE TABLE service_requests (
    request_id VARCHAR(10) PRIMARY KEY,
    guest_id VARCHAR(10),
    request_type VARCHAR(50),
    description VARCHAR(200),
    request_date DATE,
    status VARCHAR(20) DEFAULT 'OPEN',
    FOREIGN KEY (guest_id) REFERENCES guests(guest_id)
);

-- Sample data
INSERT INTO employees VALUES ('E001','Abhing Das','abhing@hotel.com','9000000001','pass123','RECEPTIONIST');
INSERT INTO employees VALUES ('E002','Aarush Lobo','aarush@hotel.com','9000000002','pass456','MANAGER');
INSERT INTO employees VALUES ('E003','Amogh Vaidya','amogh@hotel.com','9000000003','pass789','RECEPTIONIST');
INSERT INTO employees VALUES ('E004','Kusumita','kusu@hotel.com','9000000004','pass000','HOUSEKEEPING');
INSERT INTO employees VALUES ('ADM','Admin','admin@hotel.com','9000000000','admin','ADMIN');

INSERT INTO rooms VALUES ('R001','101',1,'Standard Single',1500,'AVAILABLE');
INSERT INTO rooms VALUES ('R002','102',1,'Standard Double',2000,'AVAILABLE');
INSERT INTO rooms VALUES ('R003','201',2,'Deluxe Double',3000,'AVAILABLE');
INSERT INTO rooms VALUES ('R004','202',2,'Deluxe Suite',4500,'AVAILABLE');
INSERT INTO rooms VALUES ('R005','301',3,'Presidential Suite',8000,'AVAILABLE');

show databases;
USE hotel_management;
SELECT * FROM guests ORDER BY guest_id DESC;
