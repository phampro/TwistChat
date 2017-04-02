package com.hoangsong.zumechat.untils;

/**
 * Created by Tang on 14/09/2016.
 */

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.mukesh.countrypicker.Constants;
import com.mukesh.countrypicker.models.Country;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by mukesh on 25/04/16.
 */
public class UtilCountry {
    public static List<Country> allCountriesList = new ArrayList<>();


    public UtilCountry() {
    }

    public static Currency getCurrencyCode(String countryCode) {
        try {
            return Currency.getInstance(new Locale("en", countryCode));
        } catch (Exception ignored) {
        }
        return null;
    }

    public static List<Country> getAllCountries() {
            try {
                String allCountriesCode = readEncodedJsonString();
                JSONArray countryArray = new JSONArray(allCountriesCode);
                int length = countryArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = countryArray.getJSONObject(i);
                    String countryDialCode = jsonObject.getString("dial_code");
                    String countryCode = jsonObject.getString("code");
                    Country country = new Country();
                    country.setCode(countryCode);
                    country.setDialCode(countryDialCode);
                    allCountriesList.add(country);
                }
                return allCountriesList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    private static String readEncodedJsonString() throws java.io.IOException {
        byte[] data = Base64.decode(Constants.ENCODED_COUNTRY_CODE, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    public static int getFlagResId(Context context, String drawable) {
        try {
            return context.getResources()
                    .getIdentifier("flag_" + drawable.toLowerCase(Locale.ENGLISH), "drawable",
                            context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Country getCountry(String nameOrCodeOrDialCode) {
        if (!nameOrCodeOrDialCode.equalsIgnoreCase("")) {
            allCountriesList = getAllCountries();
            int size = allCountriesList.size();
            for (int i = 0; i < size; i++) {
                Country current = allCountriesList.get(i);
                if(current.getDialCode().equalsIgnoreCase(nameOrCodeOrDialCode) || current.getCode().equalsIgnoreCase(nameOrCodeOrDialCode)){
                    return current;
                }
            }
        }
        return null;
    }

    public Country getUserCountryInfo(Context context) {
        List<Country> allCountriesList = new ArrayList<>();
        allCountriesList = getAllCountries();
        String countryIsoCode;
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!(telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT)) {
            countryIsoCode = telephonyManager.getSimCountryIso();
            for (int i = 0; i < allCountriesList.size(); i++) {
                Country country = allCountriesList.get(i);
                if (country.getCode().equalsIgnoreCase(countryIsoCode)) {
                    country.setFlag(getFlagResId(context, country.getCode()));
                    return country;
                }
            }
        }
        return afghanistan();
    }

    private Country afghanistan() {
        Country country = new Country();
        country.setCode("AF");
        country.setDialCode("+93");
        country.setFlag(com.mukesh.countrypicker.R.drawable.flag_af);
        return country;
    }
}