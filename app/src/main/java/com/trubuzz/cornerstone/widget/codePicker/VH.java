package com.trubuzz.cornerstone.widget.codePicker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.trubuzz.cornerstone.R;

public class VH extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView tvCode;
    public ImageView ivFlag;

    public VH(View itemView) {
        super(itemView);
        ivFlag = (ImageView) itemView.findViewById(R.id.iv_flag);
        ivFlag.setVisibility(View.GONE);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCode = (TextView) itemView.findViewById(R.id.tv_code);
    }
}
