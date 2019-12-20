package com.uni.julio.supertv.utils.networing;

public class WebConfig {

    private static final String domain = "https://panel.superteve.com:400/";
    public static final String baseURL              = "https://panel.superteve.com:400/Sk7suqPN2S0zTNgdxT/";
    public static final String GetCodeURL = "https://panel.superteve.com:400/ANXDRXOIXD/API/loginCode.php?request_code";
    public static final String LoginCodeURL = "https://panel.superteve.com:400/ANXDRXOIXD/API/loginCode.php?dealer_code={CODE}";
    public static final String removeUserURL = "https://panel.superteve.com:400/ANXDRXOIXD/API/login.php?user={USER}&delete";
    public static final String loginURL             = domain + "/ANXDRXOIXD/API/login.php?user={USER}&pass={PASS}&device_id={DEVICE_ID}&model={MODEL}&fw={FW}&country={COUNTRY}";
    public static final String liveTVCategoriesURL          = domain + "ANXDRXOIXD/API/live_categorias.php";
    public static final String liveTVChannelsURL            = domain + "/ANXDRXOIXD/API/live_canales.php?cve={CAT_ID}";
}
