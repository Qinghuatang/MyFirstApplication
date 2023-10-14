package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                intent.putExtra("key", "Some result data");
                setResult(Activity.RESULT_OK, intent);

                ShopItemDetailsActivity.this.finish();
            }
        });
    }

}