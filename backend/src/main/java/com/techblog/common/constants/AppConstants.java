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

    // Collections (MongoDB)
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_POSTS = "posts";
    public static final String COLLECTION_CATEGORIES = "categories";
    public static final String COLLECTION_PRODUCTS = "products";
    public static final String COLLECTION_COMMENTS = "comments";
    public static final String COLLECTION_REVIEWS = "reviews";
    public static final String COLLECTION_RATINGS = "ratings";

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

}
