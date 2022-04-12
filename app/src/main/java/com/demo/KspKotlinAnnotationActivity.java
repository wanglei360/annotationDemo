package com.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.apt.kspBinding.KspBinding;
import com.lib.imp.KspBindView;

/**
 * 创建者：leiwu
 * <p> 时间：2022/4/6 10:25
 * <p> 类描述：
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
public class KspKotlinAnnotationActivity extends AppCompatActivity {

    @KspBindView(R.id.tv)
    public TextView tv;

    @KspBindView(R.id.tv1)
    public TextView tv1;

    @KspBindView(R.id.tv2)
    public TextView tv2;

    @KspBindView(R.id.tv3)
    public TextView tv3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KspBinding.bind(this);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        System.out.println("");
    }

    public void btnClick3(View v) {
        tv.setText("Ksp Kotlin apt代码成功");
    }

}