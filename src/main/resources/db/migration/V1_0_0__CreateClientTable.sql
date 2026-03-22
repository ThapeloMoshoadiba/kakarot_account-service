CREATE TABLE client
(
    client_id                            UUID PRIMARY KEY,
    created_at                           TIMESTAMP WITH TIME ZONE,
    updated_at                           TIMESTAMP WITH TIME ZONE,
    cif_number                           VARCHAR(17) NOT NULL,
    title                                VARCHAR(5),
    first_name                           VARCHAR(20),
    middle_name                          VARCHAR(20),
    last_name                            VARCHAR(35),
    id_number                            VARCHAR(13),
    gender                               VARCHAR(10),
    date_of_birth                        DATE,
    address                              VARCHAR(100),
    cellphone_number                     VARCHAR(12),
    email                                VARCHAR(100),
    credit                               VARCHAR(15),
    employment_status                    VARCHAR(40),
    source_of_funds                      VARCHAR(40),
    verified_annual_income               VARCHAR(40)
);

CREATE INDEX client_cif_number_idx ON client (cif_number);
