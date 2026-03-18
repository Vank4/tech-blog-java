CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_roles_name UNIQUE (name),
    CONSTRAINT chk_roles_name CHECK (name IN ('ADMIN','AUTHOR','USER'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    avatar_url VARCHAR(500),
    bio VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    email_verified BIT(1) NOT NULL DEFAULT b'0',
    last_login_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE','INACTIVE','BANNED'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    slug VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    type VARCHAR(20) NOT NULL,
    parent_id BIGINT,
    sort_order INT NOT NULL DEFAULT 0,
    is_active BIT(1) NOT NULL DEFAULT b'1',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_categories_slug UNIQUE (slug),
    CONSTRAINT fk_categories_parent FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT chk_categories_type CHECK (type IN ('POST','PRODUCT'))
) ENGINE=InnoDB;

CREATE INDEX idx_categories_type_active ON categories(type, is_active, sort_order);

CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    summary VARCHAR(1000),
    content LONGTEXT NOT NULL,
    thumbnail_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    submitted_at DATETIME(6),
    published_at DATETIME(6),
    view_count BIGINT NOT NULL DEFAULT 0,
    allow_comments BIT(1) NOT NULL DEFAULT b'1',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_posts_slug UNIQUE (slug),
    CONSTRAINT fk_posts_author FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_posts_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT chk_posts_status CHECK (status IN ('DRAFT','PENDING','PUBLISHED','REJECTED','HIDDEN','DELETED'))
) ENGINE=InnoDB;

CREATE INDEX idx_posts_status_published ON posts(status, published_at);
CREATE INDEX idx_posts_author ON posts(author_id);

CREATE TABLE IF NOT EXISTS post_moderation_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    moderator_id BIGINT NOT NULL,
    from_status VARCHAR(20),
    to_status VARCHAR(20) NOT NULL,
    action VARCHAR(20) NOT NULL,
    reason VARCHAR(1000),
    moderated_at DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_post_logs_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_logs_moderator FOREIGN KEY (moderator_id) REFERENCES users(id),
    CONSTRAINT chk_post_logs_action CHECK (action IN ('SUBMIT','APPROVE','REJECT','HIDE','DELETE','UNHIDE'))
) ENGINE=InnoDB;

CREATE INDEX idx_post_logs_post ON post_moderation_logs(post_id, moderated_at);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT,
    created_by BIGINT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    brand VARCHAR(120) NOT NULL,
    model_code VARCHAR(120),
    short_description VARCHAR(1000),
    description LONGTEXT,
    price DECIMAL(15,2),
    currency VARCHAR(3) NOT NULL DEFAULT 'VND',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    published_at DATETIME(6),
    view_count BIGINT NOT NULL DEFAULT 0,
    rating_average DECIMAL(4,2) NOT NULL DEFAULT 0.00,
    rating_count INT NOT NULL DEFAULT 0,
    allow_comments BIT(1) NOT NULL DEFAULT b'1',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_products_slug UNIQUE (slug),
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_products_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT chk_products_status CHECK (status IN ('DRAFT','PUBLISHED','HIDDEN','DELETED'))
) ENGINE=InnoDB;

CREATE INDEX idx_products_status_published ON products(status, published_at);
CREATE INDEX idx_products_brand ON products(brand);

CREATE TABLE IF NOT EXISTS product_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BIT(1) NOT NULL DEFAULT b'0',
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_product_images_product_order ON product_images(product_id, display_order);

CREATE TABLE IF NOT EXISTS product_specs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    spec_key VARCHAR(120) NOT NULL,
    spec_value VARCHAR(500) NOT NULL,
    unit VARCHAR(50),
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_product_specs_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_product_specs_product_order ON product_specs(product_id, display_order);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    summary VARCHAR(1000),
    content LONGTEXT NOT NULL,
    pros TEXT,
    cons TEXT,
    overall_score DECIMAL(4,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    submitted_at DATETIME(6),
    published_at DATETIME(6),
    view_count BIGINT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_reviews_slug UNIQUE (slug),
    CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_author FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT chk_reviews_status CHECK (status IN ('DRAFT','PENDING','PUBLISHED','REJECTED','HIDDEN','DELETED'))
) ENGINE=InnoDB;

CREATE INDEX idx_reviews_product_status ON reviews(product_id, status, published_at);

CREATE TABLE IF NOT EXISTS review_scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT NOT NULL,
    criterion VARCHAR(120) NOT NULL,
    score DECIMAL(4,2) NOT NULL,
    max_score DECIMAL(4,2) NOT NULL,
    note VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_review_scores_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_review_scores_review ON review_scores(review_id, display_order);

