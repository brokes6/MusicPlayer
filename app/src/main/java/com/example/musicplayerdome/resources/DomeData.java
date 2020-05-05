package com.example.musicplayerdome.resources;

import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.bean.RecommendMusicBean;
import com.example.musicplayerdome.bean.SongRecommendation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资源文件
 * 本地资源
 */
public class DomeData {
    /**
     * 推荐歌单资源
     * @return
     */
    public static List<SongRecommendation> getSongRecommendation() {
        List<SongRecommendation> list = new ArrayList<>();
        list.add(new SongRecommendation()
                .setImageText("绝对刺缴!早读也忍不住偷听的小众华语。")
                .setImageUrl("https://qpic.y.qq.com/music_cover/jF1CHiathfGvgNZrwbCZkzs3HNH3GZPHcx6nvzQFnkd6XcY3zItIySA/300?n=1"));

        list.add(new SongRecommendation()
                .setImageText("跑步热单：听完只想绕着操场跑200圈 ！")
                .setImageUrl("https://qpic.y.qq.com/music_cover/P4gD9Gv39kWkicEBxqbQbUVicR6oxMB7bibdGwJW1zhv7UhxiahLPEB4fA/300?n=1"));

        list.add(new SongRecommendation()
                .setImageText("酷劲十足的美式复古party")
                .setImageUrl("https://qpic.y.qq.com/music_cover/r1w2SVhQcKaU8fGwBKPeP6puNwmeZVsUbiakFwibDuDhw67DPaUBhz7g/300?n=1"));

        list.add(new SongRecommendation()
                .setImageText("一个人宅家，换个口味听经典")
                .setImageUrl("https://qpic.y.qq.com/music_cover/PW5V5hCPicL8uJB8k0HAnw9bjTmkHjGlkdfwY1NOuZaYvPvlzBVtREA/600?n=1"));

        list.add(new SongRecommendation()
                .setImageText("「Along」任寂寞侵袭空荡的心灵")
                .setImageUrl("https://qpic.y.qq.com/music_cover/OmT4ibflJ4UHdpVjIJM6iaMAaSOnslkZsXYhII3BNQFCcloADoicLdtVw/300?n=1"));
        return list;
    }

    /**
     * 我的歌单资源
     * @return
     */
    public static List<SongRecommendation> getMySong() {
        List<SongRecommendation> list = new ArrayList<>();
        list.add(new SongRecommendation()
                .setImageUrl("https://qpic.y.qq.com/music_cover/OmT4ibflJ4UHdpVjIJM6iaMAaSOnslkZsXYhII3BNQFCcloADoicLdtVw/300?n=1"));

        list.add(new SongRecommendation()
                .setImageUrl("https://qpic.y.qq.com/music_cover/lPk6icmeF66MmhTHGlFUwlvrCRHWvuAZxJRKw5oja7BKz2h2YlBlJqA/600?n=1"));

        list.add(new SongRecommendation()
                .setImageUrl("https://qpic.y.qq.com/music_cover/dLianDZYXEWvUhc2CMENibkOz7SVGIDsrgyHbOnV0uAG6kiaBKBX10QZg/300?n=1"));

        list.add(new SongRecommendation()
                .setImageUrl("https://qpic.y.qq.com/music_cover/zWAjtOcnpLTJ07iacJUgay3SC9WZPgZrqSKUkGZXM5JgmQIdeonXmSA/600?n=1"));
        return list;
    }

    /**
     * 轮播图资源
     * @return
     */
    public static List<?> getBanner() {
        String[] urls = new String[]{
                "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",//伪装者:胡歌演绎"痞子特工"
                "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",//无心法师:生死离别!月牙遭虐杀
                "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",//花千骨:尊上沦为花千骨
                "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",//综艺饭:胖轩偷看夏天洗澡掀波澜
                "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
        };
        return Arrays.asList(urls);
    }

