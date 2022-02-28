package com.xxl.bdmapmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.xxl.bdmap.ActivityHelper;
import com.xxl.bdmap.data.IfLyMapData;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, WalkingRouteSearchDemos.class));
                ActivityHelper.startWalkSearchActivity(MainActivity.this, 30.252782, 120.163385);
            }
        });
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, WalkingRouteSearchDemos.class));
//                ActivityHelper.startWalkSearchActivity(MainActivity.this,30.252782, 120.163385);
                ActivityHelper.startWalkListActivity(MainActivity.this, new IfLyMapData());
            }
        });

    }


}