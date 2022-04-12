package com.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.reflex.AnnotationParse;
import com.demo.reflex.FindViewById;

/**
 * 创建者：leiwu
 * * 时间：2022/3/2 09:15
 * * 类描述：
 * * 修改人：
 * * 修改时间：
 * * 修改备注：
 */
public class ReflexActivity extends AppCompatActivity {

    @FindViewById(R.id.tv)
    public TextView tv;

    @FindViewById(R.id.tv1)
    public TextView tv1;

    @FindViewById(R.id.tv2)
    public TextView tv2;

    @FindViewById(R.id.tv3)
    public TextView tv3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnnotationParse.parse(this);

        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
    }

    public void btnClick1(View v) {
        tv.setText("反射到内容了");
    }

}