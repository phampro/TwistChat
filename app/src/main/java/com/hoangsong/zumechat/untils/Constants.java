package com.hoangsong.zumechat.untils;


public class Constants {
	public final static String TAG = "BAOSONGNGU_GCM";
	public static final String _CACHED_FOLDER []={"SIGN", "TEMP", "Photo"};
	
	public static final String GCM_API_GCM= "RegisterGCMToken";
	private static final String SENDER_ID_STAG = "773794801070";// project mastercab key testOfQuocThanh vantangst@gmail.com
	private static final String SENDER_ID_PROD = "773794801070";//"645991146506"; project mastercab key testOfQuocThanh vantangst@gmail.com
	public static final String GCM_TAG="GNC-GMC";
	
	public static final String GCM_MSG="message";
	public static final String GCM_TYPE="type";
	public static final String GCM_KEYID="booking_id";

	public final static boolean DEBUG_MODE = true;
	public final static boolean IS_PRODUCTION = false;
	public final static boolean IS_SHOW_ADS = true;
	public final static boolean IS_ADMOB_PRO = false;

	public final static int ERROR_CODE_SUCCESS = 0;
	public static int ERROR_ACCOUNT_SUSPENDED = 40678;
	public static int ERROR_TOKEN_INVALID = 408;

	public static final String EMAIL_CONTACT= "twist@contact.com";

	public static final String _USERNAME_BLACKLIST []={"supportdetails", "support-details", "about", "account", "activate", "admin", "administrator", "config", "configuration", "connect",  "download", "downloads", "edit", "email", "employment", "enterprise", "faq", "favorites", "feed", "feedback", "follow", "followers", "following", "hosting", "hostmaster", "idea", "ideas", "index", "info", "invitations", "invite", "job", "jobs", "json", "language", "languages", "login", "logout", "logs", "mail", "map", "maps", "mine", "news", "organizations", "plans", "popular", "post", "postmaster", "privacy", "projects", "put", "recruitment", "register", "security", "sessions", "settings", "signup", "sitemap", "ssl", "ssladmin", "ssladministrator", "sslwebmaster", "status", "stories", "subscribe", "subscriptions", "support", "sysadmin", "sysadministrator","unfollow", "unsubscribe", "update", "url", "user", "weather", "wiki", "ww", "www", "wwww", "fuck", "motherfucker", "test", "tester"};
	public static final String _MSG_BLACKLIST []={"anal", "bastard", "bitch", "bloody", "blowjob", "blow job", "bollock", "bollok	", "boob", "clitoris", "cock", "crap", "cunt", "dick", "dildo", "dyke", "fag", "feck", "fellate", "fellatio", "fuck", "f u c k", "fudge packer", "jerk", "muff", "nigger", "nigga", "penis", "piss", "poop", "prick", "pube", "pussy", "queer", "scrotum",  "shit", "s hit", "slut", "tit", "whore", "wtf"};

	public static final String PUSH_TOKEN_INVALID = "pushtokeninvalid";
	public static final String PUSH_RELOAD_MENU="pushreloadmenu";
	public static final String PUSH_RELOAD_MENU_AND_CHANGE_PASS="pushreloadmenuandchangepass";
	public static final String PUSH_NO_INTERNET="pushnointernet";
	public static final String PUSH_CONNECT_INTERNET="pushconnectinternet";

	public final static String URL_SHARE_APP = "https://play.google.com/store/apps/details?id=com.hoangsong.docbaosongngu";


	public static String getGCMSenderID(){
		if(IS_PRODUCTION){
			return SENDER_ID_PROD;
		}
		else{
			return SENDER_ID_STAG;
		}
	}
	//language
	public final static String LANGUAGE_EN = "en";
	public final static String LANGUAGE_VI = "vi";

	// API
	public final static String API_USERNAME = "apiZCCustomer";
	public final static String API_PASSWORD = "Z?C19!01Hat";
	public final static String API_DEVICE = "android";
	private final static String URL_PROD = "http://www.zuchatapp.cf/";
	private final static String URL_STAG = "http://www.zuchatapp.cf/";
	//private final static String URL_STAG = "http://192.168.0.100/ZuChat.API/";
	public final static String MODULE_API = "v1/Customer/";
	public final static String API_KEY = "p3O8MRSXxV0opJXe2BT322wGIRmxyxMHaQiJf3Ec8SfRR5irg9D0jJMaBAbT";
	//public final static String ID_ADMOB = "ca-app-pub-6925282602161900~7723619875";// of tang
	public final static String ID_ADMOB = "ca-app-pub-5966183733493342~2088471718";// of song

	public final static String HOST_SIGNAL_R = URL_STAG+"signalr/Hubs";

