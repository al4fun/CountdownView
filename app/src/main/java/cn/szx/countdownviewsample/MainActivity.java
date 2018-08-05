package cn.szx.countdownviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        FrameLayout fl_container = findViewById(R.id.fl_container);

        CountdownView.Listener listener = new CountdownView.Listener() {
            @Override
            public void onFinished() {
                Toast.makeText(MainActivity.this, "计时结束", Toast.LENGTH_SHORT).show();
            }
        };

        CountdownView countdownView = new CountdownView(this, 10, listener)
                .setSize(400, 400)
                .setStrokeWidth(20)
                .setTextsize(200);

        fl_container.addView(countdownView);
    }
}