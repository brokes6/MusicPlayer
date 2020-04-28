package com.example.musicplayerdome.resources;

import com.example.musicplayerdome.bean.SongRecommendation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DomeData {
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
}
