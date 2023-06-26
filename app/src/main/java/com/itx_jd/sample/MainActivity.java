package com.itx_jd.sample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itx_jd.simple_tiktok_api.TikTokAPI;
import com.itx_jd.simple_tiktok_api.TikTokUserDetails;

public class MainActivity extends AppCompatActivity {

    TextView tv_followers,tv_following,tv_likes,tv_verified,tv_private;
    EditText et_username;
    Button bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_followers  = findViewById(R.id.tv_followers);
        tv_following  = findViewById(R.id.tv_following);
        tv_likes  = findViewById(R.id.tv_likes);
        tv_verified  = findViewById(R.id.tv_verified);
        tv_private  = findViewById(R.id.tv_private);

        et_username  = findViewById(R.id.et_username);
        bt_search  = findViewById(R.id.bt_search);


        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = et_username.getText().toString().trim();

                if(!username.isEmpty()){

                    TikTokAPI.getUserDetails(username, new TikTokAPI.TikTokUserListener() {
                        @Override
                        public void onUserDetailsReceived(TikTokUserDetails userDetails) {

                            // Handle the user details
                            String followerCount = userDetails.getFollowerCount();
                            String followingCount = userDetails.getFollowingCount();
                            String totalLikes = userDetails.getTotalLikes();
                            boolean isVerified = userDetails.isVerified();
                            boolean isPrivateAccount = userDetails.isPrivateAccount();

                            tv_followers.setText("Follower Count: " + followerCount);
                            tv_following.setText("Following Count: " + followingCount);
                            tv_likes.setText("Total Likes: " + totalLikes);
                            tv_verified.setText("Is Verified: " + String.valueOf(isVerified));
                            tv_private.setText("Is Private Account: " + String.valueOf(isPrivateAccount));

                        }

                        @Override
                        public void onUserNotExist() {
                            // Handle the case where the user does not exist
                            Toast.makeText(MainActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            // Handle the error condition
                            Toast.makeText(MainActivity.this, "Error occurred while fetching user details", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(MainActivity.this, "Empty TextBox", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}