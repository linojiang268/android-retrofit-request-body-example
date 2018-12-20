package cn.test.retrofit.myretrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ArrayService {
    @GET("/get_by_array")
//    Call<ResponseBody> getByArray(@Query("id") int id);
    Call<ResponseBody> getByArray(@QueryMap Map<String, Object> params);

    /**
     * {@link FormUrlEncoded} 表明是一个表单格式的请求（Content-Type:application/x-www-form-urlencoded）
     * <code>Field("username")</code> 表示将后面的 <code>String name</code> 中name的取值作为 username 的值
     */
    @POST("/post_by_array")
//    @FormUrlEncoded
//    Call<ResponseBody> postByArray(@Field("name") String name, @Field("age") int age);
//    Call<ResponseBody> postByArray(
//            @Field("strList") List<String> strList,
//            @Field("arrList") List<ArrParam> arrList
//    );
    Call<ResponseBody> postByArray(
            @Body Map<String, Object> params
    );

    /**
     * Map的key作为表单的键
     */
    @POST("/post_by_array")
    @FormUrlEncoded
    Call<ResponseBody> postByArrayMap(@FieldMap Map<String, Object> map);
}
