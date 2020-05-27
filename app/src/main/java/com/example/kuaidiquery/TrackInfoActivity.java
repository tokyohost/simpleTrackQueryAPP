package com.example.kuaidiquery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.kuaidiquery.TrackLine.TimelineDecoration;
import com.example.kuaidiquery.pojo.Track;
import com.google.gson.Gson;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TrackInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private TextView companyName; //快递公司
    private TextView trackStatus;//快递状态


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_info);
        recyclerView = findViewById(R.id.track_info_list);
        companyName = findViewById(R.id.companyName);
        trackStatus = findViewById(R.id.trackStatus);


        //接收数据
        Intent intent = getIntent();
        Track track = new Gson().fromJson(intent.getStringExtra("trackinfo"),Track.class);
//        Log.d("track",track.getTraces().get(0).getAcceptStation());

        //展示快递状态和快递公司
        String[] kuaidiName = new String[]{"自动选择","顺丰速运", "百世快递","中通快递","申通快递","圆通速递","韵达速递","邮政快递包裹","EMS","天天快递","京东快递","优速快递","德邦快递","宅急送"};
        String[] kuaidiCode = new String[]{"auto","SF","HTKY","ZTO","STO","YTO","YD","YZPY","EMS","HHTT","JD","UC","DBL","ZJS"};
        //快递状态
        String[] kuaidiStatusCode = new String[]{"0","1","2","3","4"};
        String[] kuaidiStatus = new String[]{"查询无果","未知","正在运送途中","已签收","问题件"};

        for(int i=0;i<kuaidiCode.length;i++){
            if(kuaidiCode[i].equals(track.getShipperCode())){
                companyName.setText("快递公司:"+kuaidiName[i]);
            }
        }

        for(int i=0;i<kuaidiStatusCode.length;i++){
            if(kuaidiStatusCode[i].equals(track.getState())){
                trackStatus.setText("快递状态:"+kuaidiStatus[i]);
            }
        }



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseQuickAdapter<Track.TracesBean, BaseViewHolder> adapter = new BaseQuickAdapter<Track.TracesBean, BaseViewHolder>(R.layout.item_transport,
                (getStation(track))) {
            @Override
            protected void convert(BaseViewHolder helper, Track.TracesBean item) {
                TextView tvDescribe = helper.getView(R.id.tv_describe);
                TextView tvTime = helper.getView(R.id.tv_time);
                tvDescribe.setText(item.getAcceptStation());
                tvTime.setText(item.getAcceptTime());

                int position = helper.getAdapterPosition();
                tvDescribe.setTextColor(position==0?0xff4caf65:0xff999999);
                tvTime.setTextColor(position==0?0xff4caf65:0xff999999);

            }
        };

        recyclerView.addItemDecoration(new TimelineDecoration(getDimen(R.dimen.time_line_width),
                getDimen(R.dimen.time_line_top),
                ContextCompat.getDrawable(this,R.drawable.ic_check_circle),
                getDimen(R.dimen.time_going_size),
                1,track.getState().equals("3")?true:false));
        recyclerView.setAdapter(adapter);


    }
    private int getDimen(int dimeRes){
        return (int) getResources().getDimension(dimeRes);
    }

    /**
     * 获取信息列表
     * @param track
     * @return
     */
    private List<Track.TracesBean> getStation(Track track){
        List<Track.TracesBean> statusBean = new ArrayList<>();

        for(Track.TracesBean traces: track.getTraces()){
            statusBean.add(traces);
        }
        Collections.reverse(statusBean);
        return statusBean;
    }
}