    /**
     * 主页推荐歌单资源
     * @return
     */
    public static List<Audio> getRecommendMusic(){
        List<Audio> list = new ArrayList<>();
        list.add(new Audio()
                .setText("告白气球")
                .setimgUrl("https://y.gtimg.cn/music/photo_new/T002R300x300M000003RMaRI1iFoYd_1.jpg?max_age=2592000")
                .setUrl("https://sharefs.yun.kugou.com/202005031011/badd2dcee58c6cfb8d80faa91654e6ab/G114/M01/1F/10/sg0DAFnWF0mAWSkzADPphbA0r2s996.mp3")
                .setauthor("周杰伦")
                .setintroduce("描述遇上爱情的悸动；但是非常特别的是副歌")
                .setid(1)
        );
        list.add(new Audio()
                .setText("Maps")
                .setimgUrl("https://y.gtimg.cn/music/photo_new/T002R300x300M000003qgLUt07WXVS_1.jpg?max_age=2592000")
                .setUrl("https://sharefs.yun.kugou.com/202005031011/3372adb42fe47f6c6e8e64d127945fb4/G196/M09/1D/10/pJQEAF5Q7_-AK5BUAC5sFI5LTMc356.mp3")
                .setauthor("Maroon 5")
                .setintroduce("《Maps》是一首失恋的歌曲吧")
                .setid(2)
        );
        list.add(new Audio()
                .setText("我是如此相信")
                .setimgUrl("https://y.gtimg.cn/music/photo_new/T002R300x300M000001hGx1Z0so1YX_1.jpg?max_age=2592000")
                .setUrl("https://sharefs.yun.kugou.com/202005031011/917f2085beb2cc5ad6f722b7441e05b4/G179/M08/1D/0F/8w0DAF3zZCaATgO6AEENs8ADOpM292.mp3")
                .setauthor("周杰伦")
                .setintroduce("《我是如此相信》歌词饱含满满深意，引人遐思。")
                .setid(3)
        );
        return list;
    }

    /**
     * 歌单列表的资源
     * @return
     */
    public static List<Audio> getAudioMusic(){
        List<Audio> list = new ArrayList<>();
        list.add(new Audio()
                .setText("突然想起你")
                .setimgUrl("https://p3fx.kgimg.com/stdmusic/20150718/20150718073742794801.jpg")
                .setUrl("https://sharefs.yun.kugou.com/202005041559/6fd3a8bd315f56a7bbeaa1c5ae74f93e/G078/M02/0D/18/jg0DAFhwj_iAMlxkADJKTpoJJ6g903.mp3")
                .setauthor("林宥嘉")
                .setid(1)
        );
        list.add(new Audio()
                .setText("ツギハギスタッカート")
                .setimgUrl("https://p3fx.kgimg.com/stdmusic/20170803/20170803105723296968.jpg")
                .setUrl("https://sharefs.yun.kugou.com/202005041600/e417a9a11c0b3d387c84d992869a7a10/G109/M05/14/05/DYcBAFmChraAWB69ADycuwiDeCs643.mp3")
                .setauthor("初音未来")
                .setid(2)
        );
        list.add(new Audio()
                .setText("ミスターフィクサー")
                .setimgUrl("https://y.gtimg.cn/music/photo_new/T002R300x300M000004WJSNB2WDUo6_1.jpg?max_age=2592000")
                .setUrl("https://sharefs.yun.kugou.com/202005041600/84c5d38269b266fde9e70000e3c6f410/G191/M09/05/0B/X4cBAF4Sj0iAcecWADWuVe9xtII389.mp3")
                .setauthor("Sou")
                .setid(3)
        );
        list.add(new Audio()
                .setText("Other Side")
                .setimgUrl("https://y.gtimg.cn/music/photo_new/T002R300x300M000000zzRGb3NysZK_1.jpg?max_age=2592000")
                .setUrl("https://sharefs.yun.kugou.com/202005041600/109033f37baab942a4b2c9c04c8d202f/G169/M03/10/0B/iZQEAF0ucCSAX6kFADeqFkOQg-M882.mp3")
                .setauthor("MIYAVI")
                .setid(4)
        );
        list.add(new Audio()
                .setText("Heathens／Stressed Out")
                .setimgUrl("https://y.gtimg.cn/music/photo_new/T002R300x300M000004AV09d0DAql2_1.jpg?max_age=2592000")
                .setUrl("https://sharefs.yun.kugou.com/202005031009/4bb840432062125b542341a2934de76d/G094/M08/1B/15/PpQEAFiidJiAVJ5YAC7lEdkPo24251.mp3")
                .setauthor("Biscuits")
                .setid(5)
        );
        return list;
    }
}
