package com.example.a33626.endhomework2.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.ListViewCallBack;
import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Chat;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.EmojiDecode;
import com.example.a33626.endhomework2.utils.ImageUtil;
import com.example.a33626.endhomework2.view.BubbleTextLayout;

import java.util.List;

/**
 * 这个是自己写的适配器 继承baseadapter
 * 这个原理就是把实体类放到listview对应的每个子view中
 */
public class ChatAdapter extends BaseAdapter{
    private List<Chat> chats;
    private Context context;
    private LayoutInflater layoutInflater;
    private Friend friend;
    private User user;
    private EmojiDecode emojiDecode;
    private ListViewCallBack listViewCallBack;
    public ChatAdapter(List<Chat> chats, Friend friend, User user, Context context,ListViewCallBack listViewCallBack) {
        this.chats = chats;
        this.friend = friend;
        this.user = user;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.listViewCallBack = listViewCallBack;
        EmojiDecode.init(context);
        emojiDecode = EmojiDecode.getInstance();
    }

    @Override
    public int getCount() {
        return this.chats.size();
    }

    @Override
    public Object getItem(int position) {
        return this.chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Chat chat = this.chats.get(position);
        if (chat.getChatPhotoS() != null){
            chat.setChatPhotoB(ImageUtil.blobToBitMap(ImageUtil.base64ToImage(chat.getChatPhotoS())));
        }
        ChatAdapter.ViewHolder viewHolder;
        viewHolder = new ChatAdapter.ViewHolder();
        if (chat.getIsMe() == Constants.TRUES){
            //这个就是空间初始化
            convertView = this.layoutInflater.inflate(R.layout.listview_rightitem_messagecontent, null);
            viewHolder.chatContent = convertView.findViewById(R.id.chat_textview_content_right);
            viewHolder.headPortrait = convertView.findViewById(R.id.chat_imageview_headportarit_right);
            viewHolder.chatPhoto = convertView.findViewById(R.id.chat_imageview_content_right);
            viewHolder.chatLocation = convertView.findViewById(R.id.chat_button_share_right);
            viewHolder.bubbleTextLayout = convertView.findViewById(R.id.chat_buddlelayout_right);
            setViewValue(viewHolder,chat);
        }
        else{
            convertView = this.layoutInflater.inflate(R.layout.listview_leftitem_messagecontent, null);
            viewHolder.chatContent = convertView.findViewById(R.id.chat_textview_content_left);
            viewHolder.headPortrait = convertView.findViewById(R.id.chat_imageview_headportarit_left);
            viewHolder.chatPhoto = convertView.findViewById(R.id.chat_imageview_content_left);
            viewHolder.chatLocation =  convertView.findViewById(R.id.chat_button_share_left);
            viewHolder.bubbleTextLayout = convertView.findViewById(R.id.chat_buddlelayout_left);
            setViewValue(viewHolder,chat);
        }
        if (chat.getIsMe() == Constants.TRUES){
            if (user.getBlobHeadPortrait()!= null){
                viewHolder.headPortrait.setImageBitmap(ImageUtil.blobToBitMap(user.getBlobHeadPortrait()));
            }
            else{
                viewHolder.headPortrait.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.emoji));
            }
        }
        else{
            //viewHolder.headPortrait.setImageBitmap(friend.getFriendHeadPortrait());
        }
        viewHolder.chatLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewCallBack.listViewCallBackClick(position);
            }
        });
        return convertView;
    }

    public void setViewValue(ChatAdapter.ViewHolder viewHolder,Chat chat){
        //这个就是初始化完毕后 赋值
        if (chat.getChatType() == Constants.CHAT_TYPE_PHOTO){
            //图片消息
            viewHolder.chatPhoto.setImageBitmap(chat.getChatPhotoB());
            viewHolder.chatPhoto.setVisibility(View.VISIBLE);
        }
        else if (chat.getChatType() == Constants.CHAT_TYPE_COMMON){
            //普通文字表情消息
            viewHolder.chatContent.setText(emojiDecode.getEmojiSpan(chat.getChatContent()));
            viewHolder.chatContent.setVisibility(View.VISIBLE);
        }
        else if (chat.getChatType() == Constants.CHAT_TYPE_LOCATION){
            //定位消息
            viewHolder.chatLocation.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * 这个是viewholder
     * 里面是listitem中所有的view
     */

    static class ViewHolder{
        private ImageView headPortrait;
        private ImageView chatPhoto;
        private TextView chatContent;
        private Button chatLocation;
        private BubbleTextLayout bubbleTextLayout;
    }

}
