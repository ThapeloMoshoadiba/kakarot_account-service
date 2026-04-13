CREATE TABLE account
(
    account_id                          UUID PRIMARY KEY,
    created_at                          TIMESTAMP NOT NULL,
    cif_number                          VARCHAR(17) NOT NULL,
    account_number                      UUID NOT NULL,
    account_status                      VARCHAR(10) NOT NULL,
    initial_credit_amount               NUMERIC(18, 2) NOT NULL,
    blocked_at                          TIMESTAMP,
    reason_for_block                    VARCHAR(30),
    unblocked_at                        TIMESTAMP,
    reason_for_unblock                  VARCHAR(30),
    closed_at                           TIMESTAMP,
    reason_for_close                    VARCHAR(30)
);

CREATE INDEX account_account_number_idx ON account (account_number);
