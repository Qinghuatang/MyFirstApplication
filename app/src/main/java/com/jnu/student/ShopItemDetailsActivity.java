package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShopItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);


        Button btn_item_details_ok = findViewById(R.id.btn_item_details_ok);
        btn_item_details_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText et_item_name = findViewById(R.id.et_item_name);
                EditText et_item_price = findViewById(R.id.et_item_price);

                intent.putExtra("name", et_item_name.getText().toString());
                intent.putExtra("price", et_item_price.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                ShopItemDetailsActivity.this.finish();
            }
        });
    }

}