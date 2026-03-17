package com.techblog.common.constants;

/**
 * Application Constants
 * Chứa các hằng số dùng chung trong toàn bộ dự án.
 */
public final class AppConstants {

    private AppConstants() {
        // Không cho khởi tạo
    }

    // Pagination
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_AUTHOR = "AUTHOR";
    public static final String ROLE_GUEST = "GUEST";

    // Database Tables (MySQL)
    public static final String TABLE_USERS = "users";
    public static final String TABLE_POSTS = "posts";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_COMMENTS = "comments";
    public static final String TABLE_REVIEWS = "reviews";
    public static final String TABLE_RATINGS = "ratings";
    public static final String TABLE_PRODUCT_SPECS = "product_specs";
    public static final String TABLE_PRODUCT_IMAGES = "product_images";
    public static final String TABLE_MODERATION_LOGS = "moderation_logs";

    // API Paths
    public static final String API_BASE = "/api/v1";
    public static final String API_AUTH = API_BASE + "/auth";
    public static final String API_USERS = API_BASE + "/users";
    public static final String API_POSTS = API_BASE + "/posts";
    public static final String API_CATEGORIES = API_BASE + "/categories";
    public static final String API_PRODUCTS = API_BASE + "/products";
    public static final String API_COMMENTS = API_BASE + "/comments";
    public static final String API_REVIEWS = API_BASE + "/reviews";
    public static final String API_RATINGS = API_BASE + "/ratings";
    public static final String API_ADMIN = API_BASE + "/admin";

    // Post Status
    public static final String POST_STATUS_DRAFT = "DRAFT";
    public static final String POST_STATUS_PENDING = "PENDING";
    public static final String POST_STATUS_PUBLISHED = "PUBLISHED";
    public static final String POST_STATUS_REJECTED = "REJECTED";

    // Comment Status
    public static final String COMMENT_STATUS_PENDING = "PENDING";
    public static final String COMMENT_STATUS_APPROVED = "APPROVED";
    public static final String COMMENT_STATUS_HIDDEN = "HIDDEN";
    public static final String COMMENT_STATUS_REPORTED = "REPORTED";

    // Sentiment Labels (AI)
    public static final String SENTIMENT_POSITIVE = "POSITIVE";
    public static final String SENTIMENT_NEGATIVE = "NEGATIVE";
    public static final String SENTIMENT_NEUTRAL = "NEUTRAL";

    // File Upload
    public static final long MAX_FILE_SIZE = 10_000_000; // 10MB
    public static final String UPLOAD_PATH = "uploads/";

}

