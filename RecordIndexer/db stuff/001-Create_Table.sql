CREATE TABLE users(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	firstName	String 			NOT NULL,
	lastName	String			NOT NULL,
	email 		String			NOT NULL,
	userName	String			UNIQUE NOT NULL,
	password	String 			NOT NULL,
	indexedRecords Integer	default 0	NOT NULL
);

CREATE TABLE projects(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	title 		String 			NOT NULL,
	recordsPerImage Integer 	NOT NULL,
	firstYCoor 	Integer			NOT NULL,
	recordHeight 	Integer		NOT NULL
);

CREATE TABLE fields(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	position	Integer 		NOT NULL,
	title 		String 			NOT NULL,
	xcoor		Integer 		NOT NULL,
	width 		Integer 		NOT NULL,
	helpHtml 	String 			NULL,
	knownData 	String 			NULL,
	projectId 	Integer 		NOT NULL,
	FOREIGN KEY(projectId) 		REFERENCES projects(id)
);

CREATE TABLE images(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	file 		String 			NOT NULL,
	projectId 	Integer 		NOT NULL,
	userId 		Integer 		NULL,
	finished 	Integer default 0 NOT NULL,
	FOREIGN KEY(projectId) 		REFERENCES projects(id),
	FOREIGN KEY(userId) 		REFERENCES users(id)
);

CREATE TABLE records(
	id			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	imageId 	Integer			NOT NULL,
	FOREIGN KEY(imageId)		REFERENCES images(id)
);

CREATE TABLE fieldValues(
	id 			Integer 		PRIMARY KEY AUTOINCREMENT NOT NULL,
	recordId 	Integer 		NOT NULL,
	fieldId 	Integer 		NOT NULL,
	value 		String			NOT NULL,
	FOREIGN KEY(fieldId) 		REFERENCES fields(id),
	FOREIGN KEY(recordId) 		REFERENCES records(id)
);








