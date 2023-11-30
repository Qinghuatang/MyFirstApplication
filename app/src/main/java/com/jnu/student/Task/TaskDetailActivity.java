package com.jnu.student.Task;

import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.jnu.student.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 新建任务界面
public class TaskDetailActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private String task_classification;

    // 定义下拉列表需要显示的任务类型图标数组
    private static final int[] iconArray = {
            R.drawable.learn, R.drawable.sport, R.drawable.else_class,
    };
    // 定义下拉列表需要显示的任务类型名称数组
    private static final String[] starArray = {"学习工作", "运动健身", "其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_detail);

        Toolbar toolbar = findViewById(R.id.new_task_toolbar);
        // 为标题栏的返回按钮添加相应函数
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 声明一个映射对象的列表，用于保存行星的图标与名称配对信息
        List<Map<String, Object>> list = new ArrayList<>();
        // iconArray是任务类型的图标数组，starArray是任务类型的名称数组
        for (int i = 0; i < iconArray.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("icon", iconArray[i]);
            item.put("name", starArray[i]);
            list.add(item);
        }

        // 声明一个下拉列表的简单适配器，其中指定了图标与文本两组数据
        SimpleAdapter startAdapter = new SimpleAdapter(this, list,
                R.layout.item_task_classification,
                new String[]{"icon", "name"},
                new int[]{R.id.iv_icon, R.id.tv_name});

        Spinner sp_icon = findViewById(R.id.sp_new_task_classification);
        sp_icon.setPrompt("请选择任务标签"); // 设置下拉框的标题
        sp_icon.setAdapter(startAdapter);
        String classification = this.getIntent().getStringExtra("classification");
        int index = 0;
        if (classification != null){
            index = index(starArray, classification);
        }
        sp_icon.setSelection(index);
        sp_icon.setOnItemSelectedListener(this);


        RadioGroup rg_task_type = findViewById(R.id.rg_task_type);


        EditText newContent = findViewById(R.id.et_new_task_content);
        EditText newPoint = findViewById(R.id.et_new_task_point);
        EditText newTaskNum = findViewById(R.id.et_new_task_num);

//        EditText newClassification = findViewById(R.id.et_new_task_classification);

        Button btn_item_details_ok = findViewById(R.id.btn_new_task_confirm);

        String operate = this.getIntent().getStringExtra("operate");

        if ("update".equals(operate)) {
            for (int i = 0; i < rg_task_type.getChildCount(); i++) {
                rg_task_type.getChildAt(i).setEnabled(false);
            }
            toolbar.setTitle("编辑任务");
        }

        int position = this.getIntent().getIntExtra("position", 0);
        String content = this.getIntent().getStringExtra("content");
        int point = this.getIntent().getIntExtra("point", -1);
        int num = this.getIntent().getIntExtra("num", -1);


        if (content != null) {
            newContent.setText(content);
        }
        if (point != -1) {
            newPoint.setText(String.valueOf(point));
        }
        if (num != -1) {
            newTaskNum.setText(String.valueOf(num));
        }
        if (classification != null) {
//            newClassification.setText(classification);
        }


        btn_item_details_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                String taskType = "";
                int checkedRadioButtonId = rg_task_type.getCheckedRadioButtonId();
                if (R.id.rb_daily == checkedRadioButtonId) {
                    taskType = "Daily";
                } else if (R.id.rb_weekly == checkedRadioButtonId) {
                    taskType = "Weekly";
                } else if (R.id.rb_common == checkedRadioButtonId) {
                    taskType = "Common";
                }

                bundle.putString("taskType", taskType);
                bundle.putString("content", newContent.getText().toString());
                bundle.putInt("point", Integer.parseInt(newPoint.getText().toString()));
                bundle.putInt("num", Integer.parseInt(newTaskNum.getText().toString()));
                bundle.putString("classification", task_classification);

                bundle.putInt("position", position);

                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.task_classification = starArray[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static int index(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

}