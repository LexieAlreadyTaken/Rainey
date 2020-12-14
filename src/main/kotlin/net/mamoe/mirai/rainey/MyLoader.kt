package net.mamoe.mirai.rainey

import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.MemberNudgedEvent
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol
import java.util.*


suspend fun main() {
    val qqId = 3208445592L//Bot��QQ�ţ���ΪLong���ͣ��ڽ�β����Ӵ�дL
    val password = "ArcosDorados1234"//Bot������
    val configuration = BotConfiguration()
    var lastMessage = ""
    var thisMessage: String
    var fudued = arrayOfNulls<Boolean>(1)
    configuration.protocol = MiraiProtocol.ANDROID_PAD
    configuration.fileBasedDeviceInfo("deviceInfo.json")

    val miraiBot = Bot(qqId, password, configuration).alsoLogin()//�½�Bot����¼
    DBConn.getConnection();
    miraiBot.subscribeMessages {
        "���" reply "��á�"

        (startsWith("����") and contains("��ʼ����")){
            fudued.set(0,false)
        }

        (startsWith("����") and contains("ֹͣ����")){
            fudued.set(0,true)
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

        (startsWith("����") and contains("ǩ��")){
            val randNum = (1..20).random()
            reply("лл�������ҡ������"+randNum+"����˿����������ˡ���")
            val queryRes = DBConn.query("select coin from customer where id = "+sender.id+";")
            if(queryRes!=null) {
                if (queryRes.isBeforeFirst)
                    DBConn.query("update customer set coin = coin+ $randNum where id = " + sender.id + ";")
                else
                    DBConn.query("insert into customer values ("+sender.id+", "+randNum+");")
            }
        }

        (startsWith("����") and contains("��˿")){
            val queryRes = DBConn.query("select coin from customer where id = "+sender.id+";")
            if(queryRes!=null) {
                if (queryRes.isBeforeFirst)
                    reply("�����ڵ���˿��"+queryRes.getString("coin")+"����ô���أ���Ҫ��Щʲô��")
                else
                    reply("�����ڻ�û����˿�ء�Ҫ����������Ŷ������")
            }
        }

        (startsWith("����") and contains("��")){
            val inShop = DBConn.query("select * from shop;")
            if(inShop != null) {
                while(inShop.next()) {
                    if (message.contentToString().contains(inShop.getString("name"))) {
                        val cost = inShop.getInt("cost")
                        val coinNum = DBConn.query("select coin from customer where id = "+sender.id+";")
                        if(coinNum!=null) {
                            if (coinNum.isBeforeFirst && coinNum.getInt("coin")>inShop.getInt("cost")) {
                                DBConn.query("update customer set coin = coin - $cost where id = " + sender.id + ";")
                                val senderInShop = DBConn.query("select * from stock_"+inShop.getInt("id")+";")
                                if(senderInShop != null){
                                    if(senderInShop.isBeforeFirst)
                                        DBConn.query("update stock_"+inShop.getInt("id")+" set copies = copies + 1;")
                                    else
                                        DBConn.query("insert into stock_"+inShop.getInt("id")+" values ("+sender.id+", 1);")
                                }
                                reply("�ܸ������������ҵ�" + inShop.getString("name") + "�������ڻ���" + (coinNum.getInt("coin")-cost) + "����˿������"+inShop.getString("comment"))
                            } else
                                reply("�ܸ������������ҵ�" + inShop.getString("name") + "���������������ڻ�û���㹻����˿�أ�")
                        }
                    }
                }
            }
        }


        (startsWith("����") and contains("�ֿ�")){
            var replys = "�ţ����ҿ���"+senderName+"�ֿ�����ʲô����\n"
            var anything = false;
            val inShop = DBConn.query("select * from shop;")
            if(inShop != null) {
                while(inShop.next()) {
                    val tableI = DBConn.query("select * from stock_"+inShop.getInt("id")+" where costumer_id = "+sender.id+";")
                    if(tableI!=null) {
                        if (tableI.isBeforeFirst) {
                            replys += inShop.getString("name") + "*"+tableI.getString("copies"+"��\n")
                            anything = true
                        }
                    }
                }
            }
            if(!anything)
                replys += "�����ֿ������ʲô��û�е����ӡ���\n"
            reply(replys)
        }


        (startsWith("����") and contains("�ϼ�")){
            val m = Regex(""".*�ϼ�.*?��(.+)��.*?([0-9]+).*?""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val inShop = DBConn.query("select * from shop where name = "+m.groupValues[1]+";")
                    if(inShop != null) {
                        if(inShop.isBeforeFirst)
                            reply("�������̵����Ѿ���"+m.groupValues[1]+"���ء�")
                        else{
                            DBConn.query("insert into shop (name, cost) values (\""+m.groupValues[1]+"\", "+m.groupValues[2]+", );")
                            //���ǲ�֪����ô��ȡchatgroup��
                            val newId = DBConn.query("select max(id) from shop;")
                            if(newId != null){
                                DBConn.query( "create table stock_"+newId.getInt("max(id)")+" (-> customer_id integer primary key, copies integer, chatgroup integer);")
                            }
                        }
                    }
                }
            }
        }

    /*����Ҫ���ݿ�Ĺ���*/
        (startsWith("����") and contains("���")){
            val m = Regex(""".*���.*?([0-9]+).*?([0-9]+).*""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val a = (m.groupValues[1].toInt()..m.groupValues[2].toInt()).random()
                    reply("������ɽ��Ϊ$a�����պá�")
                }
            }
        }

        (contains("����") and contains("˭")){
            reply("������С�ӿ�����Ⱥ�����ˣ�������С������Ľ��ϣ������̻�ɫ��β����к��ӡ�Ŀǰ�ҵĹ��ܻ����٣������һᾡ���ɳ��ġ�")
        }
    }
    miraiBot.subscribeAlways<MemberJoinEvent> {
        it.group.sendMessage(PlainText("��ӭ ${it.member.nameCardOrNick} �������Ⱥ���ܸ����������Ļ����ˣ��"))
    }
    miraiBot.subscribeAlways<MemberNudgedEvent> {
        it.group.sendMessage(PlainText("${it.from.nameCardOrNick}������������㣬Ҳ�����ˡ�"))
    }
    miraiBot.subscribeAlways<MessageRecallEvent.GroupRecall> {
        it.group.sendMessage(PlainText("���أ��������꿴���ˡ�"))
    }

    miraiBot.join() // �ȴ� Bot ����, �������߳��˳�
}