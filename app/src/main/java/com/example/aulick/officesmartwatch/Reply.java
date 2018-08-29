package com.example.aulick.officesmartwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Reply extends AppCompatActivity {

    private Button buttonAccept,buttonDecline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        buttonAccept = (Button) findViewById(R.id.btn_accept);
        buttonDecline = (Button) findViewById(R.id.btn_decline);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });

        buttonDecline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });

    }


}