CREATE TABLE IF NOT EXISTS review_moderation_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    review_id BIGINT NOT NULL,
    moderator_id BIGINT NOT NULL,
    from_status VARCHAR(20),
    to_status VARCHAR(20) NOT NULL,
    action VARCHAR(20) NOT NULL,
    reason VARCHAR(1000),
    moderated_at DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_review_logs_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_logs_moderator FOREIGN KEY (moderator_id) REFERENCES users(id),
    CONSTRAINT chk_review_logs_action CHECK (action IN ('SUBMIT','APPROVE','REJECT','HIDE','DELETE','UNHIDE'))
) ENGINE=InnoDB;

CREATE INDEX idx_review_logs_review ON review_moderation_logs(review_id, moderated_at);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    target_id BIGINT NOT NULL,
    parent_id BIGINT,
    content TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    like_count INT NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_comments_parent FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE SET NULL,
    CONSTRAINT chk_comments_status CHECK (status IN ('PENDING','VISIBLE','HIDDEN','DELETED')),
    CONSTRAINT chk_comments_target_type CHECK (target_type IN ('POST','PRODUCT','REVIEW'))
) ENGINE=InnoDB;

CREATE INDEX idx_comments_target ON comments(target_type, target_id);
CREATE INDEX idx_comments_parent ON comments(parent_id);

CREATE TABLE IF NOT EXISTS comment_moderation_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT NOT NULL,
    moderator_id BIGINT NOT NULL,
    from_status VARCHAR(20),
    to_status VARCHAR(20) NOT NULL,
    action VARCHAR(20) NOT NULL,
    reason VARCHAR(1000),
    moderated_at DATETIME(6) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_comment_logs_comment FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_logs_moderator FOREIGN KEY (moderator_id) REFERENCES users(id),
    CONSTRAINT chk_comment_logs_action CHECK (action IN ('SUBMIT','APPROVE','REJECT','HIDE','DELETE','UNHIDE'))
) ENGINE=InnoDB;

CREATE INDEX idx_comment_logs_comment ON comment_moderation_logs(comment_id, moderated_at);

CREATE TABLE IF NOT EXISTS comment_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT NOT NULL,
    reporter_id BIGINT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    detail VARCHAR(1000),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    resolved_by BIGINT,
    resolved_at DATETIME(6),
    resolution_note VARCHAR(1000),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_comment_reports_comment FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_reports_reporter FOREIGN KEY (reporter_id) REFERENCES users(id),
    CONSTRAINT fk_comment_reports_resolved_by FOREIGN KEY (resolved_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT chk_comment_reports_status CHECK (status IN ('PENDING','RESOLVED','REJECTED'))
) ENGINE=InnoDB;

CREATE INDEX idx_comment_reports_status ON comment_reports(status, created_at);

CREATE TABLE IF NOT EXISTS product_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating INT NOT NULL,
    review_note VARCHAR(500),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_product_ratings_user_product UNIQUE (user_id, product_id),
    CONSTRAINT fk_product_ratings_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_product_ratings_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT chk_product_ratings_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB;

CREATE INDEX idx_product_ratings_product ON product_ratings(product_id);

CREATE TABLE IF NOT EXISTS ml_models (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    version VARCHAR(50) NOT NULL,
    provider VARCHAR(120),
    description VARCHAR(1000),
    is_active BIT(1) NOT NULL DEFAULT b'0',
    deployed_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_ml_models_name_version UNIQUE (name, version)
) ENGINE=InnoDB;

CREATE INDEX idx_ml_models_active ON ml_models(is_active);

CREATE TABLE IF NOT EXISTS comment_ai_analysis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT NOT NULL,
    model_id BIGINT NOT NULL,
    label VARCHAR(20) NOT NULL,
    confidence DECIMAL(6,4) NOT NULL,
    score DECIMAL(6,4),
    raw_response LONGTEXT,
    analyzed_at DATETIME(6) NOT NULL,
    is_latest BIT(1) NOT NULL DEFAULT b'1',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_comment_ai_analysis_comment_model UNIQUE (comment_id, model_id),
    CONSTRAINT fk_comment_ai_analysis_comment FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_ai_analysis_model FOREIGN KEY (model_id) REFERENCES ml_models(id),
    CONSTRAINT chk_comment_ai_analysis_label CHECK (label IN ('POSITIVE','NEGATIVE','NEUTRAL'))
) ENGINE=InnoDB;

CREATE INDEX idx_comment_ai_analysis_latest ON comment_ai_analysis(comment_id, is_latest);

