package com.example.smartlowerlimbsrecoveryinstrument.toolsClass;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017-09-16.
 */

public class SearchView {
    EditText editText;
    ImageView delete;
    View[] layout;
    View temp;

    public SearchView(EditText editText, ImageView delete, View []layout) {
        this.editText = editText;
        this.delete = delete;
        this.layout = layout;
    }

    public void init(final Activity context){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                delete.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    delete.setVisibility(View.GONE);
                }
                if(hasFocus&&editText.getText().toString()!=""){

                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });


        for(int i=0;i<layout.length;i++){
            temp=layout[i];
            layout[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    temp.setFocusable(true);
                    temp.setFocusableInTouchMode(true);
                    temp.requestFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
                    }
                    return false;
                }
            });
        }

    }
}
