package cn.szx.countdownviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        FrameLayout fl_container = findViewById(R.id.fl_container);

        CountdownView countdownView = new CountdownView(this, 5, null)
                .setSize(400, 400)
                .setStrokeWidth(20)
                .setTextsize(200);

        fl_container.addView(countdownView);
    }
}