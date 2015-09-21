package com.example.vjobanputra.instagramclient;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InstagramPhoto photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
        TextView tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
        TextView tvRelativeTime = (TextView)convertView.findViewById(R.id.tvRelativeTime);
        TextView tvLikes = (TextView)convertView.findViewById(R.id.tvLikes);
        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
        ImageView ivProfilePic = (ImageView)convertView.findViewById(R.id.ivProfilePic);

        // Username, caption, likes
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        tvLikes.setText(photo.likesCount + " likes");
        // Time
        CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        tvRelativeTime.setText(relativeTime.toString());
        // Profile Pic, Photo
        ivPhoto.setImageResource(0);
        ivProfilePic.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        Picasso.with(getContext()).load(photo.profilePicUrl).into(ivProfilePic);
        // Comments
        LinearLayout llComments = (LinearLayout) convertView.findViewById(R.id.llComments);
        llComments.removeAllViews();
        if (photo.comments != null) {
            for (PhotoComment comment : photo.comments) {
                TextView textView = new TextView(getContext());
                textView.setTextSize(15);
                textView.setText(Html.fromHtml("<B><font color='#086A87'>" + comment.username + "</B></font> " + comment.text));
                llComments.addView(textView);
            }
        }

        return convertView;
    }
}
