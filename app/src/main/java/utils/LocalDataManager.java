package utils;

import android.content.Context;

import com.example.ecommerce.Models.DriverAccount;
import com.example.ecommerce.Models.User;

public class LocalDataManager {
    private static final String PREF_FIRST_LOGIN_USER = "PREF_FIRST_LOGIN_USER";
    private static final String PREF_FIRST_LOGIN_USERPHONE = "PREF_FIRST_LOGIN_USERPHONE";
    private static final String PREF_FIRST_LOGIN_USERNAME = "PREF_FIRST_LOGIN_USERNAME";
    private static final String PREF_FIRST_LOGIN_DRIVER = "PREF_FIRST_LOGIN_DRIVER";

    private static final String PREF_FIRST_LOGIN_DRIVERKEY = "PREF_FIRST_LOGIN_DRIVERKEY";
    private static LocalDataManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new LocalDataManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static LocalDataManager getInstance(){
        if(instance == null){
            instance = new LocalDataManager();
        }
        return instance;
    }

    public static void setFirstTimeLoginDriver(boolean isFirst){
        LocalDataManager.getInstance().mySharedPreferences.putBooleanValue(PREF_FIRST_LOGIN_DRIVER,isFirst);
    }

    public static boolean getFirstTimeLoginDriver(){
        return LocalDataManager.getInstance().mySharedPreferences.getBooleanValue(PREF_FIRST_LOGIN_DRIVER);
    }

    public static void setFirstTimeLoginUser(boolean isFirst){
        LocalDataManager.getInstance().mySharedPreferences.putBooleanValue(PREF_FIRST_LOGIN_USER,isFirst);
    }

    public static boolean getFirstTimeLoginUser(){
        return LocalDataManager.getInstance().mySharedPreferences.getBooleanValue(PREF_FIRST_LOGIN_USER);
    }

    public static void setUserLoginInfoForBiometric(User user){
        LocalDataManager.getInstance().mySharedPreferences.putStringValue(PREF_FIRST_LOGIN_USERPHONE,user.getUserPhone());
        LocalDataManager.getInstance().mySharedPreferences.putStringValue(PREF_FIRST_LOGIN_USERNAME,user.getUsername());
    }

    public static User getUserLoginInfoForBiometric(){
        User user = new User();
        user.setUserPhone(LocalDataManager.getInstance().mySharedPreferences.getStringValue(PREF_FIRST_LOGIN_USERPHONE));
        user.setUsername(LocalDataManager.getInstance().mySharedPreferences.getStringValue(PREF_FIRST_LOGIN_USERNAME));
        return user;
    }
    public static void setDriverLoginInfoForBiometric(String driverKey){
        LocalDataManager.getInstance().mySharedPreferences.putStringValue(PREF_FIRST_LOGIN_DRIVERKEY,driverKey);
    }

    public static String getDriverLoginInfoForBiometric(){
        return LocalDataManager.getInstance().mySharedPreferences.getStringValue(PREF_FIRST_LOGIN_DRIVERKEY);
    }
}
