INSERT INTO user_roles(role) VALUES
	('ADMIN'), ('FINANCE MANAGER'), ('EMPLOYEE');

INSERT INTO reimbursement_statuses(status) VALUES
	('PENDING'), ('APPROVED'), ('DENIED');

INSERT INTO reimbursement_types (type) VALUES
	('LODGING'), ('TRAVEL'), ('FOOD'), ('OTHER');

INSERT INTO users VALUES
	('1', 'mrnoms', 'mail', 'pass', 'ray', 'thom', TRUE, 'ADMIN');