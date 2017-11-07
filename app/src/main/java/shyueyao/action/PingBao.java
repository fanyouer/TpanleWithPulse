package shyueyao.action;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by K on 2017/10/25.
 */

public class PingBao extends Activity {
    Timer timer1 = new Timer();
    private String beijing;
    private SimpleDateFormat df;

    TextView tv_beijing_shijian;

    private int zuobiao_x=0;
    private int zuobiao_y=0;
    private boolean x_flag=true;
    private boolean y_flag=true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        finish();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingbao);

        tv_beijing_shijian=findViewById(R.id.pingbao_shijian);

        df = new SimpleDateFormat("HH:mm:ss");
        timer1.schedule(task1, 100, 100);


    }
    TimerTask task1 = new TimerTask() {

        public void run() {
            runOnUiThread(new Runnable() {      // UI thread

                public void run() {
                    if (x_flag){
                        zuobiao_x++;
                    }else {
                        zuobiao_x--;
                    }
                    if (y_flag){
                        zuobiao_y++;
                    }else {
                        zuobiao_y--;
                    }

                    if (zuobiao_x>1220){
                        x_flag=false;
                    }
                    if (zuobiao_x<0){
                        x_flag=true;
                    }

                    if (zuobiao_y>880){
                        y_flag=false;
                    }
                    if (zuobiao_y<-40){
                        y_flag=true;
                    }

                    beijing = df.format(new Date());
                    tv_beijing_shijian.setText(beijing);
                    tv_beijing_shijian.setPadding(zuobiao_x,zuobiao_y,0,0);
                }
            });

        }
    };

}
