package net.mamoe.mirai.rainey

import kotlin.math.sqrt


suspend fun ocGen() :String{
   var res = "根据阿雨分析，你的理想OC大约是这个样子……\n"
   res += "身高："+normalRandom(168.0,36.0).toInt()+"cm\n"
   res += "性别："+genderGen()+"\n"
   res += "发色："+colorGen()+"发\n"
   res += "瞳色："+colorGen()+"瞳\n"
   res += "发型："+randomGen("Y:data\\hairstyle_front.txt")
   res += "，并且有着"+randomGen("Y:data\\hairstyle_back.txt")+"作为特征\n"
   res += "属性："+randomGen("Y:data\\characteristics_outer.txt")
   res += "，但实际上"+randomGen("Y:data\\characteristics_inner.txt")+"\n"
   res += "除此之外，TA还是一名骄傲的"+randomGen("Y:data\\occupation.txt")+"\n"
   res += "喜欢阿雨的分析吗？如果不够满意，可以再来一遍（）"
   return res
}

fun normalRandom(a: Double, b: Double): Double { //注意这里的b是方差，等于标准差的平方
   val temp = 12.0
   var x = 0.0
   var i = 0
   while (i < temp) {
      x += Math.random()
      i++
   }
   x = (x - temp / 2) / sqrt(temp / 12)
   x = a + x * sqrt(b)
   return x
}

fun cupGen(): String{
   val a = normalRandom(2.0,4.0).toInt()
   if(a<0)
      return "AA"
   else if(a<=1)
      return "A"
   else
      return (a+63).toChar().toString()
}

fun colorGen(): String{
   val a = Math.random()
   if(a<0.3)
      return "黑"
   else{
      val data = ReadData("Y:data\\colors.txt")
      if(data!=null){
         return data[((a-0.3)/0.7*data.size).toInt()].replace(Regex(" .*"),"")
      }
      else
         return "无"
   }
}

fun randomGen(path:String):String{
   val a = Math.random()
   val data = ReadData(path)
   if(data!=null){
      return data[(a*data.size).toInt()].replace(Regex(" .*"),"")
   }
   else
      return "无"
}

fun genderGen(): String{
   val a = Math.random()
   if(a<0.45)
      return "女"
   else if(a<0.9)
      return "男"
   else if (a<0.92)
      return "女心男身"
   else if (a<0.94)
      return "男心女身"
   else if (a<0.96)
      return "扶他"
   else if (a<0.98)
      return "扶她"
   else
      return "无性别"
}