package com.bringletech.looviedraft.livechat.ui;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bringletech.looviedraft.livechat.MainActivity;
import com.bringletech.looviedraft.livechat.R;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent myint = new Intent(Dashboard.this,MainActivity.class);
               startActivity(myint);

            }
        });
    }
}
