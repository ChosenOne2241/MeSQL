/* Author: Yungchen Jen
 * Description: It is a demo SQL file and can be loaded by `Shell.jar`.
 */

crEaTe taBle contacts(ID INT AUTO_INCREMENT, Username CHAR(15) not NuLl, Age INT, Phone CHAR(20));
-- The keywords here are case-insensitive.
INSERT into contacts(Username, Age, Phone) VALUES('WhyMe:=)', 27, "+1 (204) 389-2621");
-- Notice we use two types of quotes in the same statement.
INSERT INTO contacts(Username, Age, Phone) VALUES('$#(@_@)#_{Ah!}', 68, '+1 (204) 688-1253');
INSERT INTO contacts(Username, Age, Phone) VALUES('Hal0InfGod2012', 10, '+1 (647) 243-0059');
INSERT INTO contacts(Username, Age, Phone) VALUES('AntiWork4ever', 34, '+1 (124) 777-2213');
SELECT * FROM contacts WHERE ID <= 3;
SELECT * FROM contacts WHERE Username LIKE "%nf%";
UPDATE contacts SET Username = 'AntiWorkSucks' WHERE Phone LIKE "%213";
SELECT * FROM contacts;
DELETE FROM contacts where username = "Hal0InfGod2012";
SELECT * FROM contacts;
DROP TABLE contacts;
-- SELECT * FROM contacts;
SELECT * FROM mesql.tables;
