package com.example.smartlowerlimbsrecoveryinstrument.Recovery;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartlowerlimbsrecoveryinstrument.Bean.RecoveryRecord;
import com.example.smartlowerlimbsrecoveryinstrument.R;
import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.DataBaseHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class RecoveryData extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    String title[]={"屈髋角度","屈膝角度","大腿长度","小腿长度"};
    String ziduan[]={"qukuan","quxi","datui","xiaotui"};
    String username;

    List<RecoveryRecord> result;
    LineChart lineChart;
    List<Entry> entries[];
    LineDataSet dataSet;
    LineData lineData;

    Message msg;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(RecoveryData.this,(String)msg.obj , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    lineChart.setData(lineData);
                    break;
                case 3:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_data);

        username=getSharedPreferences("userinfo",MODE_PRIVATE).getString("username","");

        ImageView iv=(ImageView) findViewById(R.id.back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        //viewPager.setVisibility(View.GONE);

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        //tabLayout.setVisibility(View.GONE);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        lineChart=(LineChart) findViewById(R.id.lineChart);

        entries=new List[4];
        for(int i=0;i<4;i++) entries[i]=new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                result= DataBaseHelper.Query(5, RecoveryRecord.class,RecoveryRecord.params,new String[]{"recoveryrecord"},
                        "userid='"+ username +"' order by recoverytime");

                if (DataBaseHelper.responseCode!=200){
                    Message msg=new Message();
                    msg.what=1;msg.obj="网络请求失败";
                    handler.sendMessage(msg);
                    return;
                }

                for(int i=0;i<result.size();i++){
                    entries[0].add(new Entry(i,Integer.parseInt(result.get(i).getQukuan().split("\\.")[0])));
                    entries[1].add(new Entry(i,Integer.parseInt(result.get(i).getQuxi().split("\\.")[0])));
                    entries[2].add(new Entry(i,Integer.parseInt(result.get(i).getDatui().split("\\.")[0])));
                    entries[3].add(new Entry(i,Integer.parseInt(result.get(i).getXiaotui().split("\\.")[0])));
                }
                List<ILineDataSet> sets = new ArrayList<>();
                sets.add(new LineDataSet(entries[0],title[0]+"数据统计"));
                sets.add(new LineDataSet(entries[1],title[1]+"数据统计"));
                sets.add(new LineDataSet(entries[2],title[2]+"数据统计"));
                sets.add(new LineDataSet(entries[3],title[3]+"数据统计"));
                lineData=new LineData(sets);

                msg=new Message();msg.what=2;
                handler.sendMessage(msg);
            }
        }).start();

    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle=new Bundle();
            bundle.putString("ziduan",ziduan[position]);
            bundle.putString("username",username);
            bundle.putString("title",title[position]);
            bundle.putInt("flag",position);
            Fragment f=new RecoveryChart();
            f.setArguments(bundle);
            return f;
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
