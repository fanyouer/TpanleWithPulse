package it.ma.tpanel.action;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by K on 2017/8/24.
 */

public class GasSet extends Activity{

    EditText yangQiLimitUp;
    EditText yangQiLimitDown;
    EditText yaSuoLimitUp;
    EditText yaSuoLimitDown;
    EditText xiaoQiLimitUp;
    EditText xiaoQiLimitDown;
    EditText erYangLimitUp;
    EditText erYangLimitDown;
    EditText fuYaLimitUp;
    EditText fuYaLimitDown;

    private int yangqi;
    private int changeTemp;

    SharedPreferences sharedGasLimit;
    SharedPreferences.Editor editor;
    Timer gasalerm = new Timer();

    DecimalFormat format;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_set);
        sharedGasLimit=this.getSharedPreferences("gaslimit",this.MODE_WORLD_READABLE);
        editor=sharedGasLimit.edit();
        format =new DecimalFormat("0.000");

        yangQiLimitUp=findViewById(R.id.et_yangqi_up);
        yangQiLimitDown=findViewById(R.id.et_yangqi_down);
        yaSuoLimitUp=findViewById(R.id.et_yasuokongqi_up);
        yaSuoLimitDown=findViewById(R.id.et_yasuokongqi_down);
        xiaoQiLimitUp=findViewById(R.id.et_xiaoqi_up);
        xiaoQiLimitDown=findViewById(R.id.et_xiaoqi_down);
        erYangLimitUp=findViewById(R.id.et_eryanghuatan_up);
        erYangLimitDown=findViewById(R.id.et_eryanghuatan_down);
        fuYaLimitUp=findViewById(R.id.et_fuyaxiyin_up);
        fuYaLimitDown=findViewById(R.id.et_fuyaxiyin_down);

        yangQiLimitUp.setText(sharedGasLimit.getString("氧气上限", "0.450"));
        yangQiLimitDown.setText(sharedGasLimit.getString("氧气下限","0.400"));
        yaSuoLimitUp.setText(sharedGasLimit.getString("压缩空气上限","0.900"));
        yaSuoLimitDown.setText(sharedGasLimit.getString("压缩空气下限","0.450"));
        xiaoQiLimitUp.setText(sharedGasLimit.getString("笑气上限","0.450"));
        xiaoQiLimitDown.setText(sharedGasLimit.getString("笑气下限","0.100"));
        erYangLimitUp.setText(sharedGasLimit.getString("二氧化碳上限","0.400"));
        erYangLimitDown.setText(sharedGasLimit.getString("二氧化碳下限","0.350"));
        fuYaLimitUp.setText(sharedGasLimit.getString("负压吸引上限","0.030"));
        fuYaLimitDown.setText(sharedGasLimit.getString("负压吸引下限","0.070"));

        yangQiLimitUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("氧气上限",yangQiLimitUp.getText().toString());
                editor.commit();
                return false;
            }
        });

        yangQiLimitDown.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("氧气下限",yangQiLimitDown.getText().toString());
                editor.commit();
                return false;
            }
        });

        yaSuoLimitUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("压缩空气上限",yaSuoLimitUp.getText().toString());
                editor.commit();
                return false;
            }
        });

        yaSuoLimitDown.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("压缩空气下限",yaSuoLimitDown.getText().toString());
                editor.commit();
                return false;
            }
        });

        xiaoQiLimitUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("笑气上限",xiaoQiLimitUp.getText().toString());
                editor.commit();
                return false;
            }
        });

        xiaoQiLimitDown.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("笑气下限",xiaoQiLimitDown.getText().toString());
                editor.commit();
                return false;
            }
        });

        erYangLimitUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("二氧化碳上限",erYangLimitUp.getText().toString());
                editor.commit();
                return false;
            }
        });

        erYangLimitDown.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("二氧化碳下限",erYangLimitDown.getText().toString());
                editor.commit();
                return false;
            }
        });

        fuYaLimitUp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("负压吸引上限",fuYaLimitUp.getText().toString());
                editor.commit();
                return false;
            }
        });

        fuYaLimitDown.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                editor.putString("负压吸引下限",fuYaLimitDown.getText().toString());
                editor.commit();
                return false;
            }
        });
    }

    public void gasSetExit(View view){
        finish();
    }
}
