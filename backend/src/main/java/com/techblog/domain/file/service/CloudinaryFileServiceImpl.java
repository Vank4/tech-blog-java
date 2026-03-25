/*


// LƯU LẠI ĐOẠN CODE NÀY ĐỂ DÀNH CHO TƯƠNG LAI, DÙNG ĐỂ LƯU ẢNH TRÊN CLOUD THAY VÌ HIỆN TẠI LƯU Ở LOCAL


package com.techblog.domain.file.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
// ... (các import khác)

@Service
@Primary // <--- Chữ này là "phép thuật" để đè lên cái LocalFileServiceImpl
@RequiredArgsConstructor
public class CloudinaryFileServiceImpl implements FileService {
    
    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            // Cloudinary sẽ trả về một cái link https://... cực xịn
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload Cloudinary: " + e.getMessage());
        }
    }
}
*/