package net.mamoe.mirai.rainey

import java.io.*

fun mReadTxtFile(strFilePath: String): String? {
    var content = "" //�ļ������ַ���
    //���ļ�
    val file = File(strFilePath)
    //���path�Ǵ��ݹ����Ĳ�����������һ����Ŀ¼���ж�
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
                //���ж�ȡ
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
    var content = emptyArray<String>() //�ļ������ַ���
    //���ļ�
    val file = File(strFilePath)
    //���path�Ǵ��ݹ����Ĳ�����������һ����Ŀ¼���ж�
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
                //���ж�ȡ
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
