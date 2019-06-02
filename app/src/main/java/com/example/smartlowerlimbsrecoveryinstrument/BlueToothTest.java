package com.example.smartlowerlimbsrecoveryinstrument;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartlowerlimbsrecoveryinstrument.toolsClass.MiBand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BlueToothTest extends AppCompatActivity {

    private Button saomiao , duanzhen , changzhen , buting , tingxia;
    private TextView jibu , dianliang , lianjiezhuangtai;
    BluetoothAdapter bluetoothAdapter;
    List<BluetoothDevice> deviceList = new ArrayList<>();

    String[] permission={
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    RecyclerView list;
    BluetoothGattCharacteristic characteristic_zd,characteristic_jb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_test);

        //蓝牙管理，这是系统服务可以通过getSystemService(BLUETOOTH_SERVICE)的方法获取实例
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        //通过蓝牙管理实例获取适配器，然后通过扫描方法（scan）获取设备(device)
        bluetoothAdapter = bluetoothManager.getAdapter();

        if(bluetoothAdapter==null){
            Toast.makeText(BlueToothTest.this, "您的手机不支持蓝牙", Toast.LENGTH_SHORT).show();
        }

        bluetoothAdapter.enable();
        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //请求开启蓝牙设备
            startActivity(intent);
        }

        System.out.println("权限申请");
        System.out.println("权限申请"+ ContextCompat.checkSelfPermission(BlueToothTest.this,permission[0])+String.valueOf(PackageManager.PERMISSION_GRANTED));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(int i=0;i<permission.length;i++){
                if(ContextCompat.checkSelfPermission(BlueToothTest.this,permission[i])!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.shouldShowRequestPermissionRationale(BlueToothTest.this,permission[i]);
                    ActivityCompat.requestPermissions(BlueToothTest.this,new String[]{permission[i]},i);
                }
            }
        }

        initView();

    }

    @Override
    protected void onResume(){
        super.onResume();
        Set<BluetoothDevice> set=bluetoothAdapter.getBondedDevices();
        Iterator<BluetoothDevice> iterator=set.iterator();
        deviceList.clear();
        while (iterator.hasNext()){
            deviceList.add(iterator.next());
        }
    }

    private void initView() {
        list=(RecyclerView)findViewById(R.id.recyclerView);


        saomiao = (Button) findViewById(R.id.saomiao);
        duanzhen = (Button) findViewById(R.id.zhendong);
        changzhen = (Button) findViewById(R.id.changzhen);
        buting = (Button) findViewById(R.id.buting);
        tingxia = (Button) findViewById(R.id.tingxia);

        jibu = (TextView) findViewById(R.id.jibu);
        dianliang = (TextView) findViewById(R.id.dianliang);
        lianjiezhuangtai = (TextView) findViewById(R.id.lianjiezhuangtai);

        saomiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始扫描前开启蓝牙
                Intent turn_on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turn_on, 0);
                Toast.makeText(BlueToothTest.this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();

                Thread scanThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("run: saomiao ...");
                        bluetoothAdapter.startLeScan(callback);
//                        new BluetoothLeScanner().startScan();
                    }
                });
                scanThread.start();
            }
        });
        duanzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        changzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tingxia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //扫描回调
    public BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            //重复过滤方法，列表中包含不该设备才加入列表中，并刷新列表
            if (!deviceList.contains(bluetoothDevice)) {
                //将设备加入列表数据中
                deviceList.add(bluetoothDevice);
                System.out.println( "onLeScan: " + bluetoothDevice.getName() + "\t" + bluetoothDevice.getAddress() + "\t" + bluetoothDevice.getBondState());
                list.setLayoutManager(new LinearLayoutManager(BlueToothTest.this));
                MyAdapter adapter=new MyAdapter();
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        BluetoothDevice bluetoothDevice1=deviceList.get(position);
                        BluetoothGatt bluetoothGatt=bluetoothDevice1.connectGatt(BlueToothTest.this, false, bluetoothGattCallback);
                        lianjiezhuangtai.setText("连接" + bluetoothDevice1.getName() + "中...");
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                list.setAdapter(adapter);
                list.setHasFixedSize(true);
                list.setItemAnimator(new DefaultItemAnimator());
                list.addItemDecoration(new DividerItemDecoration(BlueToothTest.this, LinearLayout.VERTICAL));
            }

        }
    };

    BluetoothGattCallback bluetoothGattCallback=new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status,final int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (newState) {
                        //已经连接
                        case BluetoothGatt.STATE_CONNECTED:
                            lianjiezhuangtai.setText("已连接");

                            //该方法用于获取设备的服务，寻找服务
                            gatt.discoverServices();
                            break;
                        //正在连接
                        case BluetoothGatt.STATE_CONNECTING:
                            lianjiezhuangtai.setText("正在连接");
                            break;
                        //连接断开
                        case BluetoothGatt.STATE_DISCONNECTED:
                            lianjiezhuangtai.setText("已断开");
                            break;
                        //正在断开
                        case BluetoothGatt.STATE_DISCONNECTING:
                            lianjiezhuangtai.setText("断开中");
                            break;
                    }
                    //pd.dismiss();
                }
            });
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt bluetoothGatt, int status) {
            super.onServicesDiscovered(bluetoothGatt, status);
            //寻找到服务时
            if (status == bluetoothGatt.GATT_SUCCESS) {
                final List<BluetoothGattService> services = bluetoothGatt.getServices();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<String> serlist = new ArrayList<>();
                        for (final BluetoothGattService bluetoothGattService : services) {
//                            bluetoothGattServices = bluetoothGattService;

                            System.out.println("onServicesDiscovered: " + bluetoothGattService.getUuid());

                            List<BluetoothGattCharacteristic> charc = bluetoothGattService.getCharacteristics();

                            for (BluetoothGattCharacteristic charac : charc) {
                                System.out.println( "run: " + charac.getUuid()+"\t\t"+charac.getValue());
                                if(charac.getValue()!=null) Toast.makeText(BlueToothTest.this,String.valueOf(charac.getValue()),Toast.LENGTH_SHORT).show();
                                //找到透传特征值
                                // 00002a06-0000-1000-8000-00805f9b34fb 小米手环震动特征值 0x01震动 0x02强震
                                if (charac.getUuid().toString().equals(MiBand.VIBRATION_CHAR_UUID)) {
                                    //设备 震动特征值
                                    characteristic_zd = charac;
                                    Toast.makeText(BlueToothTest.this,"获取震动Characteristic",Toast.LENGTH_SHORT).show();
                                } else if (charac.getUuid().toString().equals(MiBand.STEP_CHAR_UUID)) {
                                    //设备 步数
                                    characteristic_jb = charac;
                                    bluetoothGatt.readCharacteristic(characteristic_jb);

                                    Toast.makeText(BlueToothTest.this,"run: 正在尝试读取步数",Toast.LENGTH_SHORT).show();

                                } else if (charac.getUuid().toString().equals("")) {
                                    //设备 电量特征值
                                }
                            }

                            serlist.add(bluetoothGattService.getUuid().toString());

                        }
                    }
                });
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == gatt.GATT_SUCCESS) {
                final int sum = characteristic.getValue()[0];

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        jibu.setText("走了" + sum + "步");
                    }
                });

                System.out.println("onCharacteristicRead: " + characteristic.getValue()[0]);

            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }
    };

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    class MyAdapter extends RecyclerView.Adapter{
        OnItemClickListener onItemClickListener=null;
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Holder holder=new Holder(LayoutInflater.from(BlueToothTest.this).inflate(R.layout.devices_item,parent,false));
            ((Holder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,list.getChildAdapterPosition(v));
                }
            });
            ((Holder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(v,list.getChildAdapterPosition(v));
                    return false;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BluetoothDevice bd = deviceList.get(position);
            ((Holder)holder).name.setText(bd.getName());
            ((Holder)holder).uuid.setText(bd.getAddress());
        }

        @Override
        public int getItemCount() {
            return deviceList.size();
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
            this.onItemClickListener=onItemClickListener;
        }
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView name , uuid , status;
        public Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.bluetoothname);
            uuid = (TextView) itemView.findViewById(R.id.uuid);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }

}
