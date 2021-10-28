package com.kcrason.highperformancefriendscircle;

/**
 * @author KCrason
 * @date 2018/4/27
 */
public class Constants {

    public static final String EMOJI_REGEX = "\\[([\u4e00-\u9fa5\\w])+\\]|[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]";

    public static final String URL_REGEX = "(((http|https)://)|((?<!((http|https)://))www\\.))" + ".*?" + "(?=(&nbsp;|[\\u4e00-\\u9fa5]|\\s|　|<br />|$|[<>]))";

    public static final String TOPIC_REGEX = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";

    public static final String AT_REGEX = "@[\u4e00-\u9fa5a-zA-Z0-9_-·\\.]+[\u200B]";

    public static final String SCHEME_URL = "com.kcrason.url//";

    public static final String SCHEME_EMOJI = "com.kcrason.emoji//";

    public static final String SCHEME_TOPIC = "com.kcrason.topic//";

    public static final String SCHEME_AT = "com.kcrason.at//";

    public static final String BLUE = "#ff0000";

    public static String[] IMAGE_URL = new String[]{
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3319438021,2309257658&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3302399998,3216746631&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=111713540,615806613&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=341802306,419856862&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1652123795,1945063222&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1160141523,2362541442&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=136615058,611164102&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2346282507,2171850944&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3452531538,3399298632&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1588620919,359805583&fm=26&gp=0.jpg",


            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2759603483,1319363293&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3523376988,1285761647&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3489150134,2737835973&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=139718553,3413558641&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3200321403,3342661494&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3586876696,2005477737&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2234238213,2776120128&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3014684922,2880800651&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1434909179,2506552154&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3231371241,3462145585&fm=26&gp=0.jpg",


            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3013778081,2300854438&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3791123780,4261799713&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3413581055,4053282087&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3093977134,1475813689&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1965212879,2703864701&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2879430259,1145403574&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2816433753,267880517&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2692052638,3584599901&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=364914450,3538613711&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3313838802,2768404782&fm=26&gp=0.jpg",


            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2699437196,2241898528&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1722449517,1620238619&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=209946149,367802997&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1609525934,2067257331&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=365658483,3790065949&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2699437196,2241898528&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2357090840,684880588&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3156590071,3197389731&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1040628782,1354239620&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3326585678,2556835623&fm=26&gp=0.jpg",

            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=346684713,1057090887&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3313045614,1054782385&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3139328511,2926479650&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3449545451,2012692101&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3540352377,2869642238&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2107642534,1339643071&fm=26&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1361654897,3878533814&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3018646101,722836054&fm=11&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2199434891,1210172381&fm=26&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1482059913,39843095&fm=11&gp=0.jpg"
    };

    public final static String[] USER_NAME = {
            "光速", "出单队", "精英队", "精英", "风启",
            "野狼盟", "篮球人", "秃鸡队", "奔驰财团", "永创第一",
            "胜羽队", "巨人队", "同路人", "18新秀", "奇迹",
            "无敌队", "给力", "天一", "飞虎队", "超越队",
            "NIKE队", "飞跃", "农夫山泉队", "兄弟盟", "完美之神",
            "百事队", "风之彩", "美特斯邦威", "我们", "跃起动力",
    };

    public final static String[] TIMES = {
            "1分钟前", "5分钟前", "10分钟前", "20分钟前", "1小时前",
            "2小时前", "3小时前", "4小时前", "4小时前", "5小时前",
            "6小时前", "7小时前", "8小时前", "9小时前", "10小时前",
            "11小时前", "一天前", "两天前", "三天前", "五天前",
            "十天前", "半个月前", "一个月前", "两个月前", "五个月前",
            "8个月前", "一年前", "二年前", "五年前", "10年前",
    };

    public final static String[] SOURCE = {
            "微信", "今日头条", "QQ", "京东", "百度",
            "阿里巴巴", "微博", "淘宝", "抖音", "内涵段子",
            "快手", "优酷", "爱奇艺", "天猫", "腾讯视频",
            "微视", "大智慧", "同花顺", "第五人格", "陌陌",
            "探探", "QQ斗地主", "大吉大利，今晚吃鸡", "Wifi万能钥匙", "QQ音乐",
            "网易云音乐", "酷狗音乐", "墨迹天气", "百度地图", "天天快报",
    };

