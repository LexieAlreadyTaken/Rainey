package net.mamoe.mirai.rainey

import java.io.*

fun mReadTxtFile(strFilePath: String): String? {
    var content = "" //文件内容字符串
    //打开文件
    val file = File(strFilePath)
    //如果path是传递过来的参数，可以做一个非目录的判断
    if (!file.isDirectory) {
        try {
            val instream: InputStream = FileInputStream(file)
            if (instream != null) {
                var line: String? = null
                val buffreader = BufferedReader(
                        InputStreamReader(
                                FileInputStream(file), "UTF-8"
                        )
                )
                //分行读取
                while (buffreader.readLine().also { line = it } != null) {
                    content += "$line;"
                }
                instream.close()
            }
        } catch (e: FileNotFoundException) {
            println("The File doesn't not exist.")
        } catch (e: IOException) {
            println(e.message!!)
        }
    }
    return content
}


fun ReadData(strFilePath: String): Array<String>? {
    var content = emptyArray<String>() //文件内容字符串
    //打开文件
    val file = File(strFilePath)
    //如果path是传递过来的参数，可以做一个非目录的判断
    if (file.isDirectory) {
    } else {
        try {
            val instream: InputStream = FileInputStream(file)
            if (instream != null) {
                var line: String? = null
                val buffreader = BufferedReader(
                        InputStreamReader(
                                FileInputStream(file), "UTF-8"
                        )
                )
                //分行读取
                while (buffreader.readLine().also { line = it } != null) {
                    content = content.plus("$line")
                }
                instream.close()
            }
        } catch (e: FileNotFoundException) {
            println("The File doesn't not exist.")
        } catch (e: IOException) {
            println(e.message!!)
        }
    }
    return content
}
