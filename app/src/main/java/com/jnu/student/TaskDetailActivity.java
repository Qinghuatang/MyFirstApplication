package com.jnu.student;

import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


// 新建任务界面
public class TaskDetailActivity extends Activity {

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


        RadioGroup rg_task_type = findViewById(R.id.rg_task_type);


        EditText newContent = findViewById(R.id.et_new_task_content);
        EditText newPoint = findViewById(R.id.et_new_task_point);
        EditText newTaskNum = findViewById(R.id.et_new_task_num);
        EditText newClassification = findViewById(R.id.et_new_task_classification);

        Button btn_item_details_ok = findViewById(R.id.btn_new_task_confirm);

        String operate = this.getIntent().getStringExtra("operate");

        if("update".equals(operate)){
            for (int i = 0; i < rg_task_type.getChildCount(); i++) {
                rg_task_type.getChildAt(i).setEnabled(false);
            }
            toolbar.setTitle("编辑任务");
        }

        int position = this.getIntent().getIntExtra("position", 0);
        String content = this.getIntent().getStringExtra("content");
        int point = this.getIntent().getIntExtra("point", -1);
        int num = this.getIntent().getIntExtra("num", -1);
        String classification = this.getIntent().getStringExtra("classification");


        if (content != null) {
            newContent.setText(content);
        }
        if(point != -1){
            newPoint.setText(String.valueOf(point));
        }
        if(num != -1){
            newTaskNum.setText(String.valueOf(num));
        }
        if (classification != null) {
            newClassification.setText(classification);
        }


        btn_item_details_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                String taskType = "";
                int checkedRadioButtonId = rg_task_type.getCheckedRadioButtonId();
                if(R.id.rb_daily == checkedRadioButtonId){
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
                bundle.putString("classification", newClassification.getText().toString());

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
}