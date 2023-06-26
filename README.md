# Simple-TikTok-API

Simple-TikTok-API is an Android library that provides a simple way to fetch TikTok profile details without signing in. It is an unofficial API that utilizes Jsoup and OkHttp to fetch and extract data quickly.

## Features

- Check if an account is available
- Fetch the number of followers
- Fetch the number of followings
- Fetch Total number of likes
  Check if an account is verified
- Check if an account is private


## Installation

Simple-TikTok-API can be included in your Android project by adding the following dependency to your `build.gradle` file:

```groovy
dependencies {
    implementation 'com.github.itx-jd:Simple-TikTok-API:1.0.0'

}
```

Replace <version> with the appropriate version of OkHttp and Jsoup.

## Quick Usage

```groovy
TikTokAPI.getUserDetails("username", new TikTokAPI.TikTokUserListener() {
    @Override
    public void onUserDetailsReceived(TikTokUserDetails userDetails) {

        // Handle the received user details

        int followerCount = userDetails.getFollowerCount();
        int followingCount = userDetails.getFollowingCount();
        int totalLikes = userDetails.getTotalLikes();
        boolean isVerified = userDetails.isVerified();
        boolean isPrivateAccount = userDetails.isPrivateAccount();
    }

    @Override
    public void onUserNotExist() {

        // Handle the case when the user does not exist

        Log.d("User Details", "User does not exist");
    }

    @Override
    public void onError() {

        // Handle any errors that occur during the API request

        Log.e("User Details", "Error occurred during API request");
    }
});

```
Replace "username" with the actual TikTok username you want to fetch details for.

## Requirements

This project depends on the following:

- Java 8+
- OkHttp
- Jsoup

## Repository

GitHub Repository: [itx-jd/Simple-TikTok-API](https://github.com/itx-jd/Simple-TikTok-API)

## Contribution

Contributions to Simple-TikTok-API are welcome! If you encounter any issues or have suggestions for improvements, please open an issue or submit a pull request. Let's make this API even better together!

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.
