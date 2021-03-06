create table purchases
(id bigint generated by default as identity,
 user_id bigint,
 user_address_id bigint,
 book_id bigint,
 quantity bigint,
 total_price bigint,
 order_date DATE NOT NULL,
 payment_mode varchar(255) not null,
 payment_status varchar(255) not null,
 primary key (id),
 CONSTRAINT fk_book_purchases FOREIGN KEY(book_id) references books(id),
 CONSTRAINT fk_address_purchases FOREIGN KEY(user_address_id) references addresses(id),
 CONSTRAINT fk_user_purchases FOREIGN KEY(user_id) references users(id))
