CREATE TABLE feature_toggle (
   toggle_id VARCHAR(255) PRIMARY KEY,
   enabled BOOLEAN NOT NULL DEFAULT false,
   rules VARCHAR (2048) NOT NULL DEFAULT '
);