	public static final String API_REGISTER_DEVICE_ID = getURL() + MODULE_API + "RegisterDevice";
	public static final String API_UPDATE_DEVICE_ID = getURL() + MODULE_API + "UpdateDevice";

	public final static String FORGOT_PASSWORD = getURL() + MODULE_API + "ForgotPassword";
	public final static String CHANGE_PASSWORD = getURL() + MODULE_API + "change-pass";
	public final static String LOGIN = getURL() + MODULE_API + "Login";
	public final static String GET_PROFILE = getURL() + MODULE_API + "GetProfile";
	public final static String REGISTER = getURL() + MODULE_API + "Register";
	public final static String FEEDBACK = getURL() + MODULE_API + "feedback";
	public final static String SEND_MESSAGE_CHAT = getURL() + MODULE_API + "SendMessageChat";
	public final static String UPDATE_MESSAGE_CHAT = getURL() + MODULE_API + "UpdateMessageChat";
	public final static String DELETE_MESSAGE_CHAT = getURL() + MODULE_API + "DeleteMessageChat";
	public final static String GET_CHAT_MESSAGES = getURL() + MODULE_API + "GetMessageChats";
	public final static String SEARCH_FRIEND = getURL() + MODULE_API + "SearchFriend";
	public final static String GET_FAVOURITES = getURL() + MODULE_API + "GetFavourites";
	public final static String DELETE_CHAT_BY_ACCOUNT = getURL() + MODULE_API + "DeleteMessageChatByAccount";
	public final static String BLOCK_ACCOUNT = getURL() + MODULE_API + "BlockAccount";
	public final static String UNBLOCK_ACCOUNT = getURL() + MODULE_API + "UnblockAccount";
	public final static String GET_CONTACT_PROFILE = getURL() + MODULE_API + "GetContactProfile";
	public final static String REMOVE_FAVOURITE = getURL() + MODULE_API + "RemoveFavourite";
	public final static String GET_MASTER_DATA= getURL() + MODULE_API + "GetMasterData";
	public final static String CHECK_ACCOUNT_REPORT= getURL() + MODULE_API + "CheckAccountReport";
	public final static String CHECK_ACCOUNT_BLOCK= getURL() + MODULE_API + "CheckAccountBlock";
	public final static String REPORT_ACCOUNT= getURL() + MODULE_API + "ReportAccount";
	public final static String GET_CREDIT_PACKAGE= getURL() + MODULE_API + "GetCreditPackage";
	public final static String BUY_CREDIT_PACKAGE= getURL() + MODULE_API + "BuyCreditPackage";
	public final static String DELETE_ACCOUNT= getURL() + MODULE_API + "DeleteAccount";


	public final static String UPDATE_PROFILE_PICKTURE = getURL() + MODULE_API + "UpdateProfilePicture";
	public final static String UPDATE_PROFILE = getURL() + MODULE_API + "UpdateProfile";
	public final static String UPDATE_JOB_STATUS = getURL() + MODULE_API + "UpdateJobStatus";
	public final static String ADD_FAVOURITE = getURL() + MODULE_API + "AddFavourite";
	public final static String CREATE_ADS = getURL() + MODULE_API + "CreateAdvertisement";
	public final static String GET_ALL_ADVERTISEMENT_LIST = getURL() + MODULE_API + "GetAllAdvertisementList";
	public final static String DELETE_ADVERTISEMENT = getURL() + MODULE_API + "DeleteAdvertisement";



	private static int processID=0;
	public final static int ID_METHOD_API_REGISTER_DEVICE_ID = ++processID;
	public final static int ID_METHOD_API_UPDATE_DEVICE_ID = ++processID;

