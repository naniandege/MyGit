package com.example.liqian.newproject;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liqian.newproject.adapter.RecycleViewAdapter;
import com.example.liqian.newproject.bean.MyBean;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements RecycleViewAdapter.onItemLisnner {

    private Toolbar toolbar;
    private RecyclerView lv;
    private ArrayList<MyBean.DataDTO> list;
    private RecycleViewAdapter adapter;
    private CollapsingToolbarLayout clpsTb;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001) {
                String str = (String) msg.obj;
                Gson gson = new Gson();
                MyBean myBean = gson.fromJson(str, MyBean.class);
                list.addAll(myBean.getData());
                adapter.notifyDataSetChanged();
            }
        }
    };
    private File mFile;
    private File mFile1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        mFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    private void initData() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        final Request request = new Request.Builder()
                .url("http://www.wanandroid.com/banner/json")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                Message message = mHandler.obtainMessage();
                message.obj = string;
                message.what = 0x001;
                mHandler.sendMessage(message);
            }
        });
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv = (RecyclerView) findViewById(R.id.lv);
        clpsTb = (CollapsingToolbarLayout) findViewById(R.id.clpsTb);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //标题必须在CollapsingToolbarLayout设置
        clpsTb.setTitle("折叠ToolBar");
        //扩张时候的title颜色
        clpsTb.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));
        //收缩后显示title的颜色
        clpsTb.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));

        list = new ArrayList<>();
        adapter = new RecycleViewAdapter(list, this);
        lv.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        lv.setLayoutManager(manager);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));


        adapter.setOnItemLisnner(this);

    }

    @Override
    public void onPosition(final MyBean.DataDTO dataDTO) {
        final String replaceAll = dataDTO.getImagePath().replaceAll("//", "").replaceAll("/", "");
        if (mFile1 != null) {
            String name = mFile1.getName();
            if (replaceAll.equals(name)) {
                Toast.makeText(this, "图片已存在", Toast.LENGTH_SHORT).show();
                this.mFile1.delete();
                return;
            }
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .url(dataDTO.getImagePath())
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                InputStream inputStream = body.byteStream();
                saveFile(inputStream, MainActivity.this.mFile + "/" + replaceAll);
            }
        });
    }

    private void saveFile(InputStream inputStream, String path) {
        try {
            mFile1 = new File(path);
            FileOutputStream outputStream = new FileOutputStream(mFile1);
            byte[] bytes = new byte[1024 * 10];
            int length = -1;
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
