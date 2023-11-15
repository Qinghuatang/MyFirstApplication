package com.jnu.student;

import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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


        Button btn_item_details_ok = findViewById(R.id.btn_new_task_confirm);
        EditText newContent = findViewById(R.id.et_new_task_content);
        EditText newPoint = findViewById(R.id.et_new_task_point);
        EditText newTask = findViewById(R.id.et_new_task_num);
        EditText newClassification = findViewById(R.id.et_new_task_classification);

        int position = this.getIntent().getIntExtra("position", 0);
        String content = this.getIntent().getStringExtra("content");
        int point = this.getIntent().getIntExtra("point", 0);

        if (content != null) {
            newContent.setText(content);
        }
        if (point != 0) {
            newPoint.setText(String.valueOf(point));
        }

        btn_item_details_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putString("content", newContent.getText().toString());
                bundle.putInt("point", Integer.parseInt(newPoint.getText().toString()));
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