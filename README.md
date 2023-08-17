# BeautyParlorAppointment_usingJdbc
It is a simple Beauty Parlor appointment booking system using jdbc and mysql for database.
Step 1) create database beautyparlor and create all the table given below
1) customers table
+---------+-------------+------+-----+---------+----------------+
| Field   | Type        | Null | Key | Default | Extra          |
+---------+-------------+------+-----+---------+----------------+
| id      | int         | NO   | PRI | NULL    | auto_increment |
| name    | varchar(25) | YES  |     | NULL    |                |
| email   | varchar(25) | YES  |     | NULL    |                |
| phone   | varchar(25) | YES  |     | NULL    |                |
| address | varchar(25) | YES  |     | NULL    |                |
+---------+-------------+------+-----+---------+----------------+.

2) beauticians table
   +----------------+-------------+------+-----+---------+----------------+
| Field          | Type        | Null | Key | Default | Extra          |
+----------------+-------------+------+-----+---------+----------------+
| id             | int         | NO   | PRI | NULL    | auto_increment |
| name           | varchar(25) | YES  |     | NULL    |                |
| specialization | varchar(25) | YES  |     | NULL    |                |
+----------------+-------------+------+-----+---------+----------------+

3) services table
+-------+-------------+------+-----+---------+----------------+
| Field | Type        | Null | Key | Default | Extra          |
+-------+-------------+------+-----+---------+----------------+
| id    | int         | NO   | PRI | NULL    | auto_increment |
| name  | varchar(25) | YES  |     | NULL    |                |
| price | double      | YES  |     | NULL    |                |
+-------+-------------+------+-----+---------+----------------+

4) appointments table
+---------------+-------------+------+-----+---------+----------------+
| Field         | Type        | Null | Key | Default | Extra          |
+---------------+-------------+------+-----+---------+----------------+
| id            | int         | NO   | PRI | NULL    | auto_increment |
| beautician_id | int         | YES  |     | NULL    |                |
| service_id    | int         | YES  |     | NULL    |                |
| date          | varchar(25) | YES  |     | NULL    |                |
| time          | varchar(25) | YES  |     | NULL    |                |
+---------------+-------------+------+-----+---------+----------------+
