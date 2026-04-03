INSERT INTO categories (name, slug, description, type, sort_order, is_active, created_at, updated_at)
SELECT
    'Hardware',
    'hardware',
    'Tin tuc va bai viet ve phan cung hieu nang cao.',
    'POST',
    1,
    b'1',
    NOW(),
    NOW()
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE slug = 'hardware'
);

INSERT INTO categories (name, slug, description, type, sort_order, is_active, created_at, updated_at)
SELECT
    'AI Reviews',
    'ai-reviews',
    'Noi dung danh gia va phan tich ung dung AI trong cong nghe.',
    'POST',
    2,
    b'1',
    NOW(),
    NOW()
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE slug = 'ai-reviews'
);

INSERT INTO tags (name, slug, created_at, updated_at)
SELECT 'Laptop', 'laptop', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM tags WHERE slug = 'laptop'
);

INSERT INTO tags (name, slug, created_at, updated_at)
SELECT 'AI', 'ai', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM tags WHERE slug = 'ai'
);

INSERT INTO tags (name, slug, created_at, updated_at)
SELECT 'Benchmark', 'benchmark', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM tags WHERE slug = 'benchmark'
);

INSERT INTO tags (name, slug, created_at, updated_at)
SELECT 'Ultrabook', 'ultrabook', NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM tags WHERE slug = 'ultrabook'
);

INSERT INTO posts (
    author_id,
    category_id,
    title,
    slug,
    summary,
    content,
    thumbnail_url,
    status,
    submitted_at,
    published_at,
    view_count,
    allow_comments,
    is_featured,
    priority,
    created_at,
    updated_at
)
SELECT
    u.id,
    c.id,
    'Cuoc dua ultrabook AI nam 2026',
    'cuoc-dua-ultrabook-ai-2026',
    'So sanh nhanh ba mau ultrabook noi bat ve pin, nhiet do va kha nang xu ly AI tai cho.',
    '<p>Thi truong ultrabook 2026 dang dich chuyen rat nhanh ve huong may mong nhe nhung van du suc xu ly AI tai thiet bi. Bai viet nay tong hop nhung diem manh dang chu y nhat ve pin, nhiet do, hieu nang thuc te va tinh co dong.</p><p>Neu ban uu tien su can bang giua pin, do on dinh va man hinh dep, day la nhom san pham dang rat dang xem.</p>',
    'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=1400&q=80',
    'PUBLISHED',
    NOW(),
    NOW(),
    128,
    b'1',
    b'1',
    10,
    NOW(),
    NOW()
FROM (SELECT id FROM users ORDER BY id LIMIT 1) u
JOIN (SELECT id FROM categories WHERE slug = 'hardware' LIMIT 1) c
WHERE NOT EXISTS (
    SELECT 1 FROM posts WHERE slug = 'cuoc-dua-ultrabook-ai-2026'
);

INSERT INTO posts (
    author_id,
    category_id,
    title,
    slug,
    summary,
    content,
    thumbnail_url,
    status,
    submitted_at,
    published_at,
    view_count,
    allow_comments,
    is_featured,
    priority,
    created_at,
    updated_at
)
SELECT
    u.id,
    c.id,
    'RTX 5080 co thuc su dang nang cap khong',
    'rtx-5080-co-thuc-su-dang-nang-cap-khong',
    'Tong hop benchmark som, nhiet do va muc tieu thu dien de xem dong GPU moi co dang de xuong tien.',
    '<p>Day card moi mang lai buoc nhay hieu nang tot hon trong cac bai test raster va ray tracing, nhung gia ban va muc dien nang van la bai toan can can nhac.</p><p>Neu ban dang o the he 40-series cao cap, ly do nang cap can duoc can nhac ky hon so voi nguoi dung card tam trung cu.</p>',
    'https://images.unsplash.com/photo-1591488320449-011701bb6704?auto=format&fit=crop&w=1400&q=80',
    'PUBLISHED',
    NOW(),
    NOW(),
    84,
    b'1',
    b'0',
    0,
    NOW(),
    NOW()
