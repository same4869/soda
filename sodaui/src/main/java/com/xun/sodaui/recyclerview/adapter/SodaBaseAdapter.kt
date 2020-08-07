package com.xun.sodaui.recyclerview.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xun.sodaui.recyclerview.interf.AdapterItemView
import com.xun.sodaui.recyclerview.interf.AdapterUIMappingProtocol

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/5
 */

abstract class SodaBaseAdapter<T>(val data: MutableList<T> = ArrayList()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    AdapterUIMappingProtocol<T> {
    private var headers = SparseArray<View>()
    private var footers = SparseArray<View>()

    private var BASE_ITEM_TYPE_HEADER = 1000000
    private var BASE_ITEM_TYPE_FOOTER = 2000000

    fun addHeaderView(view: View) {
        if (headers.indexOfValue(view) < 0) {
            headers.put(BASE_ITEM_TYPE_HEADER++, view)
            notifyItemChanged(headers.size() - 1)
        }
    }

    fun removeHeaderView(view: View) {
        val indexOfValue = headers.indexOfValue(view)
        if (indexOfValue < 0) return
        headers.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue)
    }

    fun addFooterView(view: View) {
        if (footers.indexOfValue(view) < 0) {
            footers.put(BASE_ITEM_TYPE_FOOTER++, view)
            notifyItemInserted(itemCount)
        }
    }

    fun removeFooterView(view: View) {
        val indexOfValue = footers.indexOfValue(view)
        if (indexOfValue < 0) return
        footers.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue + getHeaderSize() + getOriginalItemSize())
    }

    fun getHeaderSize(): Int {
        return headers.size()
    }

    fun getFooterSize(): Int {
        return footers.size()
    }

    fun getOriginalItemSize(): Int {
        return data.size
    }

    private fun isFooterPosition(position: Int): Boolean {
        return position >= getHeaderSize() + getOriginalItemSize()
    }

    private fun isHeaderPosition(position: Int): Boolean {
        return position < headers.size()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(headers.indexOfKey(viewType) >= 0){
            val view = headers[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }

        if (footers.indexOfKey(viewType) >= 0) {
            val view = footers[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }

        val item = createItem(viewType)
        return CommonViewHolder(item)
    }

    override fun getItemCount(): Int = data.size + getHeaderSize() + getFooterSize()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position)) return

        val itemPosition = position - getHeaderSize()
        (holder as CommonViewHolder<T>).item?.bindData(data[itemPosition], itemPosition)
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position)) {
            return headers.keyAt(position)
        }

        if (isFooterPosition(position)) {
            val footerPosition = position - getHeaderSize() - getOriginalItemSize()
            return footers.keyAt(footerPosition)
        }

        val itemPosition = position - getHeaderSize()
        return getItemType(data[itemPosition])
    }

    protected class CommonViewHolder<T> internal constructor(var item: AdapterItemView<T>?) :
        RecyclerView.ViewHolder(if (item is View) item else throw RuntimeException("item view must is view"))

}