ALTER TABLE app_users
ADD COLUMN customer_id VARCHAR(100);

CREATE UNIQUE INDEX uk_app_users_customer_id
ON app_users(customer_id)
WHERE customer_id IS NOT NULL;