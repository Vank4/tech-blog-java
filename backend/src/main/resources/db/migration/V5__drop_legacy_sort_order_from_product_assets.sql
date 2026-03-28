SET @sql = (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'product_images'
              AND column_name = 'sort_order'
        ),
        'UPDATE product_images SET display_order = sort_order WHERE display_order = 0 AND sort_order <> 0',
        'SELECT 1'
    )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'product_images'
              AND column_name = 'sort_order'
        ),
        'ALTER TABLE product_images DROP COLUMN sort_order',
        'SELECT 1'
    )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'product_specs'
              AND column_name = 'sort_order'
        ),
        'UPDATE product_specs SET display_order = sort_order WHERE display_order = 0 AND sort_order <> 0',
        'SELECT 1'
    )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'product_specs'
              AND column_name = 'sort_order'
        ),
        'ALTER TABLE product_specs DROP COLUMN sort_order',
        'SELECT 1'
    )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;