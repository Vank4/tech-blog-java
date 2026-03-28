SET @drop_stars_sql = (
    SELECT IF(
        EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'product_ratings'
              AND column_name = 'stars'
        ),
        'ALTER TABLE product_ratings DROP COLUMN stars',
        'SELECT 1'
    )
);

PREPARE stmt FROM @drop_stars_sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;