package it.ma.tpanel.action;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by K on 2017/11/6.
 */

public class TimeSet extends Activity{

    private DatePicker dpToday;
    private TimePicker tpNow;
    private Button timeSave;
    private Button timeExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_set);

        dpToday=(DatePicker)super.findViewById(R.id.today);
        tpNow=(TimePicker)super.findViewById(R.id.now);
        dpToday.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        tpNow.setIs24HourView(true);
        tpNow.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

    }

    public void ButTimeSave(View view) throws IOException, InterruptedException {
        int iYear=0;
        int iMonth=0;
        int iDay=0;
        String sDate="";
        iYear=dpToday.getYear();
        iMonth=dpToday.getMonth()+1;
        iDay=dpToday.getDayOfMonth();
        sDate="日期："+String.valueOf(iYear)+"年"+String.valueOf(iMonth)+"月"+String.valueOf(iDay)+"日";

        int iHour=0;
        int iMin=0;
        String sTime="";
        iHour=tpNow.getCurrentHour();
        iMin=tpNow.getCurrentMinute();
        sTime="时间："+String.valueOf(iHour)+"时"+String.valueOf(iMin)+"分";

        SystemDateTime.setDateTime(iYear,iMonth,iDay,iHour,iMin);

        Toast.makeText(getApplicationContext(), "修改成功",Toast.LENGTH_LONG).show();
         finish();
    }

    public void ButTimeExit(View view) {
        finish();
    }
}
