package com.wenchao.superandfix;

import android.content.Context;
import android.widget.Toast;

public class Calculator {

    public void calculate(Context context) {
        int a = 666;
        int b = 0;
        Toast.makeText(context, "a/b=" + a / b, Toast.LENGTH_SHORT).show();
    }
}
