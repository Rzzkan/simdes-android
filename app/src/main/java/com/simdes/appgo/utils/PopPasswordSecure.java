/*
 * Create by Brian Dwi Murdianto
 * Email : dwibrian120@gmail.com
 */

package com.simdes.appgo.utils;

import android.app.Dialog;
import android.content.Context;

public class PopPasswordSecure {
    public Context context;
    public Dialog dialog;

    public PopPasswordSecure(Context context){
        this.context = context;
        dialog = new Dialog(context);
    }

    public void popCheckDismis(){
        dialog.dismiss();
    }
}
