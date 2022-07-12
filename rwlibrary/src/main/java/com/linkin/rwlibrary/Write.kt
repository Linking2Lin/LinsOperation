package com.linkin.rwlibrary

import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

val ioScope = CoroutineScope(Dispatchers.IO)

fun write(): String{
    val temp: Persion = Persion(
        "HHH",
        50,
        Property("属性1","属性2")
    )

    val gson = GsonBuilder().create()

    val jsonString = gson.toJson(temp)

    println(jsonString)
    val file = File("a.json")

    ioScope.launch {
        file.createNewFile()
        val writer = OutputStreamWriter(FileOutputStream("a.json"),StandardCharsets.UTF_8)
        writer.write(jsonString)
        writer.flush()
        writer.close()
    }
    Thread.sleep(1000)
    return jsonString
}

fun read(): String {
    val file = File("a.json")
    val reader = InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)
    return reader.readText()
}

fun main() {
    //write()
//    println(read())
//    val gson = GsonBuilder().create()
//    val typeOf = object : TypeToken<Persion>() {}.type
//    val p = gson.fromJson<Persion>(read(),typeOf)
//    println(p)
//    println(p.name)
//    println(Persion::class)
//    println(Persion::class.java)
    val read = LinsFileOperation.read<Persion>(null,"a.json")
    println(read)
    LinsFileOperation.write(null,"c.json",read)
    //LinsFileOperation.read("a","b",Persion::class.java)
}