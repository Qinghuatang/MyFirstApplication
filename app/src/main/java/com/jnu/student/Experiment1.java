package com.jnu.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Experiment1 extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHello;
    private TextView tvJnu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment1);

        // 实验三内容
        TextView tv_hello = findViewById(R.id.text_view_hello_world);
        tv_hello.setText(R.string.hello_android);

        // 实验四内容
        Button btn_change_text = findViewById(R.id.btn_change_text);
        btn_change_text.setOnClickListener(this);
        tvHello = findViewById(R.id.tv_Hello);
        tvJnu = findViewById(R.id.tv_JNU);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_change_text){
            CharSequence hello = tvHello.getText();
            CharSequence jnu = tvJnu.getText();
            tvHello.setText(jnu);
            tvJnu.setText(hello);

            // 1.使用Toast显示"交换成功"
            Toast.makeText(this, "交换成功", Toast.LENGTH_SHORT).show();

            // 2.使用AlertDialog显示"交换成功"
            AlertDialog.Builder builder = new AlertDialog.Builder(this);    // 创建提醒对话框的构造器
            builder.setMessage("交换成功");
            AlertDialog dialog = builder.create();  // 根据构造器构建提醒对话框对象
            dialog.show();  // 显示提醒对话框
        }
    }
}