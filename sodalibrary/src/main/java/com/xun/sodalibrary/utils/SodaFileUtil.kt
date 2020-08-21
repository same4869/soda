package com.xun.sodalibrary.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/19
 */

object SodaFileUtil {
    //删除单个文件
    fun deleteFile(file: File) {
        if (file.isDirectory) {
            val files = file.listFiles()
            for (i in files.indices) {
                val f = files[i]
                deleteFile(f)
            }
            file.delete()//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete()
        }
    }

    //判断文件是否存在
    fun fileIsExists(strFile: String): Boolean {
        try {
            val f = File(strFile)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    /**
     * 复制单个文件
     *
     * @param oldFilePath String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newFilePath String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return `true` if and only if the file was copied;
     * `false` otherwise
     */
    fun copyFile(oldFilePath: String, newFilePath: String): Boolean {
        return try {
            val oldFile = File(oldFilePath)
            if (!oldFile.exists() || !oldFile.isFile || !oldFile.canRead()) {
                return false;
            }
            val fileInputStream = FileInputStream(oldFilePath) //读入原文件
            val fileOutputStream = FileOutputStream(newFilePath)
            val buffer = ByteArray(1024)
            var byteRead: Int
            while (fileInputStream.read(buffer).also { byteRead = it } != -1) {
                fileOutputStream.write(buffer, 0, byteRead)
            }
            fileInputStream.close()
            fileOutputStream.flush()
            fileOutputStream.close()
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getFileMD5(file: File): String? {
        if (!file.isFile) {
            return null
        }
        var digest: MessageDigest? = null
        var `in`: FileInputStream? = null
        val buffer = ByteArray(1024)
        var len: Int
        try {
            digest = MessageDigest.getInstance("MD5")
            `in` = FileInputStream(file)
            len = `in`.read(buffer, 0, 1024)
            while (len != -1) {
                digest!!.update(buffer, 0, len)
                len = `in`.read(buffer, 0, 1024)
            }
            `in`.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val bigInt = BigInteger(1, digest!!.digest())
        val md5Str = bigInt.toString(16)
        return if (md5Str.length < 16) {
            val temp0 = 16 - md5Str.length
            val sb = StringBuilder()
            for (i in 0 until temp0) {
                sb.append("0")
            }
            sb.append(md5Str)
            sb.toString()
        } else {
            md5Str
        }
    }

}