package com.example.smartlowerlimbsrecoveryinstrument.WenZhen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.smartlowerlimbsrecoveryinstrument.R;
import com.example.smartlowerlimbsrecoveryinstrument.WenZhen.DoctorSelect.DoctorSelect;
import com.example.smartlowerlimbsrecoveryinstrument.WenZhen.WenZhenAction.PostOnline;
import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.DividerGridItemDecoration;
import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.SearchView;

public class WenZhenView extends AppCompatActivity {

    LinearLayout linearLayout;
    EditText editText;
    ImageView delete;
    RecyclerView recyclerView;
    RelativeLayout zixun_layout;

    String titles[]={"儿科","妇科","呼吸科","外科","泌尿科","血液科","消化科","内分泌","骨科","男科","更多"};
    Integer picture[]={R.mipmap.child,R.mipmap.female,R.mipmap.breath,R.mipmap.ear,R.mipmap.miniao,
            R.mipmap.blood,R.mipmap.xiaohua,R.mipmap.liver,R.mipmap.bone,R.mipmap.male,R.mipmap.moreinfo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wen_zhen_view);

        linearLayout=(LinearLayout)findViewById(R.id.wen_zhen);
        delete=(ImageView)findViewById(R.id.search_iv_delete);
        editText=(EditText)findViewById(R.id.search_et_input);
        new SearchView(editText,delete,new View[]{linearLayout}).init(WenZhenView.this);

        ImageView back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        zixun_layout=(RelativeLayout)findViewById(R.id.zixun_layout);
        zixun_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WenZhenView.this, PostOnline.class));
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(WenZhenView.this,4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //添加分割线
        recyclerView.addItemDecoration(new DividerGridItemDecoration(WenZhenView.this));
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder=new Holder(LayoutInflater.from(WenZhenView.this).inflate(R.layout.item_keshi,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((Holder)holder).iv.setBackgroundResource(picture[position]);
            ((Holder)holder).tv.setText(titles[position]);

            if(position==picture.length-1){
                ((Holder)holder).iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            if(titles[position].equals("血液科")){
                ((Holder)holder).iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(WenZhenView.this,DoctorSelect.class));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }

        class Holder extends RecyclerView.ViewHolder{
            ImageView iv;
            TextView tv;

            public Holder(View v) {
                super(v);
                iv=(ImageView) v.findViewById(R.id.keshi_pic);
                tv=(TextView)v.findViewById(R.id.id_num);
            }
        }
    }
}
