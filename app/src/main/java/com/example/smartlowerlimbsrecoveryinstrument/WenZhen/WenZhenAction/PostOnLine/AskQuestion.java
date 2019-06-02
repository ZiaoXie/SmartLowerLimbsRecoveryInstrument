package com.example.smartlowerlimbsrecoveryinstrument.WenZhen.WenZhenAction.PostOnLine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartlowerlimbsrecoveryinstrument.R;
import com.example.smartlowerlimbsrecoveryinstrument.WenZhen.ChatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AskQuestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskQuestion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FrameLayout frameLayout;
    Button next;
    ImageView photo;
    EditText editText;
    View view;

    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_PHOTO = 2;
    private Uri imageUri;

    String permission[] = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE};

    public AskQuestion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AskQuestion.
     */
    // TODO: Rename and change types and number of parameters
    public static AskQuestion newInstance(String param1, String param2) {
        AskQuestion fragment = new AskQuestion();
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
        view = inflater.inflate(R.layout.fragment_ask_question, container, false);

        frameLayout = (FrameLayout) view.findViewById(R.id.ask_question);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                frameLayout.setFocusable(true);
                frameLayout.setFocusableInTouchMode(true);
                frameLayout.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                }
                return false;
            }
        });

        next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChatActivity.class));
            }
        });

        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(getContext(), permission[i])
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission[i]);
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission[i]}, i);//自定义的code
            }
        }


        photo = (ImageView) view.findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("请选择照片来源");
                final String[] photo = {"相机", "相册"};
                builder.setItems(photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            take_photo();
                        } else {
                            select_photo();
                        }
                    }
                });
                builder.show();
            }
        });
        editText = (EditText) view.findViewById(R.id.miaoshu);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 拍照获取图片
     **/
    public void take_photo() {
        //创建File对象，用于存储拍照后的图片
        File outputImage = new File(getContext().getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(getContext(), "com.example.administrator.needletherapy.WenZhen.WenZhenAction.PostOnLine", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 从相册中获取图片
     */
    public void select_photo() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            Toast.makeText(getContext(), "没有权限", Toast.LENGTH_SHORT).show();
            System.out.println("打开相册");
        } else {
            openAlbum();
        }
    }

    /**
     * 打开相册的方法
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        Drawable drawable = new BitmapDrawable(bitmap);
                        photo.setBackground(drawable);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_PHOTO:
                if (resultCode == getActivity().RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        //4.4及以上系统使用这个方法处理图片
                        //handleImgeOnKitKat(data);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                            Drawable drawable = new BitmapDrawable(bitmap);
                            photo.setBackground(drawable);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    } else {
                        //handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "权限申请成功", Toast.LENGTH_SHORT).show();
            openAlbum();
        } else {
            Toast.makeText(getContext(), "you need the permission", Toast.LENGTH_SHORT).show();
        }
    }
}
