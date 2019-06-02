package com.example.smartlowerlimbsrecoveryinstrument.WenZhen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WenZhen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WenZhen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout linearLayout;
    EditText editText;
    ImageView delete;
    View view;
    RecyclerView recyclerView;
    RelativeLayout zixun_layout;

    String titles[]={"儿科","妇科","呼吸科","外科","泌尿科","血液科","消化科","内分泌","骨科","男科","更多"};
    Integer picture[]={R.mipmap.child,R.mipmap.female,R.mipmap.breath,R.mipmap.ear,R.mipmap.miniao,
            R.mipmap.blood,R.mipmap.xiaohua,R.mipmap.liver,R.mipmap.bone,R.mipmap.male,R.mipmap.moreinfo};

    public WenZhen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WenZhen.
     */
    // TODO: Rename and change types and number of parameters
    public static WenZhen newInstance(String param1, String param2) {
        WenZhen fragment = new WenZhen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_wen_zhen, container, false);

        linearLayout=(LinearLayout) view.findViewById(R.id.wen_zhen);
        delete=(ImageView)view.findViewById(R.id.search_iv_delete);
        editText=(EditText)view.findViewById(R.id.search_et_input);
        new SearchView(editText,delete,new View[]{linearLayout}).init(getActivity());

        zixun_layout=(RelativeLayout)view.findViewById(R.id.zixun_layout);
        zixun_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PostOnline.class));
            }
        });

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MyAdapter());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //添加分割线
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder=new Holder(LayoutInflater.from(getActivity()).inflate(R.layout.item_keshi,parent,false));
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
                        startActivity(new Intent(getContext(),DoctorSelect.class));
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
