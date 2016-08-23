LOAD DATA LOCAL INFILE '/Users/arindamnath/Desktop/banks_all.csv' 
INTO TABLE lender_.bank_codes 
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n';