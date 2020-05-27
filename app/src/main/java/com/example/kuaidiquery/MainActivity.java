package com.example.kuaidiquery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kuaidiquery.kuaidiniao.KdniaoTrackQueryAPI;
import com.example.kuaidiquery.pojo.Track;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private Button bt_query;
    private EditText et_number;
    private Spinner spinner;
    private String[] kuadiName; //快递公司
    private String[] kuaidiCode; // 根据快递公司名称获取快递Code
    private Handler handler;

    private String inputkuaidinumber; //输入的快递单号
    private String inputkuaidiCode;//输入的快递号码

    private ProgressBar progressBar; //查询进度条




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_query = findViewById(R.id.bt_query);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progressBar);
        et_number = findViewById(R.id.et_number);
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            String message = (String) msg.obj;
                            //查询无果，提示
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("提示")
                                    .setMessage(message)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
//                        finish();//Exit Activity
                                        }
                                    }).create().show();

                            break;
                    }

            }
        };


        initView();
        initData();
        initListener();

    }

    protected void initView(){
        //progresBar 打开引用时隐藏
        progressBar.setVisibility(View.INVISIBLE);

        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputkuaidinumber = et_number.getText().toString().trim();
                //检查输入是否合规
                if(inputkuaidinumber.length() == 0){
                    Toast.makeText(MainActivity.this, "请输入快递单号！"+et_number.getText(), Toast.LENGTH_SHORT).show();
                    return;
                }

                //开始查询时显示
                progressBar.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
                        Track track = null;
                        try {
                            //YT4551373182338
                            //圆通
                            //773036187850410
                            //申通快递
                            //如果选择自动选择快递公司则跳过查询，直接进入轮询
                            if(!inputkuaidiCode.equals("auto"))
                                track = api.getOrderTracesByJson(inputkuaidiCode, inputkuaidinumber);

                            if(track == null || track.getState().equals("0") || track.isSuccess() == false || inputkuaidiCode.equals("auto")){
                                //查询无果时开始轮询主要快递商家。
                                boolean isOk = false;
                                for(String code:kuaidiCode){
                                    //如果与当前输入快递公司相同则跳过
                                    if (code.equals(inputkuaidiCode)){
                                        continue;
                                    }

                                    track = api.getOrderTracesByJson(code, inputkuaidinumber);

                                    //0 为查询无果、2-在途中,3-签收,4-问题件
                                    if(track.getTraces().size() != 0 && !track.getState().equals("0")){
                                        hideProgress();
                                        //跳转到显示结果的页面
                                        Intent intent = new Intent(MainActivity.this,TrackInfoActivity.class);
                                        intent.putExtra("trackinfo",new Gson().toJson(track));
                                        startActivity(intent);
                                        isOk = true;
                                        break;
                                    }
                                }
                                if(isOk== false){
                                    //查询无果
                                    hideProgress();
                                    showDialog("查询无果，请选择具体快递公司~");
                                }
                                isOk = false;
                            }else{
                                //跳转到显示结果的页面
                                Intent intent = new Intent(MainActivity.this,TrackInfoActivity.class);
                                intent.putExtra("trackinfo",new Gson().toJson(track));
                                startActivity(intent);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }
        });
    }

    /**
     * 初始化快递信息
     */
    protected void initData(){


        kuadiName = new String[]{"自动选择","顺丰速运", "百世快递","中通快递","申通快递","圆通速递","韵达速递","邮政快递包裹","EMS","天天快递","京东快递","优速快递","德邦快递","宅急送"};
        kuaidiCode = new String[]{"auto","SF","HTKY","ZTO","STO","YTO","YD","YZPY","EMS","HHTT","JD","UC","DBL","ZJS"};

        ArrayAdapter<String> kuaidiNameAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,kuadiName);
        kuaidiNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(kuaidiNameAdapter);
        spinner.setSelection(0);

    }

    protected void initListener(){
        spinner.setOnItemSelectedListener(this);

    }

    /**
     * 对下拉快递公司选择框进行监听
     * @param parent 选择框
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.spinner){
            Log.d("spinner被点击内容为",kuadiName[position]+"快递公司代码为:"+kuaidiCode[position]);
            inputkuaidiCode = kuaidiCode[position]+"";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * //查询成功时关掉progressBar
     * //使用handle完成
     */
    private void hideProgress(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                //查询失败时也隐藏progressBar
                progressBar.setVisibility(View.INVISIBLE);


            }
        });
    }

    /**
     * 显示提示框
     * @param message
     */
    private void showDialog(String message){
            Message msg = new Message();
            msg.what = 1;
            msg.obj = message;
            handler.sendMessage(msg);
    }
}
