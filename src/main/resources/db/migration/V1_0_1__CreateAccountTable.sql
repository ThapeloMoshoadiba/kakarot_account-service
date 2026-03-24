CREATE TABLE account
(
    account_id                          UUID PRIMARY KEY,
    created_at                          TIMESTAMP,
    cif_number                          VARCHAR(17) NOT NULL,
    account_number                      UUID NOT NULL,
    account_status                      VARCHAR(10) NOT NULL,
    initial_credit_amount               NUMERIC(18, 2),
    blocked_at                          TIMESTAMP NULL,
    reason_for_block                    VARCHAR(30) NULL,
    unblocked_at                        TIMESTAMP NULL,
    reason_for_unblock                  VARCHAR(30) NULL,
    closed_at                           TIMESTAMP NULL,
    reason_for_close                    VARCHAR(30) NULL
);

CREATE INDEX account_account_number_idx ON account (account_number);
