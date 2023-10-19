package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        Button btn_item_details_ok = findViewById(R.id.btn_item_details_ok);
        int position = this.getIntent().getIntExtra("position", 0);
        String title = this.getIntent().getStringExtra("name");

        EditText et_item_name = findViewById(R.id.et_item_name);

        if(null != title){
            et_item_name.setText(title);
        }

        btn_item_details_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                EditText et_item_name = findViewById(R.id.et_item_name);

                bundle.putString("name", et_item_name.getText().toString());
                bundle.putInt("position", position);

                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                BookDetailsActivity.this.finish();
            }
        });
    }

}