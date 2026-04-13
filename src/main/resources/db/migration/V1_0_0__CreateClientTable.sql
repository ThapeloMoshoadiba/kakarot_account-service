CREATE TABLE client
(
    client_id                           UUID PRIMARY KEY,
    created_at                          TIMESTAMP NOT NULL,
    updated_at                          TIMESTAMP,
    cif_number                          VARCHAR(17) NOT NULL,
    title                               VARCHAR(5) NOT NULL,
    first_name                          VARCHAR(20) NOT NULL,
    middle_name                         VARCHAR(20),
    last_name                           VARCHAR(35) NOT NULL,
    id_number                           VARCHAR(13) NOT NULL,
    gender                              VARCHAR(10) NOT NULL,
    date_of_birth                       DATE NOT NULL,
    address                             VARCHAR(100) NOT NULL,
    cellphone_number                    VARCHAR(12),
    email                               VARCHAR(100),
    credit                              VARCHAR(15),
    employment_status                   VARCHAR(40) NOT NULL,
    source_of_funds                     VARCHAR(40) NOT NULL,
    verified_annual_income              NUMERIC(18, 2) NOT NULL,
    client_status                       VARCHAR(40) NOT NULL,
    blocked_at                          TIMESTAMP,
    reason_for_block                    VARCHAR(100),
    unblocked_at                        TIMESTAMP,
    reason_for_unblock                  VARCHAR(100)
);

CREATE INDEX client_cif_number_idx ON client (cif_number);
