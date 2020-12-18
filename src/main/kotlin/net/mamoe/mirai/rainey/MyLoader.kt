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
    val qqId = 2028829835L//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = "ArcosDorados1234"//Bot的密码
    val configuration = BotConfiguration()
    var lastMessage = ""
    var thisMessage: String
    val fudued = arrayOfNulls<Boolean>(1)
    val retractOn = arrayOfNulls<Boolean>(1)
    //var welcomeMessage : String
    configuration.protocol = MiraiProtocol.ANDROID_PAD
    configuration.fileBasedDeviceInfo("deviceInfo.json")

    val miraiBot = Bot(qqId, password, configuration).alsoLogin()//新建Bot并登录
    DBConn.getConnection();
    miraiBot.subscribeMessages {
        /*复读之类的娱乐性操作*/
        "你好" reply "你好。"

        (case("on")){
            fudued.set(0,false)
            retractOn.set(0,false)
        }

        (startsWith("阿雨") and contains("开始复读")){
            fudued.set(0,false)
        }

        (startsWith("阿雨") and contains("停止复读")){
            fudued.set(0,true)
        }

        (startsWith("阿雨") and contains("监视撤回")){
            retractOn.set(0,false)
        }

        (startsWith("阿雨") and contains("自由撤回")){
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




        /*“人性化”的操作*/
        atBot(){
            reply("来而不往非礼也，所以……")
            reply(At(sender as Member)+"（轻笑）")
        }

        (matching(Regex("阿雨.?"))){
            reply("是我。怎么了……")
        }

        (startsWith("阿雨") and contains("抱")){
            reply("（迟疑了一下，然后给了你一个小心翼翼的拥抱）这样……可以了吗？")
        }
        (startsWith("阿雨") and contains("亲")){
            reply("（似乎不可思议地看着你）妈妈说……这样的事……只能和喜欢的人做哦？")
        }
        (startsWith("阿雨") and contains("喜欢")){
            reply("喜欢我吗？……我以为我的性格太冷清了……不过，也许这样，也不错。")
        }
        (startsWith("阿雨") and contains("胖次")){
            reply("（脸迅速地飞红，转过身去）啊啊啊啊啊！变态啊！")
        }




        /*商店有关的操作*/
        (startsWith("阿雨") and contains("签到")){
            reply("阿雨的签到功能已经被麻麻改成了更加人性化的问候功能。只需要召唤阿雨并说出”你好“就可以对阿雨进行问候。")
        }

        (startsWith("阿雨") and contains("你好")){
            val queryRes = DBConn.query("select * from customer where id = "+sender.id+";")
            if(queryRes!=null) {
                val randNum = (1..20).random()
                if (queryRes.isBeforeFirst) {
                    queryRes.next()
                    if(!queryRes.getBoolean("signed_in")) {
                        DBConn.query("update customer set coin = coin+ $randNum,sign_in = sign_in + 1, signed_in = true where id = " + sender.id + ";")
                        when(queryRes.getInt("sign_in")){
                            0 -> reply("谢谢你来看我。这里的" + randNum + "个雨丝你可以拿走了……")
                            in 1..3 -> reply("谢谢你今天也来看我。这里的" + randNum + "个雨丝你可以拿走了……")
                            in 4..7 -> reply("很高兴今天也能见到你。这里的" + randNum + "个雨丝你可以拿走了……")
                            else -> reply(sender.nameCardOrNick+"，还说什么？拿你的"+randNum+"雨丝就是了！（少年气的笑）")
                        }
                    }
                    else
                        reply("谢谢你来看我。很高兴一天之中能再次见到你。")
                }
                else {
                    reply("生面孔？（赶紧掩饰自己的羞涩）……非常高兴能交到更多的朋友。这里的" + randNum + "个雨丝你可以拿走了……")
                    DBConn.query("insert into customer values (" + sender.id + ", " + randNum + ",1,true);")
                }
            }
        }

        (startsWith("阿雨") and contains("连续")){
            val queryRes = DBConn.query("select * from customer where id = "+sender.id+";")
            if(queryRes!=null){
                if(queryRes.isBeforeFirst){
                    queryRes.next()
                    reply("你已经连续来看我"+queryRes.getInt("sign_in")+"天了！希望我们的友谊可以一直下去。")
                }
                else
                    reply("啊，对不起……可以麻烦介绍一下自己吗？阿雨好像不记得你。")
            }
        }

        (startsWith("阿雨") and contains("雨丝")){
            val queryRes = DBConn.query("select coin from customer where id = "+sender.id+";")
            if(queryRes!=null) {
                if (queryRes.isBeforeFirst) {
                    queryRes.next()
                    reply("你现在的雨丝有" + queryRes.getString("coin") + "个这么多呢！需要换些什么吗？")
                }
                    else
                    reply("你现在还没有雨丝呢。要多来看看我哦……？")
            }
        }





    /*其他不需要数据库的功能*/
        (startsWith("阿雨") and contains("随机")){
            val m = Regex(""".*随机.*?([0-9]+).*?([0-9]+).*""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val a = (m.groupValues[1].toInt()..m.groupValues[2].toInt()).random()
                    reply("随机生成结果为$a，请收好。")
                }
            }
        }

        (startsWith("阿雨") and contains("黑名单")){
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
                reply("太好了，目前为止还没有黑名单呢!")
            else
                reply(s)
        }

        (startsWith("阿雨") and (contains("涩图") or contains("色图"))){
            val picUrl = "W:/Store/setu.jpg"
            reply(uploadImage(File(picUrl)))
        }

        (startsWith("阿雨") and contains("黄图")){
            val picUrl = "W:/Store/huangtu.jpg"
            reply(uploadImage(File(picUrl)))
        }


        (startsWith("阿雨") and contains("动漫")){
            val preurl = URL("https://acg.xydwz.cn/api/api.php")
            val preconn: URLConnection = preurl.openConnection()
            // 读取内容
            val iss = preconn.getInputStream()
            iss.sendAsImage()
        }

        (startsWith("阿雨") and contains("生成")){
            reply(ocGen())
        }


        /*
            (startsWith("阿雨") and contains("P站")){
            val preurl = URL("https://acg.xydwz.cn/P"+ URLEncoder.encode("站")+"/P"+URLEncoder.encode("站随机图片")+".php")
            val preconn: URLConnection = preurl.openConnection()
            // 读取内容
            val iss = preconn.getInputStream()
            iss.sendAsImage()
        }
        (startsWith("阿雨") and contains("测试")){

            val m = Regex(""".*?“(.+)”.*?""").find(message.contentToString())
            if (m != null) {
                if (m.groupValues.isNotEmpty()) {
                    val preurl = URL("https://app-api.pixiv.net/v1/search/illust?filter=for_android&word=" +m.groupValues[1]+ "&sort=date_desc&search_target=partial_match_for_tags")
                    val preconn: URLConnection = preurl.openConnection()
                    // 读取内容
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
                    // 读取内容
                    conn.getInputStream().sendAsImage()*/
                }
            }
        }
        */

        (startsWith("阿雨") and contains("你是谁")){
            reply("阿雨是小河开发的群机器人，是来自小河宇宙的江南，有着烟灰色马尾辫的男孩子。目前我的功能还很少，不过我会尽量成长的。")
        }

        (startsWith("阿雨") and contains("妈妈是谁")){
            reply("阿雨的麻麻（创造者）是小河，一个没什么本事的程序员，业余喜欢写文和在代码上修修补补。")
        }

        (startsWith("阿雨") and contains("爸爸是谁")){
            reply("阿雨的爸爸也是小河，一个没什么本事的程序员……你以为还能是谁啊（摔）")
        }

        (startsWith("阿雨") and contains("帮助")){
            reply("""阿雨功能说明书（2020年12月14日）
                |0——几乎所有功能都需要在句子的开头召唤阿雨。除了撤回和自动复读。
                |1——复读：可以通过“开始复读”和“停止复读”切换阿雨的复读机状态。
                |2——撤回：可以通过“监视撤回”和“自由撤回”切换阿雨的撤回监视状态。
                |3——进群欢迎：暂时不能修改（因为我还无法判断消息来自的群，会把所有有阿雨群的欢迎词都改了）
                |4——随机数：只要召唤阿雨，说出“随机”并带上两个数字，就可以生成随机数了。
                |5——签到：可以通过召唤阿雨并签到获得雨丝。雨丝可以购买商店内的物品。
                |6——商店：默认的货物有雨伞和饼干。也可以上架和下架自己的货物。
                |7——上架：召唤阿雨，说出“上架”，把要上架的货物名写在双引号里，然后写上价格。
                |8——下架：召唤阿雨，说出“下架”，把要下架的货物名写在双引号里。
                |9——搜图：召唤阿雨，说出“动漫”，即可返回一张随机动漫图片。目前不支持关键词搜索。
            """.trimMargin())
        }
    }
    miraiBot.subscribeAlways<MemberJoinEvent> {
        it.group.sendMessage(PlainText("欢迎 ${it.member.nameCardOrNick} 来到这个群。很高兴能与更多的伙伴玩耍。"))
    }
    miraiBot.subscribeAlways<MemberNudgedEvent> {
        it.group.sendMessage(PlainText("${it.from.nameCardOrNick}？……如果是你，也就算了。"))
    }
    miraiBot.subscribeAlways<MessageRecallEvent.GroupRecall> {
        if(retractOn.get(0)==false)
            it.group.sendMessage(PlainText("撤回？……阿雨看见了。"))
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
        if(message.content=="阿雨，哪里"){
            reply(""+group.id)
        }
        if(message.content.startsWith("阿雨") and message.content.contains("上架")){
            val m = Regex(""".*上架.*?“(.+)”.*?([0-9]+).*?""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    val inShop = DBConn.query("select * from shop where name = \""+m.groupValues[1]+"\";")
                    if(inShop != null) {
                        if(inShop.isBeforeFirst)
                            reply("看样子商店里已经有"+m.groupValues[1]+"了呢。")
                        else{
                            DBConn.query("insert into shop (name, cost, chatgroup, friendliness) values (\""
                                    +m.groupValues[1]+"\", "+m.groupValues[2]+","
                                    +group.id+","+(1..20).random().toString()+");")
                            reply("已经上架“"+m.groupValues[1]+"”，每份的价格是"+m.groupValues[2]+"雨丝。")
                        }
                    }
                }
            }
        }

        if(message.content.startsWith("阿雨") and message.content.contains("买")){
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
                                                    + sender.id + ", 1,0);")
                                        DBConn.query("update customer set coin = coin - $cost where id = " + sender.id + ";")
                                        reply(
                                                "很高兴来这里买我的" + inShop.getString("name") + "。你现在还有" + (coinNum.getInt("coin") - cost) + "个雨丝。……" +
                                                        (inShop.getString("comment")?:"欢迎你们在我这里寄放货物。")
                                        )
                                    }
                                }
                                else
                                    reply("很高兴来这里买我的" + inShop.getString("name") + "。不过……你现在还没有足够的雨丝呢？")
                            }
                        }
                    }
                }
            }
        }

        if(message.content.startsWith("阿雨") and message.content.contains("仓库")){
            var replys = "嗯，让我看看"+senderName+"仓库里有什么……\n"
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
                replys += "啊，仓库里好像什么都没有的样子……\n"
            reply(replys)
        }

        if(message.content.startsWith("阿雨") and message.content.contains("商店")){
            var replys = "很高兴来到我的商店，现在让我介绍一下吧……\n"
            val inShop = DBConn.query("select * from shop where chatgroup ="+group.id+" or chatgroup = 0;")
            if(inShop != null) {
                while(inShop.next()) {
                    replys+="【"+inShop.getString("name")+"】，每份"+inShop.getString("cost")+"雨丝；\n"
                }
            }
            reply(replys)
        }

        if(message.content.startsWith("阿雨") and message.content.contains("下架")){
            val m = Regex(""".*下架.*?“(.+)”.*?""").find(message.contentToString())
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
                                reply("已经下架“"+m.groupValues[1]+"”，希望你接下来的生活也愉快。")
                            }
                        }
                        else{
                            reply("看起来商店里还没有"+m.groupValues[1]+"哦？")
                        }
                    }
                }
            }
        }
    }

    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}