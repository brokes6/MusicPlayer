package com.example.musicplayerdome.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.example.musicplayerdome.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 加载中Dialog
 * 采用的'com.wang.avi:library:2.1.3'框架（需要写混淆）
 * @author fuxinbo
 */
public class LoadingsDialog extends AlertDialog {

    private static LoadingsDialog loadingDialog;
    private AVLoadingIndicatorView avi;

    public static LoadingsDialog getInstance(Context context) {
        loadingDialog = new LoadingsDialog(context, R.style.TransparentDialog); //设置AlertDialog背景透明
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

    public LoadingsDialog(Context context, int themeResId) {
        super(context,themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        avi = this.findViewById(R.id.avi);
    }

    @Override
    public void show() {
        super.show();
        avi.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        avi.hide();
    }
}