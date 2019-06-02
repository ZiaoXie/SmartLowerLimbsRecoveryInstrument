package com.example.smartlowerlimbsrecoveryinstrument.Recovery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlowerlimbsrecoveryinstrument.Bean.RecoveryRecord;
import com.example.smartlowerlimbsrecoveryinstrument.R;
import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.security.AccessController.getContext;

public class RecoveryView extends AppCompatActivity {

    ImageButton qukuanjian,qukuanjia,quxijian,quxijia,datuijian,datuijia,xiaotuijian,xiaotuijia;
    TextView qukuan,quxi,datui,xiaotui;
    int QuKuan=10,QuXi=90,DaTui=220,XiaoTui=200;
    String username;
    NumberPicker np;
    EditText et;

    ProgressDialog pd;
    List<RecoveryRecord> record;
    List<BluetoothDevice> deviceList = new ArrayList<>();

    BluetoothAdapter bluetoothAdapter;
    BluetoothManager bluetoothManager;

    Message msg;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    Toast.makeText(RecoveryView.this,(String)msg.obj , Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    pd.dismiss();
                    qukuan.setText(String.valueOf(QuKuan));
                    quxi.setText(String.valueOf(QuXi));
                    datui.setText(String.valueOf(DaTui));
                    xiaotui.setText(String.valueOf(XiaoTui));
                    break;
                case 3:
                    new AlertDialog.Builder(RecoveryView.this).setMessage("发现您的上一次使用记录，是否沿用？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RecoveryRecord r=record.get(0);System.out.println(r.getQukuan()+"  "+r.getQuxi());
                                    QuKuan=Integer.parseInt(r.getQukuan().split("\\.")[0]);QuXi=Integer.parseInt(r.getQuxi().split("\\.")[0]);
                                    DaTui=Integer.parseInt(r.getDatui().split("\\.")[0]);XiaoTui=Integer.parseInt(r.getXiaotui().split("\\.")[0]);

                                    qukuan.setText(String.valueOf(QuKuan));
                                    quxi.setText(String.valueOf(QuXi));
                                    datui.setText(String.valueOf(DaTui));
                                    xiaotui.setText(String.valueOf(XiaoTui));
                                    pd.dismiss();
                                }
                            }).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_view);


        bluetoothManager=(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter  =bluetoothManager.getAdapter();
        if(!bluetoothAdapter .isEnabled()){
            //弹出对话框提示用户是后打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, 0);
            //不做提示，直接打开，不建议用下面的方法，有的手机会有问题。
            // mBluetoothAdapter.enable();
        }

        Thread scanThread = new Thread(new Runnable() {
            @Override
            public void run() {
                deviceList.clear();
                bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
                        Log.i("TAG", "onLeScan: " + bluetoothDevice.getName() + "/t" + bluetoothDevice.getAddress() + "/t" + bluetoothDevice.getBondState());
                        //重复过滤方法，列表中包含不该设备才加入列表中，并刷新列表
                        if (!deviceList.contains(bluetoothDevice)) {
                            //将设备加入列表数据中
                            deviceList.add(bluetoothDevice);
                        }
                    }
                });
            }
        });


        username=getSharedPreferences("userinfo",MODE_PRIVATE).getString("username","");
        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.left_grey)), ColorStateList.valueOf(Color.BLACK));
        DrawableCompat.setTintList(DrawableCompat.wrap(getResources().getDrawable(R.mipmap.right)), ColorStateList.valueOf(Color.BLACK));

        ImageView back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        qukuan=(TextView) findViewById(R.id.qukuan);
        qukuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                np=new NumberPicker(RecoveryView.this);
                np.setMinValue(10);
                np.setMaxValue(90);
                np.setValue(QuKuan);

                AlertDialog builder=new AlertDialog.Builder(RecoveryView.this).setView(np).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QuKuan=np.getValue();
                        qukuan.setText(String.valueOf(QuKuan));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
            }
        });

        quxi=(TextView) findViewById(R.id.quxi);
        quxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                np=new NumberPicker(RecoveryView.this);
                np.setMinValue(90);
                np.setMaxValue(180);
                np.setValue(QuXi);

                AlertDialog builder=new AlertDialog.Builder(RecoveryView.this).setView(np).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        QuXi=np.getValue();
                        quxi.setText(String.valueOf(QuXi));
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
            }
        });

        datui=(TextView) findViewById(R.id.datui);
        datui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et=new EditText(RecoveryView.this);
                et.setInputType(EditorInfo.TYPE_CLASS_PHONE);

                AlertDialog builder=new AlertDialog.Builder(RecoveryView.this).setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int result=Integer.parseInt(et.getText().toString());
                            if(result>=220&&result<=450){
                                DaTui=Integer.parseInt(et.getText().toString());
                                datui.setText(String.valueOf(DaTui));
                            }else if(result<220){
                                Toast.makeText(RecoveryView.this,"您输入的数字小于220",Toast.LENGTH_LONG).show();
                            }else if(result>450){
                                Toast.makeText(RecoveryView.this,"您输入的数字大于450",Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(RecoveryView.this,"请输入数字",Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
            }
        });


        xiaotui=(TextView) findViewById(R.id.xiaotui);
        xiaotui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et=new EditText(RecoveryView.this);
                et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                AlertDialog builder=new AlertDialog.Builder(RecoveryView.this).setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int result=Integer.parseInt(et.getText().toString());
                            if(result>=220&&result<=450){
                                XiaoTui=Integer.parseInt(et.getText().toString());
                                xiaotui.setText(String.valueOf(XiaoTui));
                            }else if(result<200){
                                Toast.makeText(RecoveryView.this,"您输入的数字小于200",Toast.LENGTH_LONG).show();
                            }else if(result>400){
                                Toast.makeText(RecoveryView.this,"您输入的数字大于400",Toast.LENGTH_LONG).show();
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(RecoveryView.this,"请输入数字",Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
            }
        });


        pd=ProgressDialog.show(RecoveryView.this,"请等待","正在读取您的使用记录");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(true);
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (username==null||username.equals("")){
                    Toast.makeText(RecoveryView.this,"未检测到您的使用记录",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    return;
                }
                record= DataBaseHelper.Query(5,RecoveryRecord.params, RecoveryRecord.class,
                        "select TOP 1 * from recoveryrecord where userid='"+ username +"' order by recoverytime desc ");
                if(DataBaseHelper.responseCode!=200){
                    Message msg=new Message();
                    msg.what=1;msg.obj="网络请求失败";
                    handler.sendMessage(msg);
                    return;
                }

                msg=new Message();msg.what=3;
                handler.sendMessage(msg);

            }


        }).start();

        qukuanjian=(ImageButton)findViewById(R.id.qukuanjian);
        qukuanjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QuKuan<=10) return;
                QuKuan--;
                qukuan.setText(String.valueOf(QuKuan));
            }
        });

        qukuanjia=(ImageButton)findViewById(R.id.qukuanjia);
        qukuanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuKuan>=90) return;
                QuKuan++;
                qukuan.setText(String.valueOf(QuKuan));
            }
        });

        quxijian=(ImageButton)findViewById(R.id.quxijian);
        quxijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QuXi<=90) return;
                QuXi--;
                quxi.setText(String.valueOf(QuXi));
            }
        });

        quxijia=(ImageButton)findViewById(R.id.quxijia);
        quxijia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuXi>=180) return;
                QuXi++;
                quxi.setText(String.valueOf(QuXi));
            }
        });

        datuijian=(ImageButton)findViewById(R.id.datuijian);
        datuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DaTui<=200) return;
                DaTui--;
                datui.setText(String.valueOf(DaTui));
            }
        });

        datuijia=(ImageButton)findViewById(R.id.datuijia);
        datuijia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DaTui>=450) return;
                DaTui++;
                datui.setText(String.valueOf(DaTui));
            }
        });

        xiaotuijian=(ImageButton)findViewById(R.id.xiaotuijian);
        xiaotuijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(XiaoTui<=200) return;
                XiaoTui--;
                xiaotui.setText(String.valueOf(XiaoTui));
            }
        });

        xiaotuijia=(ImageButton)findViewById(R.id.xiaotuijia);
        xiaotuijia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(XiaoTui>=400) return;
                XiaoTui++;
                xiaotui.setText(String.valueOf(XiaoTui));
            }
        });

        Button start=(Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username!=null){
                    boolean flag=DataBaseHelper.Execute("insert into recoveryrecord values('"+ username +"','"+
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"','"+
                            String.valueOf(QuKuan) +"','"+ String.valueOf(QuXi) +"','"+ String.valueOf(DaTui) +"','"+ String.valueOf(XiaoTui) +"')");
                    if (!flag){
                        msg=new Message();msg.what=1;
                        msg.obj="网络连接失败";
                        handler.sendMessage(msg);
                    } else {
                        msg=new Message();msg.what=1;
                        msg.obj="设定成功";
                        handler.sendMessage(msg);
                    }
                }
            }
        });

    }
}
