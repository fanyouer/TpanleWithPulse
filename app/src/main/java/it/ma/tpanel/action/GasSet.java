package it.ma.tpanel.action;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by K on 2017/8/24.
 */

public class GasSet extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_set);

    }

    public void gasSetExit(View view){
        finish();
    }
}
