package com.example.smartlowerlimbsrecoveryinstrument.recommend;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.example.smartlowerlimbsrecoveryinstrument.Bean.Article;
import com.example.smartlowerlimbsrecoveryinstrument.Bean.Tag;
import com.example.smartlowerlimbsrecoveryinstrument.BlueToothTest;
import com.example.smartlowerlimbsrecoveryinstrument.R;
import com.example.smartlowerlimbsrecoveryinstrument.Recovery.RecoveryData;
import com.example.smartlowerlimbsrecoveryinstrument.Recovery.RecoveryView;
import com.example.smartlowerlimbsrecoveryinstrument.WenZhen.WenZhenView;
import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.DataBaseHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("NewApi")
public class Recommend extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton fab;
    RecyclerView recyclerView;
    String  username,YinLiYear,YinLiDate,YinliYi,YinLiJi;
    TextView day, year_month,yinli_year,yinli_date,yi,ji;

    Bitmap[] bitmaps;
    View view;
    Context context;
    List<Article> articleList;
    List tagList[];
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ConvenientBanner convenientBanner;
    Integer integer[]=new Integer[]{R.mipmap.lishizheng,R.mipmap.shanggutianzhenlun,R.mipmap.door,R.mipmap.bamboo};
    int count;

    Message msg;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(context,(String)msg.obj , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    adapter=new MyAdapter();
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent=new Intent(context,RecommendView.class);
                            intent.putExtra("articleid",articleList.get(position).getArticleid());
                            intent.putExtra("username",username);
                            startActivity(intent);
                        }
                        @Override
                        public void onItemLongClick(View view, int position) { }
                    });
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayout.VERTICAL));

                    break;
                case 3:
                    swipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    public Recommend() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Recommend newInstance(String param1, String param2) {
        Recommend fragment = new Recommend();
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

        username=getActivity().getSharedPreferences("userinfo",MODE_PRIVATE).getString("username","");
        view=inflater.inflate(R.layout.fragment_recommend, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);





        LinearLayout kangfu=(LinearLayout) view.findViewById(R.id.kangfu);
        kangfu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), RecoveryView.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        LinearLayout wenzhen=(LinearLayout) view.findViewById(R.id.wenzhen);
        wenzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WenZhenView.class));
            }
        });

        LinearLayout record=(LinearLayout) view.findViewById(R.id.record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username==""){
                    Toast.makeText(context,"请先登录" , Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(getContext(), RecoveryData.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.kangfu)), getResources().getColorStateList(R.color.color_kangfu));
        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.wenzhen)),getResources().getColorStateList(R.color.color_wenzhen));
        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.record)),getResources().getColorStateList(R.color.color_jilu));

        new Thread(new Runnable() {
            @Override
            public void run() {
                getArticle();
            }
        }).start();

        /*加载滚动条模块*/
        convenientBanner=(ConvenientBanner)view.findViewById(R.id.banner);
        convenientBanner.startTurning(4000);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, Arrays.asList(integer))
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);


        /*加载下拉刷新模块*/
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.srl);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getArticle();
                        handler.sendEmptyMessage(3);
                    }
                }).start();

            }
        });

        fab=(FloatingActionButton) view.findViewById(R.id.edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(username==""){
                    Toast.makeText(context,"请先登录" , Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(context,TagSelect.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public void getArticle(){
        if(username==""){
            System.out.println("准备发送请求");
            articleList= DataBaseHelper.Query(4,Article.class,Article.params,new String[]{"Article"},null);
            if (DataBaseHelper.responseCode!=200){
                Message msg=new Message();
                msg.what=1;msg.obj="网络请求失败";
                handler.sendMessage(msg);
                return;
            }
            if(articleList==null||articleList.size()==0)
                return;
            tagList=new List[articleList.size()];
            for (int i=0;i<articleList.size();i++){
                tagList[i]=DataBaseHelper.Query(1,Tag.class,new String[]{"tagname"},new String[]{"Tag","articleTag"},
                        " articleid='" + articleList.get(i).getArticleid() + "' and articletag.tagid=tag.tagid");
            }
        }
        else {
            articleList= DataBaseHelper.Query(4,Article.class,Article.params,new String[]{"Article"},
                    "articleid in(select articleid from  article_view where userid='"+ username +"')");
            if (DataBaseHelper.responseCode!=200){
                Message msg=new Message();
                msg.what=1;msg.obj="网络请求失败";
                handler.sendMessage(msg);
                return;
            }
            if(articleList==null||articleList.size()==0)
                return;
            tagList=new List[articleList.size()];
            for (int i=0;i<articleList.size();i++){
                tagList[i]=DataBaseHelper.Query(1,Tag.class,new String[]{"tagname"},new String[]{"article_view"},
                        "userid='"+ username +"' and articleid='"+ articleList.get(i).getArticleid() +"'");
            }
        }

        bitmaps = new Bitmap[articleList.size()];
        for (int i = 0; i < articleList.size(); i++) {
            bitmaps[i]=DataBaseHelper.getBitmap(articleList.get(i).getPicturepath());
            if (bitmaps[i] == null) {
                Message msg=new Message();
                msg.what=1;msg.obj="获取图片失败";
                handler.sendMessage(msg);
                return;
            }
        }

        msg=new Message();
        msg.what=2;
        handler.sendMessage(msg);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    class MyAdapter extends RecyclerView.Adapter{

        OnItemClickListener onItemClickListener=null;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder=new Holder(LayoutInflater.from(getContext()).inflate(R.layout.listview_article_item,parent,false));
            ((Holder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,recyclerView.getChildAdapterPosition(v));
                }
            });
            ((Holder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(v,recyclerView.getChildAdapterPosition(v));
                    return false;
                }
            });
            return holder;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
            this.onItemClickListener=onItemClickListener;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((Holder)holder).title.setText(articleList.get(position).getTitle());
            ((Holder)holder).abst.setText(articleList.get(position).getAbstract());
            ((Holder)holder).iv.setImageBitmap(bitmaps[position]);


            String tags[] = new String[tagList[position].size()];
            for (int i = 0; i < tagList[position].size(); i++)
                tags[i] = ((Tag) tagList[position].get(i)).getTagname();
            ((Holder) holder).tagGroup.setTags(tags);
            ((Holder) holder).tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                @Override
                public void onTagClick(String tag) {
                    Intent intent=new Intent(getContext(),TagView.class);
                    intent.putExtra("tagname",tag);
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return articleList.size();
        }
    }

    class Holder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView title,abst;
        TagGroup tagGroup;

        public Holder(View itemView) {
            super(itemView);
            iv=(ImageView) itemView.findViewById(R.id.articlepic);
            abst=(TextView)itemView.findViewById(R.id.articleabstract);
            title=(TextView)itemView.findViewById(R.id.title);
            tagGroup=(TagGroup) itemView.findViewById(R.id.tag_group);
        }
    }

    public class NetworkImageHolderView implements com.bigkoo.convenientbanner.holder.Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

}
