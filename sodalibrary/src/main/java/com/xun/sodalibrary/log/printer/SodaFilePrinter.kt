package com.xun.sodalibrary.log.printer

import com.xun.sodalibrary.log.SodaLogConfig
import com.xun.sodalibrary.log.bean.SodaLogBean
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/17
 */

class SodaFilePrinter() : SodaLogPrinter {
    companion object {
        val instance: SodaFilePrinter by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SodaFilePrinter()
        }
        val EXECUTOR = Executors.newSingleThreadExecutor()
        const val MAX_PRE_FILE_LENGTH = 1024 * 1024 * 5 //一个文件最大的大小,5M
    }

    private var mLogPath: String? = null
    private var mRetentionTime: Long = 0L
    private var writer: LogWriter? = null

    @Volatile
    private var worker: PrintWorker? = null

    /**
     * @param logPath       log保存路径，如果是外部路径需要确保已经有外部存储的读写权限
     * @param retentionTime log文件的有效时长，单位毫秒，<=0表示一直有效
     */
    fun init(logPath: String, retentionTime: Long) {
        mLogPath = logPath
        mRetentionTime = retentionTime
        writer = LogWriter()
        worker = PrintWorker()
        cleanExpiredLog()
    }

    override fun print(config: SodaLogConfig, level: Int, tag: String, printString: String) {
        if (mLogPath == null) {
            throw IllegalStateException("you must init SodaFilePrinter first")
        }
        val timeMillis = System.currentTimeMillis()
        if (!worker!!.isRunning()) {
            worker?.start()
        }
        worker?.put(SodaLogBean(timeMillis, level, tag, printString))
    }

    fun doPrint(sodaLogBean: SodaLogBean) {
        val lastFileName = writer!!.getPreFileName() ?: ""
        val logFile = File(mLogPath, lastFileName)
        if (lastFileName.isBlank() || logFile.length() > MAX_PRE_FILE_LENGTH) {
            val newFileName = genFileName()
            if (writer!!.isReady()) {
                writer?.close()
            }
            if (!writer!!.ready(newFileName)) {
                return
            }
        }
        writer!!.append(sodaLogBean.flattenedLog())
    }

    private fun genFileName(wantHms: Boolean = false): String {
        val sdf =
            if (wantHms) {
                SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA)
            } else {
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            }
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(System.currentTimeMillis()))
    }

    /**
     * 清除过期log
     */
    private fun cleanExpiredLog() {
        if (mRetentionTime <= 0) {
            return
        }
        val currentTimeMillis = System.currentTimeMillis()
        val logDir = File(mLogPath!!)
        val files: Array<File> = logDir.listFiles() ?: return
        for (file in files) {
            if (currentTimeMillis - file.lastModified() > mRetentionTime) {
                file.delete()
            }
        }
    }

    private inner class PrintWorker : Runnable {
        private val logs: BlockingQueue<SodaLogBean> =
            LinkedBlockingQueue()

        @Volatile
        private var running = false

        /**
         * 将log放入打印队列
         *
         * @param log 要被打印的log
         */
        fun put(log: SodaLogBean) {
            try {
                logs.put(log)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        /**
         * 判断工作线程是否还在运行中
         *
         * @return true 在运行
         */
        fun isRunning(): Boolean {
            synchronized(this) { return running }
        }

        /**
         * 启动工作线程
         */
        fun start() {
            synchronized(this) {
                EXECUTOR.execute(this)
                running = true
            }
        }

        override fun run() {
            var log: SodaLogBean?
            try {
                while (true) {
                    log = logs.take()
                    doPrint(log)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
                synchronized(this) {
                    running = false
                }
            }
        }

    }

    private inner class LogWriter {
        private var preFileName: String? = null
        private var logFile: File? = null
        private var bufferedWriter: BufferedWriter? = null

        fun isReady(): Boolean {
            return bufferedWriter != null
        }

        fun getPreFileName(): String? {
            return preFileName
        }

        //找一个合适的文件记录
        fun findTargetDir(newFileName: String): File {
            val logDir = File(mLogPath!!)
            val files: Array<File> = logDir.listFiles() ?: return File(mLogPath, genFileName(true))
            for (file in files) {
                if (file.name.startsWith(newFileName) && file.length() < MAX_PRE_FILE_LENGTH) {
                    return file
                }
            }
            return File(mLogPath, genFileName(true))
        }

        /**
         * log写入前的准备操作
         *
         * @param newFileName 要保存log的文件名
         * @return true 表示准备就绪
         */
        fun ready(newFileName: String): Boolean {
            preFileName = findTargetDir(newFileName).name
//            logFile = File(mLogPath, newFileName)

//            if (!logFile!!.exists()) {
//                try {
//                    val parent = logFile?.parentFile
//                    if (parent != null && !parent.exists()) {
//                        parent.mkdirs()
//                    }
//                    logFile?.createNewFile()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                    preFileName = null
//                    logFile = null
//                    return false
//                }
//            }

            val partLogFile = findTargetDir(newFileName)
            if (!partLogFile.exists()) {
                try {
                    val parent = partLogFile.parentFile
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs()
                    }
                    partLogFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    preFileName = null
                    logFile = null
                    return false
                }
            }

            try {
                bufferedWriter = BufferedWriter(FileWriter(partLogFile, true))
            } catch (e: Exception) {
                e.printStackTrace()
                preFileName = null
                logFile = null
                return false
            }

            return true
        }

        /**
         * 关闭bufferedWriter
         */
        fun close(): Boolean {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                } finally {
                    bufferedWriter = null
                    preFileName = null
                    logFile = null
                }
            }
            return true
        }

        /**
         * 将log写入文件
         *
         * @param flattenedLog 格式化后的log
         */
        fun append(flattenedLog: String) {
            try {
                bufferedWriter?.write(flattenedLog)
                bufferedWriter?.newLine()
                bufferedWriter?.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}