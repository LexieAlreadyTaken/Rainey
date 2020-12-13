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


suspend fun main() {
    val qqId = 2028829835L//Bot的QQ号，需为Long类型，在结尾处添加大写L
    val password = "ArcosDorados1234"//Bot的密码
    val raineyCoin = mutableMapOf(2028829835L to 0);
    val raineyUmbrella = mutableMapOf(2028829835L to 0);
    val raineyBiscuit = mutableMapOf(2028829835L to 0);
    val definedShop = arrayOfNulls<MutableMap<Long,Int>>(0)
    val configuration = BotConfiguration()
    configuration.protocol = MiraiProtocol.ANDROID_PAD
    configuration.fileBasedDeviceInfo("deviceInfo.json")
    val miraiBot = Bot(qqId, password, configuration).alsoLogin()//新建Bot并登录
    miraiBot.subscribeMessages {
        "你好" reply "你好。"
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

        (startsWith("阿雨") and contains("签到")){
            val a = (1..20).random()
            reply("谢谢你来看我。这里的"+a+"个雨丝你可以拿走了……")
            if(sender.id in raineyCoin)
                raineyCoin[sender.id] = raineyCoin[sender.id]!!.plus(a)
            else
                raineyCoin[sender.id] = a
        }

        (startsWith("阿雨") and contains("雨丝")){
            if(sender.id in raineyCoin)
                reply("你现在的雨丝有"+raineyCoin[sender.id]+"个这么多呢！需要换些什么吗？")
            else
                reply("你现在还没有雨丝呢。要多来看看我哦……？")
        }

        (startsWith("阿雨")and contains("买")and contains("伞")){
            if(sender.id in raineyCoin && (raineyCoin[sender.id]!! >=10)){
                raineyCoin[sender.id] = raineyCoin[sender.id]!!.minus(10)
                if(sender.id in raineyUmbrella)
                    raineyUmbrella[sender.id] = raineyCoin[sender.id]!!.plus(1)
                else
                    raineyUmbrella[sender.id] = 1
                reply("很高兴来这里买我的雨伞。你现在还有"+raineyCoin[sender.id]+"个雨丝。……我虽然叫阿雨，但打伞也不会拦住我的哦？")
            }
            else
                reply("很高兴来这里买我的雨伞。不过……你现在还没有足够的雨丝呢？")
        }


        (startsWith("阿雨")and contains("买")and contains("饼干")){
            if(sender.id in raineyCoin && (raineyCoin[sender.id]!! >=15)){
                raineyCoin[sender.id] = raineyCoin[sender.id]!!.minus(15)
                if(sender.id in raineyBiscuit)
                    raineyBiscuit[sender.id] = raineyCoin[sender.id]!!.plus(1)
                else
                    raineyBiscuit[sender.id] = 1
                reply("很高兴来这里买我的饼干。你现在还有"+raineyCoin[sender.id]+"个雨丝。……这些是我亲手做的，虽然说我更擅长做汤面，但饼干似乎是可爱些。")
            }
            else
                reply("很高兴来这里买我的饼干。不过……你现在还没有足够的雨丝呢？")
        }

        (startsWith("阿雨") and contains("仓库")){
            var replys = "嗯，让我看看"+senderName+"仓库里有什么……\n"
            var anything = false;
            if(sender.id in raineyUmbrella){
                replys += ""+raineyUmbrella[sender.id]+"把雨伞\n"
                anything = true
            }
            if(sender.id in raineyBiscuit){
                replys += ""+raineyBiscuit[sender.id]+"盒饼干\n"
                anything = true
            }
            if(!anything)
                replys += "啊，仓库里好像什么都没有的样子……\n"
            reply(replys)
        }

        (startsWith("阿雨") and contains("随机")){
            val m = Regex(""".*随机.*([0-9]+).*([0-9]+).*""").find(message.contentToString())
            if (m != null) {
                if(m.groupValues.isNotEmpty()){
                    for(i in m.groupValues){
                        reply(i)
                    }
                }
            }
        }

        (contains("阿雨") and contains("谁")){
            reply("阿雨是小河开发的群机器人，是来自小河宇宙的江南，有着烟灰色马尾辫的男孩子。目前我的功能还很少，不过我会尽量成长的。")
        }
    }
    miraiBot.subscribeAlways<MemberJoinEvent> {
        it.group.sendMessage(PlainText("欢迎 ${it.member.nameCardOrNick} 来到这个群。很高兴能与更多的伙伴玩耍。"))
    }
    miraiBot.subscribeAlways<MemberNudgedEvent> {
        it.group.sendMessage(PlainText("${it.from.nameCardOrNick}？……如果是你，也就算了。"))
    }
    miraiBot.subscribeAlways<MessageRecallEvent.GroupRecall> {
        it.group.sendMessage(PlainText("撤回？……阿雨看见了。"))
    }

    miraiBot.join() // 等待 Bot 离线, 避免主线程退出
}