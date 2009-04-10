create table if not exists voucher_buys(
		id int primary key auto_increment,
        number varchar(50),
		voucher_type int,
		status int);



create table if not exists voucher_types(
		id int primary key auto_increment,
        name varchar(50));
		