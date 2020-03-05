package com.example.a33626.endhomework2.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

//自定义TextWatcher
public class MyTextWatcher implements TextWatcher {
    private Button button;
    private EditText editTexts[];

    public MyTextWatcher(final Button button, final EditText... editTexts) {
        this.button = button;
        this.editTexts = editTexts;
        //首先给按钮设置成未激活
        button.setEnabled(false);
        //for each 给所有 et注册监听器
        for(EditText editText : this.editTexts){
            editText.addTextChangedListener(MyTextWatcher.this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        for(EditText editText : this.editTexts){
            if (editText.getText().length() == 0){
                this.button.setEnabled(false);
                return;
            }
            else{
                this.button.setEnabled(true);
            }
        }

    }
}
