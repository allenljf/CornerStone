package com.trubuzz.cornerstone.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import com.trubuzz.cornerstone.R;


public class CustomLoadingDialog extends ProgressDialog {

    public CustomLoadingDialog(Context context) {
        super(context);
    }

    public CustomLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.view_loading_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }
}

