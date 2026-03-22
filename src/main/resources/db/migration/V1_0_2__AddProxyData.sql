INSERT INTO client (
    client_id, created_at, updated_at, cif_number, title,
    first_name, middle_name, last_name, id_number, gender,
    date_of_birth, address, cellphone_number, email,
    credit, employment_status, source_of_funds, verified_annual_income
) VALUES
      ('11111111-1111-1111-1111-111111111111', NOW(), null, '123456789', 'Mr',
       'Tony', null, 'Stark', '9001015009087', 'MALE',
       '1990-01-01', '12 Main Street', '0712345678', 'tony.stark@starkenterprises.com',
       'EXCELLENT', 'SELF_EMPLOYED', 'SHARES', '1000000000'),

      ('22222222-2222-2222-2222-222222222222', NOW(), null, '223456789', 'Mr',
       'Steve', null, 'Rogers', '9202156009088', 'MALE',
       '1992-02-15', '45 Oak Avenue', '0723456789', 'steve.rogers@shield.com',
       'AVERAGE', 'UNEMPLOYED', 'NONE', '0'),

      ('33333333-3333-3333-3333-333333333333', NOW(), null, '323456789', 'Dr',
       'Bruce', null, 'Banner', '8807105800082', 'MALE',
       '1988-07-10', '78 Market Road', '0734567890', 'bruce.banner@sakar.com',
       'GOOD', 'EMPLOYED', 'SALARY', '700000'),

      ('44444444-4444-4444-4444-444444444444', NOW(), null, '423456789', 'Ms',
       'Natasha', null, 'Romanoff', '9503046201089', 'FEMALE',
       '1995-03-04', '9 Pine Street','0745678901', 'nat.romanoff@shield.com',
       'EXCELLENT', 'EMPLOYED', 'SALARY', '2500000'),

      ('55555555-5555-5555-5555-555555555555', NOW(), null, '987654321', 'Mr',
       'Thor', null, 'Odinson', '8509225109084', 'MALE',
       '1985-09-22', '101 Cedar Lane', '0756789012', 'thor.odinsom@asgard.com',
       'BAD', 'UNEMPLOYED', 'NONE', '0');

INSERT INTO account (
    account_id, created_at, cif_number, account_number,
    account_status, initial_credit_amount, closed_at, reason_for_close
) VALUES
      ('11111111-6666-1111-1111-111111111111', NOW(), 123456789, '11111111-6666-6666-1111-111111111111', 'OPEN',
       '1000000', null, null);
