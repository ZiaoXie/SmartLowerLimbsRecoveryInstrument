package com.example.smartlowerlimbsrecoveryinstrument;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.smartlowerlimbsrecoveryinstrument.YangSheng.MainYangSheng;
import com.example.smartlowerlimbsrecoveryinstrument.forum.MainForum;
import com.example.smartlowerlimbsrecoveryinstrument.personal.Personal;
import com.example.smartlowerlimbsrecoveryinstrument.recommend.Recommend;
import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.DataBaseHelper;

public class HomePage extends AppCompatActivity {

    public String username;
    Intent intent,intent1;
    public static int skin_use=-1;

    RadioGroup radioGroup;
    private int mOriginButtonTop;

    FragmentManager fm;
    FragmentTransaction transaction;
    int radiocheckedId;

    public Bitmap bitmap;
    GestureDetectorCompat gdc;

    String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

//        ActivityCompat.requestPermissions(HomePage.this,new String[]{permission[0]},0);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(int i=0;i<permission.length;i++){
                System.out.println(ContextCompat.checkSelfPermission(HomePage.this,permission[i]));
                if(ContextCompat.checkSelfPermission(HomePage.this,permission[i])!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.shouldShowRequestPermissionRationale(HomePage.this,permission[i]);
                    ActivityCompat.requestPermissions(HomePage.this,new String[]{permission[i]},i);
                }

            }
        }

        username=getSharedPreferences("userinfo",MODE_PRIVATE).getString("username","");
        if(username!=""){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String[][] headpath=DataBaseHelper.Query("select headpath from users where userid='"+ username +"'",1);
                    if(headpath==null) return;
                    bitmap=DataBaseHelper.getBitmap(headpath[0][0]);
                }
            }).start();

        }
        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();
        transaction.replace(R.id.detail,new Recommend());
        transaction.commit();


    }

    public void onResume(){
        super.onResume();

        username=getSharedPreferences("userinfo",MODE_PRIVATE).getString("username","");
        Toast.makeText(HomePage.this,username,Toast.LENGTH_LONG);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //DataBaseHelper.Execute("insert into users values('1002','123456','grdfsd')");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();



        Drawable icon = getResources().getDrawable(R.mipmap.taolun);
        Drawable wrap = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(wrap,getResources().getColorStateList(R.color.color_selector));
        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.tuijian)),getResources().getColorStateList(R.color.color_selector));
        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.yangsheng)),getResources().getColorStateList(R.color.color_selector));
        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.personal)),getResources().getColorStateList(R.color.color_selector));


        fm=getSupportFragmentManager();
        transaction=fm.beginTransaction();

        radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        radiocheckedId=radioGroup.getCheckedRadioButtonId();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(radiocheckedId==checkedId) return;
                radiocheckedId=radioGroup.getCheckedRadioButtonId();
                transaction=fm.beginTransaction();
                switch (checkedId){
                    case R.id.recommend:
                        transaction.replace(R.id.detail,new Recommend());
                        break;
                    case R.id.yangsheng:
                        transaction.replace(R.id.detail,new MainYangSheng());
                        break;
                    case R.id.taolun:
                        transaction.replace(R.id.detail,new MainForum());
                        break;
                    case R.id.PersonalCentre:
                        transaction.replace(R.id.detail,new Personal());
                        break;
                }
                transaction.commit();
            }
        });
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        int buttonTop,buttonBottom;
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {//判断是否竖直滑动
                buttonTop = radioGroup.getTop();
                buttonBottom = radioGroup.getBottom();

                //是否向下滑动
                boolean isScrollDown = e1.getRawY() < e2.getRawY() ? true : false;
                //根据滑动方向和当前的位置判断是否需要移动位置
                if (!ifNeedScroll(isScrollDown)) return false;

                if (isScrollDown) {
                    //下滑上移
                    radioGroup.setTop(buttonTop - (int) Math.abs(distanceY));
                    radioGroup.setBottom(buttonBottom - (int) Math.abs(distanceY));
                } else if (!isScrollDown) {
                    //上滑下移
                    radioGroup.setTop(buttonTop + (int) Math.abs(distanceY));
                    radioGroup.setBottom(buttonBottom + (int) Math.abs(distanceY)); }
            }
            return true;
        }

        //写一个方法，根据滑动方向和mButton当前的位置，判断按钮是否应该继续滑动
        private boolean ifNeedScroll(boolean isScrollDown) {
            int nowButtonTop = radioGroup.getTop();
            //不能超出原来的上边界
            if (isScrollDown && nowButtonTop >= mOriginButtonTop) return false;
            //判断按钮是否在屏幕范围内，如果不在，则不需要再移动位置
            if (!isScrollDown) {
                return isInScreen(radioGroup);
            }
            return true;
        }


        //判断一个控件是否在屏幕范围内
        private boolean isInScreen(View view) {
            int width, height;
            Point p = new Point();
            getWindowManager().getDefaultDisplay().getSize(p);
            width = p.x;
            height = p.y;

            Rect rect = new Rect(0, 0, width, height);

            if (!view.getLocalVisibleRect(rect)) return false;
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
//        if(requestCode<permission.length-1){
//            if(ContextCompat.checkSelfPermission(HomePage.this,permission[requestCode+1])!= PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.shouldShowRequestPermissionRationale(HomePage.this, permission[requestCode + 1]);
//                ActivityCompat.requestPermissions(HomePage.this, new String[]{permission[requestCode + 1]}, requestCode + 1);
//            }else {
//
//            }
//        }
    }
}


