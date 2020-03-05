package com.example.a33626.endhomework2.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.models.MessageInfo;
import com.example.a33626.endhomework2.utils.EmojiDecode;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private List<MessageInfo> messages;
    private Context context;
    private LayoutInflater layoutInflater;
    private EmojiDecode emojiDecode;
    public MessageAdapter(List<MessageInfo> messages, Context context) {
        this.messages = messages;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        EmojiDecode.init(context);
        emojiDecode = EmojiDecode.getInstance();
    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public Object getItem(int position) {
        return this.messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageInfo message = this.messages.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.listview_item_message, null);
            viewHolder = new ViewHolder();
            viewHolder.headPortrait = (ImageView) convertView.findViewById(R.id.message_imageview_headportrait);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.message_textview_friendName);
            viewHolder.chatContent = (TextView) convertView.findViewById(R.id.message_textview_chatContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.headPortrait.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.emoji));
        viewHolder.friendName.setText(message.getFriendName());
        if (message.getChatType() == 1){
            message.setChatContent("[图片]");
        }
        else if (message.getChatType() == 2){
            message.setChatContent("[定位]");
        }
        viewHolder.chatContent.setText(emojiDecode.getEmojiSpan(message.getChatContent()));
        return convertView;
    }

    static class ViewHolder{
        public ImageView headPortrait;
        private TextView friendName;
        private TextView chatContent;
    }
}