    public final static String[] CONTENT = {
            "土地是以它的肥沃和收获而被估价的；才能也是土地，不过它生产的不是粮食，而是真理。如果只能滋生瞑想和幻想的话，即使再大的才能也只是砂地或盐池，那上面连小草也长不出来的。",
            "我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。",
            "人生的磨难是很多的，所以我们不可对于每一件轻微的伤害都过于敏感。在生活磨难面前，精神上的坚强和无动于衷是我们抵抗罪恶和人生意外的最好武器",
            "爱情只有当它是自由自在时，才会叶茂花繁。认为爱情是某种义务的思想只能置爱情于死地。只消一句话：你应当爱某个人，就足以使你对这个人恨之入骨。",
            "温顺的青年人在图书馆里长大，他们相信他们的责任是应当接受西塞罗，洛克，培根发表的意见；他们忘了西塞罗，洛克与培根写这些书的时候，也不过是在图书馆里的青年人。",
            "较高级复杂的劳动，是这样一种劳动力的表现，这种劳动力比较普通的劳动力需要较高的教育费用，它的生产需要花费较多的劳动时间。因此，具有较高的价值。",
            "父亲子女兄弟姊妹等称谓，并不是简单的荣誉称号，而是一种负有完全确定的异常郑重的相互义务的称呼，这些义务的总和便构成这些民族的社会制度的实质部分。",
            "世界上没有才能的人是没有的。问题在于教育者要去发现每一位学生的禀赋、兴趣、爱好和特长，为他们的表现和发展提供充分的条件和正确引导。",
            "在人类历史的长河中，真理因为像黄金一样重，总是沉于河底而很难被人发现，相反地，那些牛粪一样轻的谬误倒漂浮在上面到处泛滥。",
            "要永远觉得祖国的土地是稳固地在你脚下，要与集体一起生活，要记住，是集体教育了你。那一天你若和集体脱离，那便是末路的开始。"
    };

    public final static String[] COMMENT_CONTENT = {
            "在强者的眼中，没有最好，只有更好。", "盆景秀木正因为被人溺爱，才破灭了成为栋梁之材的梦。",
            "永远都不要放弃自己，勇往直前，直至成功！", "没有平日的失败，就没有最终的成功。重要的是分析失败原因并吸取教训。",
            "蝴蝶如要在百花园里得到飞舞的欢乐，那首先得忍受与蛹决裂的痛苦。",
            "萤火虫的光点虽然微弱，但亮着便是向黑暗挑战。", "面对人生旅途中的挫折与磨难，我们不仅要有勇气，更要有坚强的信念。",
            "对坚强的人来说，不幸就像铁犁一样开垦着他内心的大地，虽然痛，却可以播种。", "想而奋进的过程，其意义远大于未知的结果。",
            "上有天，下有地，中间站着你自己，做一天人，尽一天人事儿。",
            "努力向上的开拓，才使弯曲的竹鞭化作了笔直的毛竹。", "只要你确信自己正确就去做。做了有人说不好，不做还是有人说不好，不要逃避批判。",
            "对没志气的人，路程显得远；对没有银钱的人，城镇显得远。", "生命力的意义在于拼搏，因为世界本身就是一个竞技场。",
            "通过云端的道路，只亲吻攀登者的足迹。", "不经巨大的困难，不会有伟大的事业。",
            "泉水，奋斗之路越曲折，心灵越纯洁。", "人的一生，可以有所作为的时机只有一次，那就是现在。",
            "对于攀登者来说，失掉往昔的足迹并不可惜，迷失了继续前时的方向却很危险。", "生命的道路上永远没有捷径可言，只有脚踏实地走下去。",
            "时间顺流而下，生活逆水行舟。", "一个人，只要知道付出爱与关心，她内心自然会被爱与关心充满。",
            "你要求的次数愈多，你就越容易得到你要的东西，而且连带地也会得到更多乐趣。", "有大快乐的人，必有大哀痛；有大成功的人，必有大孤独。",
            "成功等于目标，其他都是这句话的注解。", "生命之长短殊不重要，只要你活得快乐，在有生之年做些有意义的事，便已足够。",
            "路在自己脚下，没有人可以决定我的方向。", "命运是不存在的，它不过是失败者拿来逃避现实的借口。",
            "生命太过短暂，今天放弃了明天不一定能得到。", "改变自我，挑战自我，从现在开始。",
    };

