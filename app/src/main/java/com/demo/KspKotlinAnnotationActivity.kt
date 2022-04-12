package com.demo

import com.lib.imp.KspBindView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.view.View
import com.demo.R

/**
 * 创建者：leiwu
 *  时间：2022/4/6 10:25
 *  类描述：这个类是Java的报错，注解用在变量上报错，ksp太难弄了
 *  修改人：
 *  修改时间：
 *  修改备注：
 */
@KspBindView(1111)
class KspKotlinAnnotationActivity : AppCompatActivity() {
    var tv: TextView? = null
    var tv1: TextView? = null
    var tv2: TextView? = null
    var tv3: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        KspBinding.bind(this);
//        tv1.setVisibility(View.GONE);
//        tv2.setVisibility(View.GONE);
//        System.out.println("");
    }

    fun btnClick3(v: View?) {
        tv!!.text = "Ksp Kotlin apt代码成功"
    }
}