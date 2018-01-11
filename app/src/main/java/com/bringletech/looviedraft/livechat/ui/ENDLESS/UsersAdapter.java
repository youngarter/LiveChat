package com.bringletech.looviedraft.livechat.ui.ENDLESS;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bringletech.looviedraft.looviedraft.CLASSES.User;
import com.bringletech.looviedraft.looviedraft.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> UserList;
    private String TAG="youngsaid";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, age,city,country;
        public ImageView overflow;
        public SimpleDraweeView avatar;
        public Button love ;
        public Button like ;
        public Button chat ;

        public MyViewHolder(View view) {
            super(view);

            //controles declaration
            name = (TextView) view.findViewById(R.id.name);
            age = (TextView) view.findViewById(R.id.age);
            city = (TextView) view.findViewById(R.id.city);
            country = (TextView) view.findViewById(R.id.country);
            avatar = (SimpleDraweeView) view.findViewById(R.id.frescooo);

           // overflow = (ImageView) view.findViewById(R.id.overflow);

            love = (Button)view.findViewById(R.id.love);
            like = (Button)view.findViewById(R.id.like);
            chat = (Button)view.findViewById(R.id.chat);
        }
    }


    public UsersAdapter(Context mContext, List<User> UserList) {
        this.mContext = mContext;
        this.UserList = UserList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_profiles_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User user = UserList.get(position);
        holder.name.setText(user.getName());
        holder.age.setText(user.getAge() + " Yo");
        holder.city.setText(user.getCityCount());
        holder.country.setText(user.getCountry());

        if(user.getAvatar()  != null ) {
            Uri uri = Uri.parse(user.getAvatar());
            //### DraweeVieew show picture

            holder.avatar.setImageURI(uri);
            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, " Waache a ba :  " + user.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            //### like Press
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //### To show the timestamp
                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                   // Likes like = new Likes("Youngsaid",user.getNom(),ts);
                  //  like.SaveLike(like);
                }
            });

            //### love Press
            holder.love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, " you love :  " + user.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
               // loading User cover using Glide library
      //  Glide.with(mContext).load(User.getThumbnail()).into(holder.thumbnail);


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.users_small_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }
}
