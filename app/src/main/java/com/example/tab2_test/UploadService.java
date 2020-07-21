package com.example.tab2_test;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("/api/upload")
    Call<FileNameBody> postImage(@Part("user_id") String id, @Part("group_name") String group_name, @Part MultipartBody.Part image);

    @Multipart
    @POST("/api/memoBook")
    Call<ResponseBody> addFileName(@Part("group_name") String group, @Part("file_name") String file);
}
