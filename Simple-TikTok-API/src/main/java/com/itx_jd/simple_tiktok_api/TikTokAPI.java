package com.itx_jd.simple_tiktok_api;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TikTokAPI {

    // Interface for receiving TikTok user details callbacks
    public interface TikTokUserListener {
        void onUserDetailsReceived(TikTokUserDetails userDetails);
        void onUserNotExist();
        void onError();
    }

    // Method to fetch user details
    public static void getUserDetails(String username, TikTokUserListener listener) {
        new FetchUserDetailsTask(listener).execute(username);
    }

    // AsyncTask to fetch user details from TikTok API
    private static class FetchUserDetailsTask extends AsyncTask<String, Void, TikTokUserDetails> {

        private TikTokUserListener listener;

        public FetchUserDetailsTask(TikTokUserListener listener) {
            this.listener = listener;
        }

        @Override
        protected TikTokUserDetails doInBackground(String... usernames) {
            OkHttpClient client = new OkHttpClient();

            try {
                String url = "https://www.tiktok.com/@" + usernames[0];
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String html = responseBody.string();
                        return parseUserDetails(html);
                    }
                } else if (response.code() == 404) {
                    return null; // User does not exist
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null; // Return null to indicate an error occurred
        }

        @Override
        protected void onPostExecute(TikTokUserDetails userDetails) {
            if (userDetails != null) {
                listener.onUserDetailsReceived(userDetails);
            } else {
                listener.onUserNotExist();
            }
        }

        // Method to parse user details from HTML response
        private TikTokUserDetails parseUserDetails(String html) {
            TikTokUserDetails userDetails = new TikTokUserDetails();

            // Use regular expressions to extract user details
            Pattern followerCountPattern = Pattern.compile("\"followerCount\":(\\d+)");
            Matcher followerCountMatcher = followerCountPattern.matcher(html);

            Pattern followingCountPattern = Pattern.compile("\"followingCount\":(\\d+)");
            Matcher followingCountMatcher = followingCountPattern.matcher(html);

            // Alternate way using Jsoup library | IDK Why regular expressions is not working :/
            Document doc = Jsoup.parse(html);
            Elements likesElements = doc.select("strong[data-e2e=likes-count]");

            // Use regular expressions to extract user details
            Pattern isVerifiedPattern = Pattern.compile("\"verified\":(true|false)");
            Matcher isVerifiedMatcher = isVerifiedPattern.matcher(html);

            Pattern isPrivatePattern = Pattern.compile("\"privateAccount\":(true|false)");
            Matcher isPrivateMatcher = isPrivatePattern.matcher(html);

            if (followerCountMatcher.find()) {
                String followerCountText = followerCountMatcher.group(1);
                userDetails.setFollowerCount(followerCountText);
            }

            if (followingCountMatcher.find()) {
                String followingCountText = followingCountMatcher.group(1);
                userDetails.setFollowingCount(followingCountText);
            }

            if (!likesElements.isEmpty()) {
                Element firstLikesElement = likesElements.first();
                String likesCountText = firstLikesElement.text();
                userDetails.setTotalLikes(likesCountText);
            }

            if (isVerifiedMatcher.find()) {
                String isVerifiedText = isVerifiedMatcher.group(1);
                userDetails.setVerified(Boolean.parseBoolean(isVerifiedText));
            }

            if (isPrivateMatcher.find()) {
                String isPrivateText = isPrivateMatcher.group(1);
                userDetails.setPrivateAccount(Boolean.parseBoolean(isPrivateText));
            }

            return userDetails;
        }
    }
}
