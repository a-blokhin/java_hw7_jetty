CREATE TABLE product
(
    id       SERIAL  NOT NULL,
    name     VARCHAR NOT NULL,
    company  VARCHAR,
    quantity INT NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (id)
);