    public static String[] TYPE02_EMOJI_NAME = {"[emoji_02_angel]",
            "[emoji_02_angry]", "[emoji_02_astonished]", "[emoji_02_astonished_1]", "[emoji_02_confused]", "[emoji_02_cool]", "[emoji_02_cool_1]", "[emoji_02_cry]",
            "[emoji_02_cry_1]", "[emoji_02_devil]", "[emoji_02_dizzy]", "[emoji_02_expressionless]",
            "[emoji_02_flushed]", "[emoji_02_happy]", "[emoji_02_happy_1]",
            "[emoji_02_happy_2]", "[emoji_02_in_love]", "[emoji_02_injury]", "[emoji_02_joy]", "[emoji_02_kiss]", "[emoji_02_kiss_1]",
            "[emoji_02_kiss_2]", "[emoji_02_mask]", "[emoji_02_mute]", "[emoji_02_neutral]",
            "[emoji_02_sad]", "[emoji_02_sad_1]", "[emoji_02_scared]", "[emoji_02_scared_1]", "[emoji_02_secret]", "[emoji_02_shocked]", "[emoji_02_sick]",
            "[emoji_02_sleeping]", "[emoji_02_smile]", "[emoji_02_smile_1]", "[emoji_02_smiling]", "[emoji_02_smiling_1]",
            "[emoji_02_smirking]", "[emoji_02_surprised]", "[emoji_02_sweat]", "[emoji_02_thinking]", "[emoji_02_tired]", "[emoji_02_tongue]",
            "[emoji_02_tongue_1]", "[emoji_02_tongue_2]", "[emoji_02_vomiting]", "[emoji_02_wink]"};


    public static String[] TYPE01_EMOJI_NAME = {
            "[emoji_01_angry]", "[emoji_01_angry_1]", "[emoji_01_bored]", "[emoji_01_bored_1]",
            "[emoji_01_bored_2]", "[emoji_01_confused]", "[emoji_01_confused_1]", "[emoji_01_crying]",
            "[emoji_01_crying_1]", "[emoji_01_embarrassed]", "[emoji_01_emoticons]", "[emoji_01_happy]",
            "[emoji_01_happy_1]", "[emoji_01_happy_2]", "[emoji_01_happy_3]", "[emoji_01_happy_4]",
            "[emoji_01_ill]", "[emoji_01_in_love]", "[emoji_01_kissing]", "[emoji_01_mad]",
            "[emoji_01_nerd]", "[emoji_01_ninja]", "[emoji_01_quiet]", "[emoji_01_sad]", "[emoji_01_secret]",
            "[emoji_01_smart]", "[emoji_01_smile]", "[emoji_01_smiling]", "[emoji_01_surprised]",
            "[emoji_01_surprised_1]", "[emoji_01_suspicious]", "[emoji_01_suspicious_1]", "[emoji_01_tongue_out]",
            "[emoji_01_tongue_out_1]", "[emoji_01_unhappy]", "[emoji_01_wink]"
    };


