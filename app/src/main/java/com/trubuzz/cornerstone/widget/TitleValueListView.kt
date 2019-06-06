package com.trubuzz.cornerstone.widget

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.api.RetrofitUtil
import com.trubuzz.cornerstone.common.activity.PdfViewActivity
import com.trubuzz.cornerstone.common.model.bean.TitleValueData
import com.trubuzz.cornerstone.util.FormatUtils
import com.trubuzz.cornerstone.util.download.ProgressCallBack
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody

import com.trubuzz.cornerstone.common.activity.PdfViewActivity.ARG_FILE_TYPE
import com.trubuzz.cornerstone.common.activity.PdfViewActivity.ARG_PDF_DIR
import com.trubuzz.cornerstone.common.activity.PdfViewActivity.ARG_PDF_NAME
import com.trubuzz.cornerstone.util.download.DownLoadSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_title_value_list.view.*
import org.jetbrains.anko.toast


class TitleValueListView(internal var mContext: Context, internal var bean: TitleValueData) : RelativeLayout(mContext) {

    init {
        View.inflate(context, R.layout.view_title_value_list, this)
        init()
    }

    private fun init() {

        title.text = bean.title
        value.text = bean.value

        if (bean.style.color != null) {
            value.setTextColor(Color.parseColor(bean.style.color))
        }

        if (bean.style.formats != null) {
            FormatUtils.formatTextView(
                bean.value,
                bean.style.formats,
                value,
                this.context
            )
        }

        if (bean.action.type != null) {
            val type = bean.action.type
            value.setOnClickListener {
                if (type == "download") {
                    val rxPermissions = RxPermissions(mContext as Activity)
                    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe { aBoolean ->
                            if (aBoolean!!) {
                                downloadPdf(bean.action.url)
                            } else {

                            }
                        }

                } else if (type == "web") {

                }
            }
        }
    }

    private fun downloadPdf(loadUrl: String) {
        val progressDialog = ProgressDialog(mContext, R.style.DownloadDialog)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setTitle("Download")
        progressDialog.setCancelable(true)
        progressDialog.max = 100
        progressDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.ModalCancel)
        ) { dialog, which -> dialog.dismiss() }
        progressDialog.show()

        val fileName = loadUrl.substring(loadUrl.lastIndexOf("/") + 1, loadUrl.lastIndexOf("."))
        val fileType = loadUrl.substring(loadUrl.lastIndexOf("."), loadUrl.length)
        val destFileDir = Environment.getExternalStorageDirectory().toString() + "/Download"
        val destFileName = (fileName + fileType).replace("/", "")

        load(loadUrl, object : ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
            override fun onStart() {
                super.onStart()
            }

            override fun onCompleted() {
                progressDialog.dismiss()
            }

            override fun onSuccess(responseBody: ResponseBody) {
                val intent = Intent(mContext, PdfViewActivity::class.java)
                intent.putExtra(ARG_PDF_DIR, destFileDir)
                intent.putExtra(ARG_PDF_NAME, fileName)
                intent.putExtra(ARG_FILE_TYPE, fileType)
                mContext.startActivity(intent)
            }

            override fun progress(progress: Long, total: Long) {
                progressDialog.max = total.toInt()
                progressDialog.progress = progress.toInt()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                mContext.toast(R.string.DownloadFail)
            }
        })
    }

    fun load(downUrl: String, callBack: ProgressCallBack<ResponseBody>) {
        RetrofitUtil.getApiService()
            .download(downUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnNext(Consumer<ResponseBody> { responseBody -> callBack.saveFile(responseBody) })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(DownLoadSubscriber<ResponseBody>(callBack))
    }
}
