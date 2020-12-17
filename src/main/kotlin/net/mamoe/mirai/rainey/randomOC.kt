package net.mamoe.mirai.rainey

import kotlin.math.sqrt


suspend fun ocGen() :String{
   var res = "���ݰ���������������OC��Լ��������ӡ���\n"
   res += "��ߣ�"+normalRandom(164.0,36.0).toInt()+"cm\n"
   res += "�Ա�"+genderGen()+"\n"
   res += "��ɫ��"+colorGen()+"��\n"
   res += "ͫɫ��"+colorGen()+"ͫ\n"
   res += "���ͣ�"+randomGen("Y:data\\hairstyle_front.txt")
   res += "����������"+randomGen("Y:data\\hairstyle_back.txt")+"��Ϊ����\n"
   res += "���ԣ�"+randomGen("Y:data\\characteristics_outer.txt")
   res += "����ʵ����"+randomGen("Y:data\\characteristics_inner.txt")+"\n"
   res += "����֮�⣬������һ��������"+randomGen("Y:data\\occupation.txt")+"\n"
   res += "ϲ������ķ���������������⣬��������һ�飨��"
   return res
}

fun normalRandom(a: Double, b: Double): Double { //ע�������b�Ƿ�����ڱ�׼���ƽ��
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
      return "��"
   else{
      val data = ReadData("Y:data\\colors.txt")
      if(data!=null){
         return data[((a-0.3)/0.7*data.size).toInt()].replace(Regex(" .*"),"")
      }
      else
         return "��"
   }
}

fun randomGen(path:String):String{
   val a = Math.random()
   val data = ReadData(path)
   if(data!=null){
      return data[(a*data.size).toInt()].replace(Regex(" .*"),"")
   }
   else
      return "��"
}

fun genderGen(): String{
   val a = Math.random()
   if(a<0.45)
      return "Ů"
   else if(a<0.9)
      return "��"
   else if (a<0.92)
      return "Ů������"
   else if (a<0.94)
      return "����Ů��"
   else if (a<0.96)
      return "����"
   else if (a<0.98)
      return "����"
   else
      return "���Ա�"
}