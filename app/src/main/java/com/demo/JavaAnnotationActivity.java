package com.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.apt.AptBinding.Binding;
import com.lib.BindView;

/**
 * 创建者：leiwu
 * <p> 时间：2022/3/2 10:14
 * <p> 类描述：
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
public class JavaAnnotationActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    public TextView tv;

    @BindView(R.id.tv1)
    public TextView tv1;

    @BindView(R.id.tv2)
    public TextView tv2;

    @BindView(R.id.tv3)
    public TextView tv3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new JavaAnnotationActivity$Binding(this);
        Binding.bind(this);

        tv1.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
    }

    public void btnClick2(View v) {
        tv.setText("Java apt代码成功");
    }

}