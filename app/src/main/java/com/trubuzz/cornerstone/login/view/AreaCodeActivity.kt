package com.trubuzz.cornerstone.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trubuzz.cornerstone.Constant
import com.trubuzz.cornerstone.R
import com.trubuzz.cornerstone.base.BaseAdapter
import com.trubuzz.cornerstone.main.model.bean.TitleValueBean
import com.trubuzz.cornerstone.widget.codePicker.*
import kotlinx.android.synthetic.main.activity_area_code.*
import kotlinx.android.synthetic.main.list_item_area_code.view.*
import kotlinx.android.synthetic.main.view_toolbar_white.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


class AreaCodeActivity : AppCompatActivity() {
    var adapter: BaseAdapter<TitleValueBean>? = null
    val titleValueList: ArrayList<TitleValueBean>? = ArrayList()

    private val selectedCountries = ArrayList<Country>()
    private val allCountries = ArrayList<Country>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_code)
        initView()
    }

    fun initView() {
        toolbar.toolbar_title.text = getString(R.string.AreaNavigationTitle)
        toolbar.rl_left_icon.setOnClickListener {
            finishPage(intent.getStringExtra("ARG_CODE"))
        }

        //常用列表
        titleValueList?.add(TitleValueBean(getString(R.string.AreaChina), "+86"))
        titleValueList?.add(TitleValueBean(getString(R.string.AreaHongkong), "+852"))
        titleValueList?.add(TitleValueBean(getString(R.string.AreaTaiwan), "+886"))
        titleValueList?.add(TitleValueBean(getString(R.string.AreaAomen), "+853"))
        titleValueList?.add(TitleValueBean(getString(R.string.AreaSingapore), "+65"))
        titleValueList?.add(TitleValueBean(getString(R.string.AreaMalaysia), "+60"))

        rv_common_area_code.layoutManager = LinearLayoutManager(this)
        rv_common_area_code.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = BaseAdapter(R.layout.list_item_area_code, titleValueList) { view: View, item: TitleValueBean ->
            view.tv_name.text = item.country
            view.tv_code.text = item.code
            view.onClick {
                finishPage(item.code)
            }
        }
        rv_common_area_code.adapter = adapter
        adapter!!.notifyDataSetChanged()

        //分類列表
        allCountries.clear()
        allCountries.addAll(Country.getAll(this, null))
        selectedCountries.clear()
        selectedCountries.addAll(allCountries)

        val manager = LinearLayoutManager(this)
        rv_area_code.layoutManager = manager
        val adapter = CAdapter(selectedCountries)
        rv_area_code.setAdapter(adapter)
        rv_area_code.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //右邊字母導覽
        sidebar.addIndex("#", 0)
        sidebar.setOnLetterChangeListener(object : SideBar.OnLetterChangeListener {
            override fun onLetterChange(letter: String) {
                val position = adapter.getLetterPosition(letter)
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0)
                }
            }

            override fun onReset() {

            }
        })
    }

    fun finishPage(code: String) {
        val data = Intent()
        data.putExtra(Constant.ARG_AREA_CODE, code)
        setResult(Constant.AREA_CODE, data)
        finish()
    }

    //分類區碼列表Adapter
    internal inner class CAdapter(entities: List<PyEntity>) : PyAdapter<RecyclerView.ViewHolder>(entities) {

        override fun onCreateLetterHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LetterHolder(layoutInflater.inflate(R.layout.list_item_letter, parent, false))
        }

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return VH(layoutInflater.inflate(R.layout.list_item_area_code, parent, false))
        }

        override fun onBindHolder(holder: RecyclerView.ViewHolder, entity: PyEntity, position: Int) {
            val vh = holder as VH
            val country = entity as Country
            vh.ivFlag.setImageResource(country.flag)
            vh.tvName.setText(country.name)
            vh.tvCode.setText("+" + country.code)
            holder.itemView.setOnClickListener {
                finishPage("+" + country.code)
            }
        }

        override fun onBindLetterHolder(holder: RecyclerView.ViewHolder, entity: LetterEntity, position: Int) {
            (holder as LetterHolder).textView.text = entity.letter.toUpperCase()
        }
    }
}





