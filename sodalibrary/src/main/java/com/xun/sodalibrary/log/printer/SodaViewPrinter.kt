package com.xun.sodalibrary.log.printer

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xun.sodalibrary.R
import com.xun.sodalibrary.log.SodaLogConfig
import com.xun.sodalibrary.log.bean.SodaLogBean
import com.xun.sodalibrary.utils.dp2px
import com.xun.sodalibrary.utils.getColorByString
import kotlinx.android.synthetic.main.sodalog_item.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/17
 */

class SodaViewPrinter(activity: Activity) : SodaLogPrinter {
    companion object {
        const val TAG_LOG_VIEW = "TAG_LOG_VIEW"
    }

    private var recyclerView: RecyclerView? = null
    private var adapter: LogAdapter? = null
    private var rootView: FrameLayout? = null
    private var logView: View? = null
    private var isOpen: Boolean = false

    init {
        rootView = activity.findViewById(android.R.id.content)
        recyclerView = RecyclerView(activity)
        adapter = LogAdapter()
        val layoutManager = LinearLayoutManager(recyclerView!!.context)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = adapter
    }

    override fun print(config: SodaLogConfig, level: Int, tag: String, printString: String) {
        adapter?.addItem(SodaLogBean(System.currentTimeMillis(), level, tag, printString))
        recyclerView?.smoothScrollToPosition(adapter?.itemCount ?: 1 - 1)
    }

    fun showLogView() {
        if (rootView?.findViewWithTag<View>(TAG_LOG_VIEW) != null) {
            return
        }
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            300.dp2px
        )
        params.gravity = Gravity.BOTTOM
        val logView: View = getLogView()
        logView.tag = TAG_LOG_VIEW
        rootView?.addView(logView, params)
        isOpen = true
    }

    private fun getLogView(): View {
        if (logView != null) {
            return logView!!
        }
        logView = FrameLayout(recyclerView?.context!!)
        logView?.setBackgroundColor(Color.parseColor("#77000000"))
        (logView as ViewGroup).addView(recyclerView)

        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.END
        val closeView = TextView(rootView!!.context)
        closeView.setOnClickListener { closeLogView() }
        closeView.text = "close"
        (logView as ViewGroup).addView(closeView, params)
        return logView!!
    }

    private fun closeLogView() {
        isOpen = false
        rootView?.removeView(getLogView())
    }

    inner class LogAdapter : RecyclerView.Adapter<LogViewHolder>() {
        private val logs: MutableList<SodaLogBean> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
            val item = LayoutInflater.from(recyclerView?.context)
                .inflate(R.layout.sodalog_item, parent, false)
            return LogViewHolder(item)
        }

        override fun getItemCount(): Int = logs.size

        override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
            logs[position].apply {
                holder.itemView.mTagTv.setTextColor(getHighlightColor(level))
                holder.itemView.mMessageTv.setTextColor(getHighlightColor(level))

                holder.itemView.mTagTv.text = getFlattened()
                holder.itemView.mMessageTv.text = log
            }
        }

        fun addItem(logItem: SodaLogBean) {
            logs.add(logItem)
            notifyItemInserted(logs.size - 1)
        }

        /**
         * 跟进log级别获取不同的高了颜色
         *
         * @param logLevel log 级别
         * @return 高亮的颜色
         */
        private fun getHighlightColor(logLevel: Int): Int {
            return when (logLevel) {
                Log.VERBOSE -> -0x444445
                Log.DEBUG -> -0x1
                Log.INFO -> getColorByString("#00ff00")
                Log.WARN -> -0x444ad7
                Log.ERROR -> -0x9498
                else -> -0x100
            }
        }
    }

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}