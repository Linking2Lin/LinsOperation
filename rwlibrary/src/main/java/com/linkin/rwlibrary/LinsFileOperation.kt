package com.linkin.rwlibrary

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

/**
 * @author Lin
 * @date 2022/7/12
 * ----------------------------------------------------
 * Tell me God,are you punishing me?                  *
 * Is this the price I'm paying for my past mistakes? *
 * ----------------------------------------------------
 * 对外接口
 */

object LinsFileOperation {
    private val scope = CoroutineScope(Dispatchers.IO)
    val gson: Gson = GsonBuilder().create()

    /**
     * 从文件内读取
     * @param path 存储路径
     * @param name 文件名
     * @param type 储存的文件的类型
     */
    inline fun <reified T> read(path: String?, name: String): T {
        val file = if (path == null) {
            File(name)
        } else {
            File("$path${File.separator}$name")
        }
        //println(file.toString())
        if (!file.exists()) {
            throw FileNotFoundException("文件不存在")
        } else {
            val reader = InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)
            val jsonString = reader.readText()
            // println(jsonString)
            val typeOf = object : TypeToken<T>() {}.type
            // println(typeOf.typeName)
            return gson.fromJson(jsonString, typeOf)
        }
        //return type
    }

    /**
     * 写入到文件
     * @param path 存储路径
     * @param name 文件名
     */
    fun <T> write(path: String?, name: String, datas: T): Boolean {
        val file = if (path == null) {
            File(name)
        } else {
            File("$path${File.separator}$name")
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        return try {
            val writer = OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8)
            val content = gson.toJson(datas)
            println(content)
            writer.write(content)
            writer.flush()
            writer.close()
            true
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }


    }

}