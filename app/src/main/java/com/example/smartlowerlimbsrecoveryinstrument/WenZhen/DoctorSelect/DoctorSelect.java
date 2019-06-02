package com.example.smartlowerlimbsrecoveryinstrument.WenZhen.DoctorSelect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartlowerlimbsrecoveryinstrument.R;

public class DoctorSelect extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back;

    String titles[]={"专家","普通"};
    Integer picture[]={R.mipmap.expert3,R.mipmap.expert2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_select);

        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        MyAdapter myAdapter=new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if(postion==0){
                    startActivity(new Intent(DoctorSelect.this,DoctorList.class));
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(DoctorSelect.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(DoctorSelect.this, LinearLayoutManager.VERTICAL));




    }

    class MyAdapter extends RecyclerView.Adapter{

        MyItemClickListener mItemClickListener;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder=new Holder(LayoutInflater.from(DoctorSelect.this).inflate(R.layout.item_doctor_class,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((Holder)holder).iv.setBackgroundResource(picture[position]);
            ((Holder)holder).tv.setText(titles[position]);
            if(position==0){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(DoctorSelect.this,DoctorList.class));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }

        public void setOnItemClickListener(MyItemClickListener listener){
            this.mItemClickListener = listener;
        }

        class Holder extends RecyclerView.ViewHolder{
            ImageView iv;
            TextView tv;

            public Holder(View v) {
                super(v);
                iv=(ImageView) v.findViewById(R.id.picture);
                tv=(TextView)v.findViewById(R.id.item_person);
            }
        }
    }
}

interface MyItemClickListener {
    public void onItemClick(View view, int postion);
}
