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
    val qqId = 2028829835L//Bot��QQ�ţ���ΪLong���ͣ��ڽ�β����Ӵ�дL
    val password = "ArcosDorados1234"//Bot������
    val raineyCoin = mutableMapOf(2028829835L to 0);
    val raineyUmbrella = mutableMapOf(2028829835L to 0);
    val raineyBiscuit = mutableMapOf(2028829835L to 0);
    var definedShop = emptyArray<MutableMap<Long,Int>>()
    var shopName = emptyArray<String>()
    var shopCost = emptyArray<Int>()
    val configuration = BotConfiguration()
    var lastMessage = ""
    var thisMessage: String
    var fudued = false
    configuration.protocol = MiraiProtocol.ANDROID_PAD
    configuration.fileBasedDeviceInfo("deviceInfo.json")
    val miraiBot = Bot(qqId, password, configuration).alsoLogin()//�½�Bot����¼
    miraiBot.subscribeMessages {
        "���" reply "��á�"

        (contains("")){
            thisMessage = message.contentToString()
            if(thisMessage == lastMessage && !fudued) {
                reply(thisMessage)
                fudued = true
                Timer().schedule(object:TimerTask(){
                    override fun run() {
                        fudued=false
                    }
                }, Date(), 10000)
                Timer().schedule(object:TimerTask(){
                    override fun run() {
                        println(fudued)
                    }
                }, Date(), 1000)
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
            val a = (1..20).random()
            reply("лл�������ҡ������"+a+"����˿����������ˡ���")
            if(sender.id in raineyCoin)
                raineyCoin[sender.id] = raineyCoin[sender.id]!!.plus(a)
            else
                raineyCoin[sender.id] = a
        }

        (startsWith("����") and contains("��˿")){
            if(sender.id in raineyCoin)
                reply("�����ڵ���˿��"+raineyCoin[sender.id]+"����ô���أ���Ҫ��Щʲô��")
            else
                reply("�����ڻ�û����˿�ء�Ҫ����������Ŷ������")
        }

        (startsWith("����")and contains("��")and contains("ɡ")){
            if(sender.id in raineyCoin && (raineyCoin[sender.id]!! >=10)){
                raineyCoin[sender.id] = raineyCoin[sender.id]!!.minus(10)
                if(sender.id in raineyUmbrella)
                    raineyUmbrella[sender.id] = raineyCoin[sender.id]!!.plus(1)
                else
                    raineyUmbrella[sender.id] = 1
                reply("�ܸ������������ҵ���ɡ�������ڻ���"+raineyCoin[sender.id]+"����˿����������Ȼ�а��꣬����ɡҲ������ס�ҵ�Ŷ��")
            }
            else
                reply("�ܸ������������ҵ���ɡ���������������ڻ�û���㹻����˿�أ�")
        }

        (startsWith("����")and contains("��")and contains("����")){
            if(sender.id in raineyCoin && (raineyCoin[sender.id]!! >=15)){
                raineyCoin[sender.id] = raineyCoin[sender.id]!!.minus(15)
                if(sender.id in raineyBiscuit)
                    raineyBiscuit[sender.id] = raineyCoin[sender.id]!!.plus(1)
                else
                    raineyBiscuit[sender.id] = 1
                reply("�ܸ������������ҵı��ɡ������ڻ���"+raineyCoin[sender.id]+"����˿��������Щ�����������ģ���Ȼ˵�Ҹ��ó������棬�������ƺ��ǿɰ�Щ��")
            }
            else
                reply("�ܸ������������ҵı��ɡ��������������ڻ�û���㹻����˿�أ�")
        }

        (startsWith("����") and contains("�ֿ�")){
            var replys = "�ţ����ҿ���"+senderName+"�ֿ�����ʲô����\n"
            var anything = false;
            if(sender.id in raineyUmbrella){
                replys += ""+raineyUmbrella[sender.id]+"����ɡ\n"
                anything = true
            }
            if(sender.id in raineyBiscuit){
                replys += ""+raineyBiscuit[sender.id]+"�б���\n"
                anything = true
            }
            if(!anything)
                replys += "�����ֿ������ʲô��û�е����ӡ���\n"
            reply(replys)
        }

        (startsWith("����") and contains("���")){
            val m = Regex(""".*���.*?([0-9]+).*?([0-9]+).*""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val a = (m.groupValues[1].toInt()..m.groupValues[2].toInt()).random()
                    reply("������ɽ��Ϊ$a�����պá�")
                }
            }
        }

        (startsWith("����") and contains("�ϼ�")){
            val m = Regex(""".*�ϼ�.*?��(.+)��.*?([0-9]+).*?""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    shopName = shopName.plus(m.groupValues[1])
                    definedShop = definedShop.plus(mutableMapOf())
                    shopCost = shopCost.plus(m.groupValues[2].toInt())
                    reply(shopName.last()+shopCost.last().toString())
                }
            }
        }

        for(i in shopName.indices){
            (startsWith("����") and contains("��") and contains(shopName[i])){
                if(sender.id in raineyCoin && (raineyCoin[sender.id]!! >= shopCost[i])){
                    raineyCoin[sender.id] = raineyCoin[sender.id]!!.minus(shopCost[i])
                    if(sender.id in definedShop[i])
                        definedShop[i][sender.id] = definedShop[i][sender.id]!!.plus(1)
                    else
                        definedShop[i][sender.id] = 1
                    reply("�ܸ������������ҵ�"+shopName[i]+"�������ڻ���"+raineyCoin[sender.id]+"����˿��������ӭ������������ķ���Ʒ��")
                }
                else
                    reply("�ܸ������������ҵ�"+shopName[i]+"���������������ڻ�û���㹻����˿�أ�")
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