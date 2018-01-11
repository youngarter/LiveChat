package com.bringletech.looviedraft.livechat.data;


import com.bringletech.looviedraft.livechat.model.User;

import java.util.List;

public class StaticConfig {
    public static String TAG = "Youngsaid";
    public static int REQUEST_CODE_REGISTER = 2000;
    public static String STR_EXTRA_ACTION_LOGIN = "login";
    public static String STR_EXTRA_ACTION_RESET = "resetpass";
    public static String STR_EXTRA_ACTION = "action";
    public static String STR_EXTRA_USERNAME = "username";
    public static String STR_EXTRA_PASSWORD = "password";
    public static String STR_DEFAULT_BASE64 = "default";
    public static String UID = "";
    //TODO only use this UID for debug mode
//    public static String UID = "6kU0SbJPF5QJKZTfvW1BqKolrx22";
    public static String INTENT_KEY_CHAT_FRIEND = "friendname";
    public static String INTENT_KEY_CHAT_AVATA = "friendavata";
    public static String INTENT_KEY_CHAT_ID = "friendid";
    public static String INTENT_KEY_CHAT_ROOM_ID = "roomid";
    public static long TIME_TO_REFRESH = 10 * 1000;
    public static long TIME_TO_OFFLINE = 2 * 60 * 1000;

    //### *** ENDLESS ***
    public static String head_first_pack ;
    public static int maxUsers = 20 ;
    /**
     * The total number of items in the dataset after the last load
     */
    public static int mPreviousTotal = 0;
    /**
     * True if we are still waiting for the last set of data to load.
     */
    public static boolean mLoading = true;

    public static List<User> UserList;

    //FIN *** ENDLESS ***


}