    public static int[] TYPE01_EMOJI_DREWABLES = {
            R.drawable.emoji_01_angry, R.drawable.emoji_01_angry_1, R.drawable.emoji_01_bored,
            R.drawable.emoji_01_bored_1, R.drawable.emoji_01_bored_2, R.drawable.emoji_01_confused,
            R.drawable.emoji_01_confused_1, R.drawable.emoji_01_crying, R.drawable.emoji_01_crying_1,
            R.drawable.emoji_01_embarrassed, R.drawable.emoji_01_emoticons, R.drawable.emoji_01_happy,
            R.drawable.emoji_01_happy_1, R.drawable.emoji_01_happy_2, R.drawable.emoji_01_happy_3,
            R.drawable.emoji_01_happy_4, R.drawable.emoji_01_ill, R.drawable.emoji_01_in_love,
            R.drawable.emoji_01_kissing, R.drawable.emoji_01_mad, R.drawable.emoji_01_nerd,
            R.drawable.emoji_01_ninja, R.drawable.emoji_01_quiet, R.drawable.emoji_01_sad,
            R.drawable.emoji_01_secret, R.drawable.emoji_01_smart, R.drawable.emoji_01_smile,
            R.drawable.emoji_01_smiling, R.drawable.emoji_01_surprised, R.drawable.emoji_01_surprised_1,
            R.drawable.emoji_01_suspicious, R.drawable.emoji_01_suspicious_1, R.drawable.emoji_01_tongue_out,
            R.drawable.emoji_01_tongue_out_1, R.drawable.emoji_01_unhappy, R.drawable.emoji_01_wink
    };

    public static int[] TYPE02_EMOJI_DREWABLES = {
            R.drawable.emoji_02_angel, R.drawable.emoji_02_angry,
            R.drawable.emoji_02_astonished, R.drawable.emoji_02_astonished_1,
            R.drawable.emoji_02_confused, R.drawable.emoji_02_cool,
            R.drawable.emoji_02_cool_1, R.drawable.emoji_02_cry,
            R.drawable.emoji_02_cry_1, R.drawable.emoji_02_devil,
            R.drawable.emoji_02_dizzy, R.drawable.emoji_02_expressionless,
            R.drawable.emoji_02_flushed, R.drawable.emoji_02_happy, R.drawable.emoji_02_happy_1,
            R.drawable.emoji_02_happy_2, R.drawable.emoji_02_in_love, R.drawable.emoji_02_injury,
            R.drawable.emoji_02_joy, R.drawable.emoji_02_kiss,
            R.drawable.emoji_02_kiss_1, R.drawable.emoji_02_kiss_2, R.drawable.emoji_02_mask,
            R.drawable.emoji_02_mute, R.drawable.emoji_02_neutral, R.drawable.emoji_02_sad, R.drawable.emoji_02_sad_1,
            R.drawable.emoji_02_scared, R.drawable.emoji_02_scared_1, R.drawable.emoji_02_secret,
            R.drawable.emoji_02_shocked, R.drawable.emoji_02_sick, R.drawable.emoji_02_sleeping,
            R.drawable.emoji_02_smile, R.drawable.emoji_02_smile_1, R.drawable.emoji_02_smiling, R.drawable.emoji_02_smiling_1,
            R.drawable.emoji_02_smirking, R.drawable.emoji_02_surprised, R.drawable.emoji_02_sweat,
            R.drawable.emoji_02_thinking, R.drawable.emoji_02_tired,
            R.drawable.emoji_02_tongue, R.drawable.emoji_02_tongue_1, R.drawable.emoji_02_tongue_2,
            R.drawable.emoji_02_vomiting, R.drawable.emoji_02_wink
    };


    public final static class FriendCircleType {
        //纯文字
        public final static int FRIEND_CIRCLE_TYPE_ONLY_WORD = 0;
        //文字和图片
        public final static int FRIEND_CIRCLE_TYPE_WORD_AND_IMAGES = 1;
        //分享链接
        public final static int FRIEND_CIRCLE_TYPE_WORD_AND_URL = 2;
    }

    public final static class CommentType {
        //单一评论
        public final static int COMMENT_TYPE_SINGLE = 0;
        //回复评论
        public final static int COMMENT_TYPE_REPLY = 1;
    }

    public final static class EmojiType {
        //单一评论
        public final static int EMOJI_TYPE_01 = 1;
        //回复评论
        public final static int EMOJI_TYPE_02 = 2;
    }

}