FROM (SELECT id FROM users ORDER BY id LIMIT 1) u
JOIN (SELECT id FROM categories WHERE slug = 'hardware' LIMIT 1) c
WHERE NOT EXISTS (
    SELECT 1 FROM posts WHERE slug = 'rtx-5080-co-thuc-su-dang-nang-cap-khong'
);

INSERT INTO posts (
    author_id,
    category_id,
    title,
    slug,
    summary,
    content,
    thumbnail_url,
    status,
    submitted_at,
    published_at,
    view_count,
    allow_comments,
    is_featured,
    priority,
    created_at,
    updated_at
)
SELECT
    u.id,
    c.id,
    'AI tai thiet bi dang thay doi laptop Windows ra sao',
    'ai-tai-thiet-bi-dang-thay-doi-laptop-windows-ra-sao',
    'NPU, kha nang tom tat noi dung va workflow sang tao dang dan tro thanh diem ban hang moi cua laptop.',
    '<p>Su xuat hien cua NPU tren laptop khong chi la con so marketing. Neu duoc toi uu dung cach, no mo ra nhieu tac vu xu ly cuc bo nhu tom tat, goi y van ban, xu ly hinh anh va cac tinh nang ho tro nang suat.</p><p>Van de lon nhat hien nay la he sinh thai phan mem da theo kip phan cung den dau.</p>',
    'https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1400&q=80',
    'PUBLISHED',
    NOW(),
    NOW(),
    65,
    b'1',
    b'0',
    0,
    NOW(),
    NOW()
FROM (SELECT id FROM users ORDER BY id LIMIT 1) u
JOIN (SELECT id FROM categories WHERE slug = 'ai-reviews' LIMIT 1) c
WHERE NOT EXISTS (
    SELECT 1 FROM posts WHERE slug = 'ai-tai-thiet-bi-dang-thay-doi-laptop-windows-ra-sao'
);

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id
FROM posts p
JOIN tags t ON t.slug = 'laptop'
WHERE p.slug = 'cuoc-dua-ultrabook-ai-2026'
  AND NOT EXISTS (
      SELECT 1 FROM post_tags WHERE post_id = p.id AND tag_id = t.id
  );

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id
FROM posts p
JOIN tags t ON t.slug = 'ultrabook'
WHERE p.slug = 'cuoc-dua-ultrabook-ai-2026'
  AND NOT EXISTS (
      SELECT 1 FROM post_tags WHERE post_id = p.id AND tag_id = t.id
  );

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id
FROM posts p
JOIN tags t ON t.slug = 'ai'
WHERE p.slug = 'cuoc-dua-ultrabook-ai-2026'
  AND NOT EXISTS (
      SELECT 1 FROM post_tags WHERE post_id = p.id AND tag_id = t.id
  );

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id
FROM posts p
JOIN tags t ON t.slug = 'benchmark'
WHERE p.slug = 'rtx-5080-co-thuc-su-dang-nang-cap-khong'
  AND NOT EXISTS (
      SELECT 1 FROM post_tags WHERE post_id = p.id AND tag_id = t.id
  );

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id
FROM posts p
JOIN tags t ON t.slug = 'ai'
WHERE p.slug = 'ai-tai-thiet-bi-dang-thay-doi-laptop-windows-ra-sao'
  AND NOT EXISTS (
      SELECT 1 FROM post_tags WHERE post_id = p.id AND tag_id = t.id
  );

INSERT INTO post_tags (post_id, tag_id)
SELECT p.id, t.id
FROM posts p
JOIN tags t ON t.slug = 'laptop'
WHERE p.slug = 'ai-tai-thiet-bi-dang-thay-doi-laptop-windows-ra-sao'
  AND NOT EXISTS (
      SELECT 1 FROM post_tags WHERE post_id = p.id AND tag_id = t.id
  );
