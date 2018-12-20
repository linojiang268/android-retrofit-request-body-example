package cn.test.retrofit.myretrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public final class PostRequestConvertFactory extends Converter.Factory {
    public static PostRequestConvertFactory create() {
        return new PostRequestConvertFactory();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        //进行条件判断，如果传进来的Type不是class，则匹配失败
//        if (!(type instanceof Class<?>)) {
//            return null;
//        }
        return new PostRequestConverter<>();
    }
}