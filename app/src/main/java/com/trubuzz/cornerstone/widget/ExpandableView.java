package com.trubuzz.cornerstone.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.trubuzz.cornerstone.R;


public class ExpandableView extends RelativeLayout {
    private boolean isExpanded = true;
    TextView title;
    ImageView arrow;
    ExpandableRelativeLayout expandableLayout;
    LinearLayout container;

    public ExpandableView(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_expandable_layout, this);
        init();
    }

    public void init() {
        title = findViewById(R.id.title);
        arrow = findViewById(R.id.arrow);
        expandableLayout = findViewById(R.id.expandableLayout);
        container = findViewById(R.id.container);

        arrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded) {
                    expandableLayout.collapse();
                    arrow.setImageResource(R.drawable.down);
                } else {
                    expandableLayout.expand();
                    arrow.setImageResource(R.drawable.up);
                }
                isExpanded = !isExpanded;
            }
        });
    }

    public void setTitle(String value){
        title.setText(value);
    }

    public void addView(View view){
        container.addView(view);
    }
}
