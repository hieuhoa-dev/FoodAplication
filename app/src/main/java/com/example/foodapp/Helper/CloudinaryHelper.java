package com.example.foodapp.Helper;

import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryHelper {
    public CloudinaryHelper() {

    }
    public void uploadImageToCloudinary(Uri imageUri) {
        // Thêm  chức năng xóa imageUri
        MediaManager.get().upload(imageUri)
               .option("folder", "userImg").callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {

            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                String imageUrl = (String) resultData.get("url"); // Lấy URL của ảnh sau khi upload
                UserRepository userRepository = new UserRepository();
                userRepository.setUserImg(imageUrl.toString());
                Log.d("Upload", "Uploaded: " + imageUrl);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.e("Upload", "Error: " + error.getDescription()); // Xử lý lỗi nếu có
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();
    }

    public void deleteImageFromCloudinary(String publicId) {
        Map options = ObjectUtils.asMap(
                "invalidate", true // Invalidate cache
        );

        new Thread(() -> {
            try {
                Map result = MediaManager.get().getCloudinary().uploader().destroy(publicId, options);
                Log.d("Cloudinary", "Deleted: " + result.toString());
            } catch (Exception e) {
                Log.e("Cloudinary", "Delete error: " + e.getMessage());
            }
        }).start();
    }
}
