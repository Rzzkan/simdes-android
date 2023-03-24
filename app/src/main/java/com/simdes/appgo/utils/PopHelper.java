package com.simdes.appgo.utils;

import android.app.Dialog;
import android.content.Context;

import com.simdes.appgo.R;

public class PopHelper {

    Context context;
    Dialog dialog;

    public PopHelper(Context context){
        this.context = context;
        dialog = new Dialog(context);
    }

    public void popLoading(){
        dialog.setContentView(R.layout.pop_loading);
        dialog.show();
    }

    public void popDismiss(){
        dialog.dismiss();
    }
}