	public final static int ID_METHOD_CHANGE_PASSWORD = ++processID;
	public final static int ID_METHOD_FORGOT_PASSWORD = ++processID;
	public final static int ID_METHOD_LOGIN = ++processID;
	public final static int ID_METHOD_GET_PROFILE = ++processID;
	public final static int ID_METHOD_REGISTER = ++processID;
	public final static int ID_METHOD_FEEDBACK= ++processID;
	public final static int ID_METHOD_SEND_MESSAGE_CHAT= ++processID;
	public final static int ID_METHOD_UPDATE_MESSAGE_CHAT= ++processID;
	public final static int ID_METHOD_DELETE_MESSAGE_CHAT= ++processID;
	public final static int ID_METHOD_GET_CHAT_MESSAGES= ++processID;
	public final static int ID_METHOD_SEARCH_FRIEND = ++processID;
	public final static int ID_METHOD_SEARCH_FRIEND_NEARBY = ++processID;
	public final static int ID_METHOD_GET_FAVOURITES = ++processID;
	public final static int ID_METHOD_DELETE_CHAT_BY_ACCOUNT = ++processID;
	public final static int ID_METHOD_BLOCK_ACCOUNT = ++processID;
	public final static int ID_METHOD_UNBLOCK_ACCOUNT = ++processID;
	public final static int ID_METHOD_GET_CONTACT_PROFILE = ++processID;
	public final static int ID_METHOD_REMOVE_FAVOURITE = ++processID;
	public final static int ID_METHOD_GET_MASTER_DATA = ++processID;
	public final static int ID_METHOD_CHECK_ACCOUNT_REPORT= ++processID;
	public final static int ID_METHOD_CHECK_ACCOUNT_BLOCK = ++processID;
	public final static int ID_METHOD_REPORT_ACCOUNT = ++processID;
	public final static int ID_METHOD_GET_CREDIT_PACKAGE = ++processID;
	public final static int ID_METHOD_BUY_CREDIT_PACKAGE = ++processID;
	public final static int ID_METHOD_DELETE_ACCOUNT = ++processID;

	public final static int ID_METHOD_UPDATE_PROFILE_PICTURE = ++processID;
	public final static int ID_METHOD_UPDATE_PROFILE = ++processID;
	public final static int ID_METHOD_UPDATE_JOB_STATUS = ++processID;
	public final static int ID_METHOD_ADD_FAVOURITE = ++processID;
	public final static int ID_METHOD_CREATE_ADS = ++processID;
	public final static int ID_METHOD_GET_ALL_ADVERTISEMENT_LIST = ++processID;
	public final static int ID_METHOD_DELETE_ADVERTISEMENT = ++processID;

	private static int processPopupID=0;
	public final static int ID_POPUP_CONFIRM = ++processPopupID;
	public final static int ID_POPUP_CONFIRM_EXIT = ++processPopupID;
	
	public static final int ID_POPUP_CONFIRM_NO = ++processPopupID;
	public static final int ID_POPUP_CONFIRM_YES= ++processPopupID;
	public static final int ID_POPUP_CONFIRM_OK= ++processPopupID;
	public static final int ID_POPUP_LOG_IN= ++processPopupID;
	public static final int ID_POPUP_CHAT_DETAIL= ++processPopupID;
	public static final int ID_POPUP_TAB_CHAT= ++processPopupID;
	public static final int ID_POPUP_CHAT_RECEIVED_MESSAGE= ++processPopupID;
	public static final int ID_POPUP_CHAT_UPDATE_MESSAGE= ++processPopupID;
	public static final int ID_POPUP_SPEAK= ++processPopupID;
	public static final int ID_POPUP_SAVE_IMAGE= ++processPopupID;
	public static final int ID_POPUP_CALLBACK_ADAPTER = ++processPopupID;
	public static final int ID_POPUP_MENU_DELETE = ++processPopupID;
	public static final int ID_POPUP_ACTION_MEMBER_DELETE = ++processPopupID;
	public static final int ID_POPUP_ACTION_MEMBER_BLOCK = ++processPopupID;
	public static final int ID_POPUP_IS_READ_MESSAGE = ++processPopupID;

	public static final int ID_POPUP_CHOSE_PHOTO_LIBRARY = ++processPopupID;
	public static final int ID_POPUP_TAKE_PHOTO= ++processPopupID;
	public static final int ID_POPUP_CHOOSE_IMAGE= ++processPopupID;

	
	public static String getURL(){
		if(IS_PRODUCTION){
			return URL_PROD;
		}else{
			return URL_STAG;
		}
	}
	// Time delay
	public static final int animationTime = 2 * 100;
	public static final int DELAY_TEXT_CHANGE = 1 * 1000;
	public static final int SPLASH_DISPLAY_TIME_MILLISECONDS = 1 * 1000;
	public static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 1000;
	public static final int DEFAULT_SOCKET_TIMEOUT = 35 * 1000;

	//type
	public static final String TYPE_STATUS_ONLINE = "online";
	public static final String TYPE_STATUS_OFFLINE = "offline";
	public static final String TYPE_STATUS_BUSY = "busy";

	//type chat
	public static final String CHAT_TYPE_TEXT = "text";
	public static final String CHAT_TYPE_PHOTO = "photo";
	public static final String CHAT_TYPE_HI= "hi";


	//name fragment
	public static final String side_nav_fr_chat_detail = "chat detail";


	//BroadcastSignalR
	public static final int ACTION_RECEIVED_MESSAGE = 1;
	public static final int ACTION_UPDATE_MESSAGE = 2;
	public static final int ACTION_DELETE_MESSAGE = -1;
}
