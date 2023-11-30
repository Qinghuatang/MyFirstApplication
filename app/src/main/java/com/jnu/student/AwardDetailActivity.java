package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwardDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String award_classification;

    // 定义下拉列表需要显示的任务类型图标数组
    private static final int[] iconArray = {
            R.drawable.play, R.drawable.eat, R.drawable.relax, R.drawable.shopping, R.drawable.travel
    };
    // 定义下拉列表需要显示的任务类型名称数组
    private static final String[] classificationArray = {"娱乐", "美食", "休息", "购物", "旅行"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_detail);

        Toolbar toolbar = findViewById(R.id.new_award_toolbar);
        // 为标题栏的返回按钮添加相应函数
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText et_new_award_content = findViewById(R.id.et_new_award_content);
        EditText et_new_award_point = findViewById(R.id.et_new_award_point);
        Button btn_new_award_confirm = findViewById(R.id.btn_new_award_confirm);

        String operate = this.getIntent().getStringExtra("operate");
        if("update".equals(operate)){
            toolbar.setTitle("编辑奖励");
        }

        int position = this.getIntent().getIntExtra("position", 0);
        String content = this.getIntent().getStringExtra("content");
        int point = this.getIntent().getIntExtra("point", 0);

        if (content != null) {
            et_new_award_content.setText(content);
        }
        if(point != 0){
            et_new_award_point.setText(String.valueOf(-point));
        }

        // 声明一个映射对象的列表，用于保存行星的图标与名称配对信息
        List<Map<String, Object>> list = new ArrayList<>();
        // iconArray是任务类型的图标数组，starArray是任务类型的名称数组
        for (int i = 0; i < iconArray.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("icon", iconArray[i]);
            item.put("name", classificationArray[i]);
            list.add(item);
        }

        // 声明一个下拉列表的简单适配器，其中指定了图标与文本两组数据
        SimpleAdapter startAdapter = new SimpleAdapter(this, list,
                R.layout.item_task_classification,
                new String[]{"icon", "name"},
                new int[]{R.id.iv_icon, R.id.tv_name});

        Spinner sp_icon = findViewById(R.id.sp_new_award_classification);
        sp_icon.setPrompt("请选择奖励标签"); // 设置下拉框的标题
        sp_icon.setAdapter(startAdapter);
        String classification = this.getIntent().getStringExtra("classification");
        int index = 0;
        if (classification != null){
            index = index(classificationArray, classification);
        }
        sp_icon.setSelection(index);
        sp_icon.setOnItemSelectedListener(this);


        btn_new_award_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putString("content", et_new_award_content.getText().toString());
                bundle.putInt("point", -Integer.parseInt(et_new_award_point.getText().toString()));
                bundle.putInt("position", position);
                bundle.putString("classification", award_classification);

                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.award_classification = classificationArray[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int index(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }
}