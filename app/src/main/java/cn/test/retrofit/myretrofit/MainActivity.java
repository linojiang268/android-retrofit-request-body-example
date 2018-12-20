package cn.test.retrofit.myretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void getClicked(View view)
    {
        Toast.makeText(this, "get clicked", Toast.LENGTH_SHORT).show();
        this.requestGet();
    }

    public void postClicked(View view) {
        Toast.makeText(this, "post clicked", Toast.LENGTH_SHORT).show();
        this.requestPost();
    }

    private Retrofit createRetrofit() {
        String baseUrl = "http://192.168.0.51:8000/";
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(PostRequestConvertFactory.create())
                .build();
    }

    // Get²»Ö§³ÖBody
    private void requestGet() {
        ArrayService arrayService = createRetrofit().create(ArrayService.class);
        ArrayList<String> strList = new ArrayList<>();
        strList.add("aaa");
        strList.add("bbb");

        ArrayList<ArrParam> arrList = new ArrayList<>();
        arrList.add(new ArrParam("aaa111", "bbb111"));
        arrList.add(new ArrParam("aaa222", "bbb222"));
        arrList.add(new ArrParam("aaa333", "bbb333"));

        Map<String, Object> map = new HashMap<>();
        map.put("strList", strList);
        map.put("arrList", arrList);

        Call<ResponseBody> answers = arrayService.getByArray(map);
        answers.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i("Get Response", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void requestPost() {
        ArrayService arrayService = createRetrofit().create(ArrayService.class);
        ArrayList<String> strList = new ArrayList<>();
        strList.add("aaa");
        strList.add("bbb");

        ArrayList<ArrParam> arrList = new ArrayList<>();
        arrList.add(new ArrParam("aaa111", "bbb111"));
        arrList.add(new ArrParam("aaa222", "bbb222"));
        arrList.add(new ArrParam("aaa333", "bbb333"));

        Map<String, Object> map = new HashMap<>();
        map.put("strList", strList);
        map.put("arrList", arrList);

        Call<ResponseBody> answers = arrayService.postByArray(map);
        answers.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i("Post Response", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