CREATE TABLE IF NOT EXISTS comment_labels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT NOT NULL,
    labeled_by BIGINT NOT NULL,
    label VARCHAR(20) NOT NULL,
    note VARCHAR(1000),
    is_training_data BIT(1) NOT NULL DEFAULT b'1',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_comment_labels_comment FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_labels_user FOREIGN KEY (labeled_by) REFERENCES users(id),
    CONSTRAINT chk_comment_labels_label CHECK (label IN ('POSITIVE','NEGATIVE','NEUTRAL'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product_sentiment_summary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    total_comments INT NOT NULL DEFAULT 0,
    positive_count INT NOT NULL DEFAULT 0,
    negative_count INT NOT NULL DEFAULT 0,
    neutral_count INT NOT NULL DEFAULT 0,
    positive_ratio DECIMAL(6,2) NOT NULL DEFAULT 0.00,
    negative_ratio DECIMAL(6,2) NOT NULL DEFAULT 0.00,
    neutral_ratio DECIMAL(6,2) NOT NULL DEFAULT 0.00,
    conclusion VARCHAR(30) NOT NULL DEFAULT 'NOT_ENOUGH_DATA',
    last_calculated_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_product_sentiment_summary_product UNIQUE (product_id),
    CONSTRAINT fk_product_sentiment_summary_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT chk_product_sentiment_conclusion CHECK (conclusion IN ('GOOD','BAD','NEUTRAL','NOT_ENOUGH_DATA'))
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ai_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    min_comments_for_conclusion INT NOT NULL,
    good_threshold_percent DECIMAL(6,2) NOT NULL,
    bad_threshold_percent DECIMAL(6,2) NOT NULL,
    auto_approve_enabled BIT(1) NOT NULL DEFAULT b'0',
    auto_approve_min_confidence DECIMAL(6,4) NOT NULL DEFAULT 0.9000,
    active_model_id BIGINT,
    is_active BIT(1) NOT NULL DEFAULT b'1',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_ai_settings_name UNIQUE (name),
    CONSTRAINT fk_ai_settings_active_model FOREIGN KEY (active_model_id) REFERENCES ml_models(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_ai_settings_active ON ai_settings(is_active);

CREATE TABLE IF NOT EXISTS user_favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    target_id BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_user_favorites_user_target UNIQUE (user_id, target_type, target_id),
    CONSTRAINT fk_user_favorites_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_user_favorites_target_type CHECK (target_type IN ('POST','PRODUCT','REVIEW'))
) ENGINE=InnoDB;

CREATE INDEX idx_user_favorites_target ON user_favorites(target_type, target_id);

CREATE TABLE IF NOT EXISTS featured_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    target_type VARCHAR(20) NOT NULL,
    target_id BIGINT NOT NULL,
    priority INT NOT NULL DEFAULT 0,
    start_time DATETIME(6),
    end_time DATETIME(6),
    is_active BIT(1) NOT NULL DEFAULT b'1',
    created_by BIGINT,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_featured_items_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT chk_featured_items_target_type CHECK (target_type IN ('POST','PRODUCT','REVIEW'))
) ENGINE=InnoDB;

CREATE INDEX idx_featured_items_active_time ON featured_items(is_active, start_time, end_time, priority);

CREATE TABLE IF NOT EXISTS compare_lists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_compare_lists_user_name UNIQUE (user_id, name),
    CONSTRAINT fk_compare_lists_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS compare_list_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    compare_list_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    position INT NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_compare_list_items_list_product UNIQUE (compare_list_id, product_id),
    CONSTRAINT uk_compare_list_items_list_position UNIQUE (compare_list_id, position),
    CONSTRAINT fk_compare_list_items_list FOREIGN KEY (compare_list_id) REFERENCES compare_lists(id) ON DELETE CASCADE,
    CONSTRAINT fk_compare_list_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS page_views (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    target_type VARCHAR(20) NOT NULL,
    target_id BIGINT NOT NULL,
    viewed_at DATETIME(6) NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    referrer VARCHAR(500),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_page_views_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT chk_page_views_target_type CHECK (target_type IN ('POST','PRODUCT','REVIEW'))
) ENGINE=InnoDB;

CREATE INDEX idx_page_views_target_viewed_at ON page_views(target_type, target_id, viewed_at);

CREATE TABLE IF NOT EXISTS use_cases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    display_order INT NOT NULL DEFAULT 0,
    is_active BIT(1) NOT NULL DEFAULT b'1',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_use_cases_name UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product_usecase_scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    use_case_id BIGINT NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    note VARCHAR(500),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_product_usecase_scores_product_usecase UNIQUE (product_id, use_case_id),
    CONSTRAINT fk_product_usecase_scores_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_usecase_scores_use_case FOREIGN KEY (use_case_id) REFERENCES use_cases(id) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO roles (name, description)
SELECT 'ADMIN', 'System role ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name, description)
SELECT 'AUTHOR', 'System role AUTHOR'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'AUTHOR');

INSERT INTO roles (name, description)
SELECT 'USER', 'System role USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

INSERT INTO ai_settings (
    name,
    min_comments_for_conclusion,
    good_threshold_percent,
    bad_threshold_percent,
    auto_approve_enabled,
    auto_approve_min_confidence,
    is_active
)
SELECT
    'default',
    10,
    60.00,
    60.00,
    b'0',
    0.9000,
    b'1'
WHERE NOT EXISTS (SELECT 1 FROM ai_settings WHERE name = 'default');

