package com.uni.julio.supertv.utils.networing;

public class WebConfig {
    private static final String domain = "https://supertvplus.com/";
    public static final String baseURL              = "https://supertvplus.com/Ip7hEQjdZdaMLb8gCWf/";
    public static final String GetCodeURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/loginCode.php?request_code";
    public static final String LoginSplash = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/login.php?user={USER}&pass={PASS}&device_id={DEVICE_ID}&splash";
    public static final String getMessage = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/getMessage.php?user={USER}";
    public static final String removeUserURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/login.php?user={USER}&device_num=0&device_id={DEVICE_ID}&delete";
    public static final String trackingURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/tracking.php?username={USER}&movie={MOVIE}&ip={IP}&device_id={DEVICE_ID}&isTV={ISTV}";
    public static final String likeURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/like.php?movieID={MOVIEID}&like={LIKE}&dislike={DISLIKE}";
    public static final String getLikeURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/getLike.php?movieID={MOVIEID}";
    public static final String loginURL             = domain + "/GsZzwW3PqfXm268C/Connect/login.php?user={USER}&pass={PASS}&device_id={DEVICE_ID}&model={MODEL}&fw={FW}&country={COUNTRY}";
    public static final String liveTVCategoriesURL          = domain + "GsZzwW3PqfXm268C/Connect/live_categorias.php";
    public static final String liveTVChannelsURL            = domain + "/GsZzwW3PqfXm268C/Connect/live_canales.php?cve={CAT_ID}";
    public static final String updateURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/upgrade_version.php?new_version";
    public static final String videoSearchURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/searchVideo.php?type={TYPE}&pattern={PATTERN}";
}
