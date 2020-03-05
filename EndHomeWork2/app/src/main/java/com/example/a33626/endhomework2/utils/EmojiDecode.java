package com.example.a33626.endhomework2.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.example.a33626.endhomework2.R;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiDecode {

    /**
     * 单例模式
     * 1文字资源，图片资源
     * 2.使用正则表达式进行匹配文字
     * 3.把edittext当中整体的内容匹配正则表达式一次
     * 4.SpannableStringBuilder 进行替换
     */

    /**
     * 这个就是emoji
     * 就是把表情图片的资源id 和 名字 以键值对的形式存储 用的时候用正则去匹配一下
     *
     * 然后用 imagespan把文字替换成图片
     */
    private static EmojiDecode instance;
    public static EmojiDecode getInstance(){
        return instance;
    }

    public static void init(Context context){
        instance = new EmojiDecode(context);
    }

    private Context context;
    private Pattern ePattern; //用正则
    private Map<String, Integer> emojis;
    private String[] emojisKey;

    private static final int[] emojisValues = {
            R.drawable.e2,
            R.drawable.e3,
            R.drawable.e4,
            R.drawable.e5,
            R.drawable.e6,
            R.drawable.e7,
            R.drawable.e8,
            R.drawable.e9,
            R.drawable.e10,
            R.drawable.e11,
            R.drawable.e12,
            R.drawable.e13,
            R.drawable.e14,
            R.drawable.e15,
            R.drawable.e16,
            R.drawable.e17,
            R.drawable.e18,
            R.drawable.e19,
            R.drawable.e20,
            R.drawable.e21,
            R.drawable.e22,
            R.drawable.e23,
            R.drawable.e24
    };

    //私有构造器
    private EmojiDecode(Context context) {
        this.context = context;
        this.emojisKey = context.getResources().getStringArray(R.array.default_emoji_key);
        this.ePattern = buildPattern();
        this.emojis = buildeEmojis();
    }


    private Pattern buildPattern(){
        StringBuilder patternString = new StringBuilder(emojisKey.length);
        patternString.append('(');
        for(String s : emojisKey){
            patternString.append(Pattern.quote(s));//转字面量
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1,patternString.length(),")");
        return Pattern.compile(patternString.toString());
    }

    private Map<String, Integer> buildeEmojis(){
        if (emojisKey.length != emojisValues.length){
            throw new IllegalStateException("the length is ERROR");
        }
        Map<String, Integer> emojis = new HashMap<>();
        for (int i = 0;i < emojisKey.length;i++){
            emojis.put(emojisKey[i],emojisValues[i]);
        }
        return emojis;
    }

    /**
     * 根据表情的文本格式来替换成一个图片的序列
     */
    public CharSequence getEmojiSpan(CharSequence emojiText){
        SpannableStringBuilder builder = new SpannableStringBuilder(emojiText);
        //判断提取工具类 根据正则
        Matcher matcher = this.ePattern.matcher(emojiText);
        while (matcher.find()){
            int value = this.emojis.get(matcher.group());
            builder.setSpan(new ImageSpan(context,value),matcher.start(),matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}
