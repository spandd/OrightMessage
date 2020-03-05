package com.example.a33626.endhomework2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a33626.endhomework2.R;
import com.example.a33626.endhomework2.commoninterface.ListViewCallBack;
import com.example.a33626.endhomework2.models.Friend;

import java.util.List;

public class FriendAdapter extends BaseAdapter{
    private List<Friend> friends;
    private Context context;
    private LayoutInflater layoutInflater;
    private ListViewCallBack callBackInterface;

    public FriendAdapter(List<Friend> friends, Context context,ListViewCallBack callBackInterface) {
        this.friends = friends;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.callBackInterface = callBackInterface;
    }

    @Override
    public int getCount() {
        return this.friends.size();
    }

    @Override
    public Object getItem(int position) {
        return this.friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Friend friend = this.friends.get(position);
        FriendAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.listview_item_friend, null);
            viewHolder = new FriendAdapter.ViewHolder();
            viewHolder.headPortrait = (ImageView) convertView.findViewById(R.id.friend_imageview_headportrait);
            viewHolder.friendName = (TextView) convertView.findViewById(R.id.friend_textview_friendname);
            viewHolder.meIsAccept = (Button) convertView.findViewById(R.id.friend_button_meisaccept);
            viewHolder.friendIsAccept = (TextView) convertView.findViewById(R.id.friend_textview_friendisaccept);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FriendAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.headPortrait.setImageBitmap(friend.getFriendHeadPortrait());
        viewHolder.friendName.setText(friend.getFriendName());
        if (friend.getMeIsAccept() == 1){
            //如果同意的话就直接给他disable
            viewHolder.meIsAccept.setBackground(context.getResources().getDrawable(R.drawable.shape_corner_all_button_me));
            viewHolder.meIsAccept.setEnabled(false);
        }
        else{
            viewHolder.meIsAccept.setBackground(context.getResources().getDrawable(R.drawable.shape_corner_all_button_friend));
            viewHolder.meIsAccept.setEnabled(true);
        }

        if (friend.getFriendIsAccept() == 1){
            viewHolder.friendIsAccept.setBackground(context.getResources().getDrawable(R.drawable.shape_corner_all_button_me));
        }
        else{
            viewHolder.friendIsAccept.setBackground(context.getResources().getDrawable(R.drawable.shape_corner_all_button_friend));
        }
//        if(friend.getMeIsAccept() == 1 && friend.getFriendIsAccept() == 1){
//            viewHolder.meIsAccept.setVisibility(View.GONE);
//            viewHolder.friendIsAccept.setVisibility(View.GONE);
//        }

        viewHolder.meIsAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用回调接口的回调函数
                callBackInterface.listViewCallBackClick(position);
            }
        });
        return convertView;
    }


    static class ViewHolder{
        public ImageView headPortrait;
        private TextView friendName;
        private Button meIsAccept;
        private TextView friendIsAccept;
    }
}
