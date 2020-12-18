package net.mamoe.mirai.rainey

import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.MemberLeaveEvent
import net.mamoe.mirai.event.events.MemberNudgedEvent
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


suspend fun main() {
    val qqId = 2028829835L//Bot��QQ�ţ���ΪLong���ͣ��ڽ�β����Ӵ�дL
    val password = "ArcosDorados1234"//Bot������
    val configuration = BotConfiguration()
    var lastMessage = ""
    var thisMessage: String
    val fudued = arrayOfNulls<Boolean>(1)
    val retractOn = arrayOfNulls<Boolean>(1)
    //var welcomeMessage : String
    configuration.protocol = MiraiProtocol.ANDROID_PAD
    configuration.fileBasedDeviceInfo("deviceInfo.json")

    val miraiBot = Bot(qqId, password, configuration).alsoLogin()//�½�Bot����¼
    DBConn.getConnection();
    miraiBot.subscribeMessages {
        /*����֮��������Բ���*/
        "���" reply "��á�"

        (case("on")){
            fudued.set(0,false)
            retractOn.set(0,false)
        }

        (startsWith("����") and contains("��ʼ����")){
            fudued.set(0,false)
        }

        (startsWith("����") and contains("ֹͣ����")){
            fudued.set(0,true)
        }

        (startsWith("����") and contains("���ӳ���")){
            retractOn.set(0,false)
        }

        (startsWith("����") and contains("���ɳ���")){
            retractOn.set(0,false)
        }

        (contains("")){
            thisMessage = message.contentToString()
            if(thisMessage == lastMessage && fudued.get(0)==false) {
                reply(thisMessage)
                fudued.set(0,true)
                Timer().schedule(object:TimerTask(){
                    override fun run() {
                        fudued.set(0,false)
                    }
                }, 10000)
            }
            lastMessage = thisMessage
        }




        /*�����Ի����Ĳ���*/
        atBot(){
            reply("������������Ҳ�����ԡ���")
            reply(At(sender as Member)+"����Ц��")
        }

        (matching(Regex("����.?"))){
            reply("���ҡ���ô�ˡ���")
        }

        (startsWith("����") and contains("��")){
            reply("��������һ�£�Ȼ�������һ��С�������ӵ��������������������")
        }
        (startsWith("����") and contains("��")){
            reply("���ƺ�����˼��ؿ����㣩����˵�����������¡���ֻ�ܺ�ϲ��������Ŷ��")
        }
        (startsWith("����") and contains("ϲ��")){
            reply("ϲ�����𣿡�������Ϊ�ҵ��Ը�̫�����ˡ���������Ҳ��������Ҳ����")
        }
        (startsWith("����") and contains("�ִ�")){
            reply("����Ѹ�ٵطɺ죬ת����ȥ����������������̬����")
        }




        /*�̵��йصĲ���*/
        (startsWith("����") and contains("ǩ��")){
            reply("�����ǩ�������Ѿ�������ĳ��˸������Ի����ʺ��ܡ�ֻ��Ҫ�ٻ����겢˵������á��Ϳ��Զ԰�������ʺ�")
        }

        (startsWith("����") and contains("���")){
            val queryRes = DBConn.query("select * from customer where id = "+sender.id+";")
            if(queryRes!=null) {
                val randNum = (1..20).random()
                if (queryRes.isBeforeFirst) {
                    queryRes.next()
                    if(!queryRes.getBoolean("signed_in")) {
                        DBConn.query("update customer set coin = coin+ $randNum,sign_in = sign_in + 1, signed_in = true where id = " + sender.id + ";")
                        when(queryRes.getInt("sign_in")){
                            0 -> reply("лл�������ҡ������" + randNum + "����˿����������ˡ���")
                            in 1..3 -> reply("лл�����Ҳ�����ҡ������" + randNum + "����˿����������ˡ���")
                            in 4..7 -> reply("�ܸ��˽���Ҳ�ܼ����㡣�����" + randNum + "����˿����������ˡ���")
                            else -> reply(sender.nameCardOrNick+"����˵ʲô�������"+randNum+"��˿�����ˣ�����������Ц��")
                        }
                    }
                    else
                        reply("лл�������ҡ��ܸ���һ��֮�����ٴμ����㡣")
                }
                else {
                    reply("����ף����Ͻ������Լ�����ɬ�������ǳ������ܽ�����������ѡ������" + randNum + "����˿����������ˡ���")
                    DBConn.query("insert into customer values (" + sender.id + ", " + randNum + ",1,true);")
                }
            }
        }

        (startsWith("����") and contains("����")){
            val queryRes = DBConn.query("select * from customer where id = "+sender.id+";")
            if(queryRes!=null){
                if(queryRes.isBeforeFirst){
                    queryRes.next()
                    reply("���Ѿ�����������"+queryRes.getInt("sign_in")+"���ˣ�ϣ�����ǵ��������һֱ��ȥ��")
                }
                else
                    reply("�����Բ��𡭡������鷳����һ���Լ��𣿰�����񲻼ǵ��㡣")
            }
        }

        (startsWith("����") and contains("��˿")){
            val queryRes = DBConn.query("select coin from customer where id = "+sender.id+";")
            if(queryRes!=null) {
                if (queryRes.isBeforeFirst) {
                    queryRes.next()
                    reply("�����ڵ���˿��" + queryRes.getString("coin") + "����ô���أ���Ҫ��Щʲô��")
                }
                    else
                    reply("�����ڻ�û����˿�ء�Ҫ����������Ŷ������")
            }
        }





    /*��������Ҫ���ݿ�Ĺ���*/
        (startsWith("����") and contains("���")){
            val m = Regex(""".*���.*?([0-9]+).*?([0-9]+).*""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val a = (m.groupValues[1].toInt()..m.groupValues[2].toInt()).random()
                    reply("������ɽ��Ϊ$a�����պá�")
                }
            }
        }

        (startsWith("����") and contains("������")){
            val a = DBConn.query("select * from blocklist;")
            var s = ""
            var found = false
            if(a!=null){
                if(a.isBeforeFirst){
                    while(a.next()){
                        s += a.getString("id")+";\n"
                        found = true
                    }
                }
            }
            if(!found)
                reply("̫���ˣ�ĿǰΪֹ��û�к�������!")
            else
                reply(s)
        }

        (startsWith("����") and (contains("ɬͼ") or contains("ɫͼ"))){
            val picUrl = "W:/Store/setu.jpg"
            reply(uploadImage(File(picUrl)))
        }

        (startsWith("����") and contains("��ͼ")){
            val picUrl = "W:/Store/huangtu.jpg"
            reply(uploadImage(File(picUrl)))
        }


        (startsWith("����") and contains("����")){
            val preurl = URL("https://acg.xydwz.cn/api/api.php")
            val preconn: URLConnection = preurl.openConnection()
            // ��ȡ����
            val iss = preconn.getInputStream()
            iss.sendAsImage()
        }

        (startsWith("����") and contains("����")){
            reply(ocGen())
        }


        /*
            (startsWith("����") and contains("Pվ")){
            val preurl = URL("https://acg.xydwz.cn/P"+ URLEncoder.encode("վ")+"/P"+URLEncoder.encode("վ���ͼƬ")+".php")
            val preconn: URLConnection = preurl.openConnection()
            // ��ȡ����
            val iss = preconn.getInputStream()
            iss.sendAsImage()
        }
        (startsWith("����") and contains("����")){

            val m = Regex(""".*?��(.+)��.*?""").find(message.contentToString())
            if (m != null) {
                if (m.groupValues.isNotEmpty()) {
                    val preurl = URL("https://app-api.pixiv.net/v1/search/illust?filter=for_android&word=" +m.groupValues[1]+ "&sort=date_desc&search_target=partial_match_for_tags")
                    val preconn: URLConnection = preurl.openConnection()
                    // ��ȡ����
                    val iss = preconn.getInputStream()
                    iss.sendAsImage()

                    val isr = InputStreamReader(iss)
                    val br = BufferedReader(isr)
                    var line: String? = null
                    while (br.readLine().also { line = it } != null) {
                        println(line)
                    }


                    /*val url = URL("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3618594892,1072052365&fm=26&gp=0.jpg")
                    val conn: URLConnection = url.openConnection()
                    // ��ȡ����
                    conn.getInputStream().sendAsImage()*/
                }
            }
        }
        */

        (startsWith("����") and contains("����˭")){
            reply("������С�ӿ�����Ⱥ�����ˣ�������С������Ľ��ϣ������̻�ɫ��β����к��ӡ�Ŀǰ�ҵĹ��ܻ����٣������һᾡ���ɳ��ġ�")
        }

        (startsWith("����") and contains("������˭")){
            reply("��������飨�����ߣ���С�ӣ�һ��ûʲô���µĳ���Ա��ҵ��ϲ��д�ĺ��ڴ��������޲�����")
        }

        (startsWith("����") and contains("�ְ���˭")){
            reply("����İְ�Ҳ��С�ӣ�һ��ûʲô���µĳ���Ա��������Ϊ������˭����ˤ��")
        }

        (startsWith("����") and contains("����")){
            reply("""���깦��˵���飨2020��12��14�գ�
                |0�����������й��ܶ���Ҫ�ھ��ӵĿ�ͷ�ٻ����ꡣ���˳��غ��Զ�������
                |1��������������ͨ������ʼ�������͡�ֹͣ�������л�����ĸ�����״̬��
                |2�������أ�����ͨ�������ӳ��ء��͡����ɳ��ء��л�����ĳ��ؼ���״̬��
                |3������Ⱥ��ӭ����ʱ�����޸ģ���Ϊ�һ��޷��ж���Ϣ���Ե�Ⱥ����������а���Ⱥ�Ļ�ӭ�ʶ����ˣ�
                |4�����������ֻҪ�ٻ����꣬˵����������������������֣��Ϳ�������������ˡ�
                |5����ǩ��������ͨ���ٻ����겢ǩ�������˿����˿���Թ����̵��ڵ���Ʒ��
                |6�����̵꣺Ĭ�ϵĻ�������ɡ�ͱ��ɡ�Ҳ�����ϼܺ��¼��Լ��Ļ��
                |7�����ϼܣ��ٻ����꣬˵�����ϼܡ�����Ҫ�ϼܵĻ�����д��˫�����Ȼ��д�ϼ۸�
                |8�����¼ܣ��ٻ����꣬˵�����¼ܡ�����Ҫ�¼ܵĻ�����д��˫�����
                |9������ͼ���ٻ����꣬˵���������������ɷ���һ���������ͼƬ��Ŀǰ��֧�ֹؼ���������
            """.trimMargin())
        }
    }
    miraiBot.subscribeAlways<MemberJoinEvent> {
        it.group.sendMessage(PlainText("��ӭ ${it.member.nameCardOrNick} �������Ⱥ���ܸ����������Ļ����ˣ��"))
    }
    miraiBot.subscribeAlways<MemberNudgedEvent> {
        it.group.sendMessage(PlainText("${it.from.nameCardOrNick}������������㣬Ҳ�����ˡ�"))
    }
    miraiBot.subscribeAlways<MessageRecallEvent.GroupRecall> {
        if(retractOn.get(0)==false)
            it.group.sendMessage(PlainText("���أ��������꿴���ˡ�"))
    }
    miraiBot.subscribeAlways<MemberJoinEvent>{
        DBConn.query("insert into quitlist values("+member.id+", now());")
    }
    miraiBot.subscribeAlways<MemberLeaveEvent>{
        val a = DBConn.query("select * from quitlist where id = "+member.id+";")
        if(a!=null){
            if(a.isBeforeFirst){
                a.next()
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val jointime: Date = simpleDateFormat.parse(a.getString("jointime"))
                if(Instant.now().toEpochMilli() - jointime.time < 1000*60*60*24)
                    DBConn.query("insert into blocklist values ("+member.id+");")
            }
        }
    }
    miraiBot.subscribeAlways<GroupMessageEvent>{
        if(message.content=="���꣬����"){
            reply(""+group.id)
        }
        if(message.content.startsWith("����") and message.content.contains("�ϼ�")){
            val m = Regex(""".*�ϼ�.*?��(.+)��.*?([0-9]+).*?""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val inShop = DBConn.query("select * from shop where name = \""+m.groupValues[1]+"\";")
                    if(inShop != null) {
                        if(inShop.isBeforeFirst)
                            reply("�������̵����Ѿ���"+m.groupValues[1]+"���ء�")
                        else{
                            DBConn.query("insert into shop (name, cost, chatgroup, friendliness) values (\""
                                    +m.groupValues[1]+"\", "+m.groupValues[2]+","
                                    +group.id+","+(1..20).random().toString()+");")
                            reply("�Ѿ��ϼܡ�"+m.groupValues[1]+"����ÿ�ݵļ۸���"+m.groupValues[2]+"��˿��")
                        }
                    }
                }
            }
        }

        if(message.content.startsWith("����") and message.content.contains("��")){
            val inShop = DBConn.query("select * from shop where chatgroup = 0 or chatgroup = "+group.id+";")
            if(inShop != null) {
                while(inShop.next()) {
                    if (message.contentToString().contains(inShop.getString("name"))) {
                        val cost = inShop.getInt("cost")
                        val coinNum = DBConn.query("select coin from customer where id = "+sender.id+";")
                        if(coinNum!=null) {
                            if (coinNum.isBeforeFirst) {
                                coinNum.next()
                                if(coinNum.getInt("coin")>cost) {                                    val senderInShop =
                                            DBConn.query("select * from stock where s_id =" + inShop.getInt("id")
                                                    +" and c_id ="+sender.id+ ";")
                                    if (senderInShop != null) {
                                        if (senderInShop.isBeforeFirst)
                                            DBConn.query("update stock set copies = copies + 1 where s_id ="
                                                    + inShop.getInt("id") +"and c_id ="+sender.id+ ";")
                                        else
                                            DBConn.query("insert into stock values ("+ inShop.getInt("id") +","
                                                    + sender.id + ", 1);")
                                        DBConn.query("update customer set coin = coin - $cost where id = " + sender.id + ";")
                                        reply(
                                                "�ܸ������������ҵ�" + inShop.getString("name") + "�������ڻ���" + (coinNum.getInt("coin") - cost) + "����˿������" +
                                                        (inShop.getString("comment")?:"��ӭ������������ķŻ��")
                                        )
                                    }
                                }
                                else
                                    reply("�ܸ������������ҵ�" + inShop.getString("name") + "���������������ڻ�û���㹻����˿�أ�")
                            }
                        }
                    }
                }
            }
        }

        if(message.content.startsWith("����") and message.content.contains("�ֿ�")){
            var replys = "�ţ����ҿ���"+senderName+"�ֿ�����ʲô����\n"
            var anything = false;
            val inShop = DBConn.query("select * from shop;")
            if(inShop != null) {
                while(inShop.next()) {
                    val tableI = DBConn.query("select * from stock where c_id = "+sender.id
                            +" and s_id ="+inShop.getString("id")+";")
                    if(tableI!=null) {
                        if (tableI.isBeforeFirst) {
                            tableI.next()
                            replys += inShop.getString("name") + "*"+tableI.getString("copies")+"\n"
                            anything = true
                        }
                    }
                }
            }
            if(!anything)
                replys += "�����ֿ������ʲô��û�е����ӡ���\n"
            reply(replys)
        }

        if(message.content.startsWith("����") and message.content.contains("�̵�")){
            var replys = "�ܸ��������ҵ��̵꣬�������ҽ���һ�°ɡ���\n"
            val inShop = DBConn.query("select * from shop where chatgroup ="+group.id+" or chatgroup = 0;")
            if(inShop != null) {
                while(inShop.next()) {
                    replys+="��"+inShop.getString("name")+"����ÿ��"+inShop.getString("cost")+"��˿��\n"
                }
            }
            reply(replys)
        }

        if(message.content.startsWith("����") and message.content.contains("�¼�")){
            val m = Regex(""".*�¼�.*?��(.+)��.*?""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val inShop = DBConn.query("select * from shop where name = \""+m.groupValues[1]+"\" and (chatgroup = "+group.id+" or chatgroup = 0);")
                    if(inShop != null) {
                        if(inShop.isBeforeFirst) {
                            DBConn.query("delete from shop where name = \""+m.groupValues[1]+"\";")
                            val newId = DBConn.query("select max(id) from shop;")
                            if(newId != null){
                                newId.next()
                                DBConn.query( "delete from stock where name = \""+m.groupValues[1]+"\";")
                                reply("�Ѿ��¼ܡ�"+m.groupValues[1]+"����ϣ���������������Ҳ��졣")
                            }
                        }
                        else{
                            reply("�������̵��ﻹû��"+m.groupValues[1]+"Ŷ��")
                        }
                    }
                }
            }
        }
    }

    miraiBot.join() // �ȴ� Bot ����, �������߳��˳�
}