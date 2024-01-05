DELIMITER $$

INSERT INTO users(user_id, account_id, user_image_url, nickname, provider, user_role)
VALUES (1, 'account_id', 'image_url', '똑똑한 프로도', 'GOOGLE', 'USER');

INSERT INTO categories(category_id, category_name)
values (1, 'HOME_ELECTRONICS');
insert into categories(category_id, category_name)
values (2, 'MALE_CLOTHES');
insert into categories(category_id, category_name)
values (3, 'FEMALE_CLOTHES');
insert into categories(category_id, category_name)
values (4, 'GOODS_ACCESSORY');
insert into categories(category_id, category_name)
values (5, 'SHOES');
insert into categories(category_id, category_name)
values (6, 'SPORTS');
insert into categories(category_id, category_name)
values (7, 'BOOKS');
insert into categories(category_id, category_name)
values (8, 'ELECTRONICS');
insert into categories(category_id, category_name)
values (9, 'FURNITURE_INTERIOR');

INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (1, 'item1', 'PRICE_RANGE_TWO', 1);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (2, 'item1', 'PRICE_RANGE_TWO', 2);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (3, 'item1', 'PRICE_RANGE_TWO', 3);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (4, 'item1', 'PRICE_RANGE_TWO', 4);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (5, 'item1', 'PRICE_RANGE_TWO', 5);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (6, 'item1', 'PRICE_RANGE_TWO', 6);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (7, 'item1', 'PRICE_RANGE_TWO', 7);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (8, 'item1', 'PRICE_RANGE_TWO', 8);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (9, 'item1', 'PRICE_RANGE_TWO', 9);
INSERT INTO items(item_id, item_name, price_range, category_id)
VALUES (10, 'item1', 'PRICE_RANGE_TWO', 3);


DROP PROCEDURE IF EXISTS insertDummyData$$

CREATE PROCEDURE insertDummyData()
BEGIN
    DECLARE i INT DEFAULT 10001;

    WHILE i <= 100000
        DO
            INSERT INTO cards(card_id, card_title, thumbnail, content, user_id, dib_count, is_active, item_id,
                              poke_available, status, trade_area, trade_type, view_count, created_date, modified_date)
            VALUES (i, concat('제목', i), i, concat('내용', i), 1, 0, 1, 1 + FLOOR(RAND() * 9), 0, 'TRADE_AVAILABLE',
                    '서울 성동구', 'DIRECT_DEALING', 0, NOW(), NOW());
            SET i = i + 1;

        END WHILE;
END$$
DELIMITER $$

CALL insertDummyData;

set FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE cards;
set FOREIGN_KEY_CHECKS = 1;

explain
select card0_.card_id       as col_0_0_,
       card0_.card_title    as col_1_0_,
       item1_.item_name     as col_2_0_,
       item1_.price_range   as col_3_0_,
       card0_.thumbnail     as col_4_0_,
       card0_.status        as col_5_0_,
       card0_.created_date  as col_6_0_,
       card0_.modified_date as col_7_0_
from cards card0_
         left outer join items item1_ on (card0_.item_id = item1_.item_id)
where card0_.is_active = 1
order by card0_.created_date desc, card0_.card_id desc
limit 10
$$
