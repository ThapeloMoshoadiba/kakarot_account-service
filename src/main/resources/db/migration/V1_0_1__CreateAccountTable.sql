CREATE TABLE account
(
    account_id                           UUID PRIMARY KEY,
    created_at                           TIMESTAMP WITH TIME ZONE,
    cif_number                           VARCHAR(17) NOT NULL,
    account_number                       UUID NOT NULL,
    account_status                       VARCHAR(10) NOT NULL,
    initial_credit_amount                VARCHAR(17) NOT NULL,
    closed_at                            TIMESTAMP WITH TIME ZONE NULL,
    reason_for_close                     VARCHAR(30) NULL
);

CREATE INDEX account_account_number_idx ON account (account_number);
