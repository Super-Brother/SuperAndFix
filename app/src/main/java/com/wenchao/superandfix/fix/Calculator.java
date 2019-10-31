package com.wenchao.superandfix.fix;

import android.content.Context;
import android.widget.Toast;

import com.wenchao.superandfix.MethodReplace;

public class Calculator {

    @MethodReplace(className = "com.wenchao.superandfix.Calculator", methodName = "calculate")
    public void calculate(Context context) {
        int a = 666;
        int b = 1;
        Toast.makeText(context, "a/b=" + a / b, Toast.LENGTH_SHORT).show();
    }
}
