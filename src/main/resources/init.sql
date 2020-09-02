CREATE TABLE QUOTE(
  SYMBOL VARCHAR COMMENT 'The symbol the quote is for.',
  DAY DATE COMMENT 'The date of the quote.',
  BID DECIMAL COMMENT 'The bid of the quote.',
  ASK DECIMAL COMMENT 'The ask of the quote.',
  PRIMARY KEY(SYMBOL, DAY)
);

INSERT INTO QUOTE(SYMBOL, DAY, BID, ASK) VALUES
  -- Microsoft
  ('MSFT', '2020-01-1', 2.03, 2.60),
  ('MSFT', '2020-01-2', 2.91, 3.33),
  ('MSFT', '2020-01-3', 3.74, 4.33),
  ('MSFT', '2020-01-4', 3.87, 4.10),
  ('MSFT', '2020-01-5', 2.51, 2.96),

  -- Google
  ('GOOG', '2020-01-1', 3.11, 3.34),
  ('GOOG', '2020-01-2', 3.97, 4.56),
  ('GOOG', '2020-01-3', 5.59, 5.85),
  ('GOOG', '2020-01-4', 4.94, 5.28),
  ('GOOG', '2020-01-5', 0.67, 0.81),

  -- Facebook
  ('FB', '2020-01-1', 5.54, 5.66),
  ('FB', '2020-01-2', 4.89, 5.40),
  ('FB', '2020-01-3', 2.90, 3.31),
  ('FB', '2020-01-4', 6.12, 6.33),
  ('FB', '2020-01-5', 3.68, 3.93);
