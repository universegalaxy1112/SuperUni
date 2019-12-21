package com.uni.julio.supertv.utils.networing;

public class WebConfig {

    private static final String domain = "https://supertvplus.com/";
    public static final String baseURL              = "https://supertvplus.com/Ip7hEQjdZdaMLb8gCWf/";
    public static final String GetCodeURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/loginCode.php?request_code";
    public static final String LoginCodeURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/loginCode.php?dealer_code={CODE}";
    public static final String removeUserURL = "https://supertvplus.com/GsZzwW3PqfXm268C/Connect/login.php?user={USER}&delete";
    public static final String loginURL             = domain + "/GsZzwW3PqfXm268C/Connect/login.php?user={USER}&pass={PASS}&device_id={DEVICE_ID}&model={MODEL}&fw={FW}&country={COUNTRY}";
    public static final String liveTVCategoriesURL          = domain + "GsZzwW3PqfXm268C/Connect/live_categorias.php";
    public static final String liveTVChannelsURL            = domain + "/GsZzwW3PqfXm268C/Connect/live_canales.php?cve={CAT_ID}";
}
