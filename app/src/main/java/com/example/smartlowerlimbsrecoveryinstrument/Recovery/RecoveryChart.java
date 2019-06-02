package com.example.smartlowerlimbsrecoveryinstrument.Recovery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.smartlowerlimbsrecoveryinstrument.Bean.RecoveryRecord;
import com.example.smartlowerlimbsrecoveryinstrument.R;
import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.DataBaseHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


public class RecoveryChart extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String ziduan,username,title;
    int flag;
    List<RecoveryRecord> result;
    LineChartInViewPager lineChart;
    List<Entry> entries = new ArrayList<Entry>();
    LineDataSet dataSet;
    LineData lineData;

    Message msg;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(getContext(),(String)msg.obj , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    lineChart.setData(lineData);
                    break;
                case 3:

                    break;
            }
        }
    };

    public RecoveryChart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecoveryChart.
     */
    // TODO: Rename and change types and number of parameters
    public static RecoveryChart newInstance(String param1, String param2) {
        RecoveryChart fragment = new RecoveryChart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_chart, container, false);
        ziduan=getArguments().getString("ziduan");
        username=getArguments().getString("username");
        flag=getArguments().getInt("flag",-1);
        title=getArguments().getString("title");

        lineChart=(LineChartInViewPager) view.findViewById(R.id.lineChart);

        new Thread(new Runnable() {
            @Override
            public void run() {
                result= DataBaseHelper.Query(1, RecoveryRecord.class,new String[]{ziduan},new String[]{"recoveryrecord"},
                        "userid='"+ username +"' order by recoverytime");

                if (DataBaseHelper.responseCode!=200){
                    Message msg=new Message();
                    msg.what=1;msg.obj="网络请求失败";
                    handler.sendMessage(msg);
                    return;
                }

                for(int i=0;i<result.size();i++){
                    switch (flag){
                        case 0:
                            entries.add(new Entry(i,Integer.parseInt(result.get(i).getQukuan().split("\\.")[0])));
                            break;
                        case 1:
                            entries.add(new Entry(i,Integer.parseInt(result.get(i).getQuxi().split("\\.")[0])));
                            break;
                        case 2:
                            entries.add(new Entry(i,Integer.parseInt(result.get(i).getDatui().split("\\.")[0])));
                            break;
                        case 3:
                            entries.add(new Entry(i,Integer.parseInt(result.get(i).getXiaotui().split("\\.")[0])));
                            break;
                    }

                }
                dataSet=new LineDataSet(entries,title+"数据统计");
                lineData=new LineData(dataSet);
                msg=new Message();msg.what=2;
                handler.sendMessage(msg);
            }
        }).start();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
