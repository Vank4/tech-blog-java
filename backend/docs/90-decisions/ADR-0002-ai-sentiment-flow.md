# ADR-0002: Hoạt Động Cốt Lõi AI Sentiment Analysis Architecture

## 1. Vấn đề bối cảnh
Nền tảng tích hợp Deep Learning Model AI để phân tích sắc thái biểu cảm (Sentiment Evaluation: Tích cực, Tiêu cực, Trung lập) trực tiếp sau khi độc giả submit các bài đánh giá / bình luận sản phẩm. Từ đó Backend sẽ summarize rút gọn lại để show tổng kết điểm đánh giá tổng quan (Ví dụ: 80% Khen Tốt, 5% Phàn nàn). 

Đảm bảo: Xử lý chạy NLP của Model là 1 tác vụ Heavy load gây Blocking chậm, ta không thể ép trình duyệt của user giữ vòng quay reload Loading xoay chờ API AI nhai xong dữ liệu. Cần hướng đi bất đồng bộ.

## 2. Core Quyết định Giải Pháp
Xây dựng workflow xử lý đa phân luồng **Asynchronous (Bất đồng bộ)** và đẩy Job xuống Background Backend.
- **Bước 1**: Ngay khi user POST data bình luận, Spring Boot API lưu thẳng Object Comment Document vô Data gốc theo trạng thái `ai_processed: false`. Response mã lỗi gật đầu `200 OK` cho Frontend tắt Loading UI tức thì.
- **Bước 2**: Spring Boot lập tức bắn tín hiệu Push đến 1 Actor/Event Bus (`@Async`) hoặc chạy Async Messaging Queue Background service.
- **Bước 3**: Thread chạy ngầm ấy sẽ HTTP REST call gọi tới mô hình Python model NLP, lấy nhãn POSITIVE/NEGATIVE trả ra.
- **Bước 4**: Save Label ngược lại ghi Update lại record Object Comment ấy trong MongoDB (`ai_processed: true`). Kích hoạt cập nhật lại cache tổng hợp điểm Rating đếm tay của User.

## 3. Kết Quả Và Lưu Ý
- Performance trả Request comment cho User nhanh chóng chưa tới 10ms (Không còn phải bận tâm mạng lưới kết nối model Python bị trễ).
- Hệ quả phụ: Các comment mới xuất hiện sẽ chưa được đóng dấu AI gán nhãn ngay thời điểm F5 reload, có thể delay vài chục milliseconds nhưng đảm bảo không bóp nghẽn Server Tomcat container. Node AI nằm biệt lập ra.
