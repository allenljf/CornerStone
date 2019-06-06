package com.trubuzz.cornerstone.common.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.trubuzz.cornerstone.R;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity implements OnPageChangeListener {
    public static String ARG_PDF_DIR = "ARG_PDF_DIR";
    public static String ARG_PDF_NAME = "ARG_PDF_NAME";
    public static String ARG_FILE_TYPE = "ARG_FILE_TYPE";
    String pdfDir;
    String pdfName;
    String pdfType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfDir = getIntent().getStringExtra(ARG_PDF_DIR);
        pdfName = getIntent().getStringExtra(ARG_PDF_NAME);
        pdfType = getIntent().getStringExtra(ARG_FILE_TYPE);

        ImageView right_icon = findViewById(R.id.right_icon);
        right_icon.setVisibility(View.GONE);

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setVisibility(View.VISIBLE);
        leftIcon.setImageResource(R.drawable.back_gold);
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = findViewById(R.id.toolbar_title);
        title.setVisibility(View.VISIBLE);
        title.setText(pdfName);

        PDFView pdfView = findViewById(R.id.pdfView);
        File file = new File(pdfDir, pdfName + pdfType);
        pdfView.fromFile(file)
                .defaultPage(1)
                .onPageChange(this)
                .swipeVertical(true)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}