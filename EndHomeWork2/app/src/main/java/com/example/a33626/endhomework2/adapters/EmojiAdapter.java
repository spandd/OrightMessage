package com.example.a33626.endhomework2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.GridViewCallBack;
import com.example.a33626.endhomework2.commoninterface.ListViewCallBack;
import com.example.a33626.endhomework2.models.Emoji;

import java.util.List;

public class EmojiAdapter extends BaseAdapter {
    private List<Emoji> emojis;
    private Context context;
    private LayoutInflater layoutInflater;
    private GridViewCallBack gridViewCallBack;

    public EmojiAdapter(List<Emoji> emojis, Context context,GridViewCallBack gridViewCallBack) {
        this.emojis = emojis;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.gridViewCallBack = gridViewCallBack;
    }

    @Override
    public int getCount() {
        return this.emojis.size();
    }

    @Override
    public Object getItem(int position) {
        return this.emojis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Emoji emoji = this.emojis.get(position);
        EmojiAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.chat_emoji_container_item, null);
            viewHolder = new EmojiAdapter.ViewHolder();
            viewHolder.emoji = (ImageView) convertView.findViewById(R.id.chat_imageview_emojicontaineritem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmojiAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.emoji.setImageResource(emoji.getEmojiId());
        //调用回调接口的回调函数
        viewHolder.emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridViewCallBack.gridViewCallBackClick(position);
            }
        });
        return convertView;
    }


    static class ViewHolder{
        public ImageView emoji;
    }
}
