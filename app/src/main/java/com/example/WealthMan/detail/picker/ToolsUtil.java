package com.example.WealthMan.detail.picker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.example.WealthMan.R;
import com.example.WealthMan.detail.picker.config.PickerConfig;
import com.example.WealthMan.detail.picker.data.Type;
import com.example.WealthMan.detail.picker.data.WheelCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/22.
 */

public class ToolsUtil extends DateFormatUtil {
    private static ToolsUtil toolsUtil;

    private ToolsUtil() {
    }

    public static ToolsUtil getToolsInstance() {
        if (toolsUtil == null) {
            return new ToolsUtil();
        } else {
            return toolsUtil;
        }
    }

    private static int notifyId = 0;

    public static boolean isLegalPhoneNum(String phoneNum) {
        //"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        } else {
            if (phoneNum.contains(" ")) {
                phoneNum = phoneNum.replace(" ", "");
            }
            return phoneNum.matches("[1]\\d{10}");
        }
    }

    public static boolean isLetterDigit(String str) {
        if (TextUtils.isEmpty(str)) return false;

        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }


    public static String convertTimeLongToString(long millis, String pattern) {
        Date date = new Date(millis);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String convertTimeLongToString(long millis) {
        return convertTimeLongToString(millis, "yyyy-MM-dd HH:mm");
    }



    /*public static String encryptByRC4(String aInput) {
        String aKey = "NuHjhg%&^fxF57cGnm";
        int[] iS = new int[256];
        byte[] iK = new byte[256];
        for (int i = 0; i < 256; i++)
            iS[i] = i;
        int j = 1;
        for (short i = 0; i < 256; i++) {
            iK[i] = (byte) aKey.charAt((i % aKey.length()));
        }
        j = 0;
        for (int i = 0; i < 255; i++) {
            j = (j + iS[i] + iK[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
        }
        int i = 0;
        j = 0;
        char[] iInputChar = aInput.toCharArray();
        char[] iOutputChar = new char[iInputChar.length];
        for (short x = 0; x < iInputChar.length; x++) {
            i = (i + 1) % 256;
            j = (j + iS[i]) % 256;
            int temp = iS[i];
            iS[i] = iS[j];
            iS[j] = temp;
            int t = (iS[i] + (iS[j] % 256)) % 256;
            int iY = iS[t];
            char iCY = (char) iY;
            iOutputChar[x] = (char) (iInputChar[x] ^ iCY);
        }
        return new String(iOutputChar);
    }*/



    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }

    public static boolean isValidJson(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }





    public static String saveBitmapToFile(Bitmap bitmap, String filePath, String fileName) {
        if (bitmap == null || TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName))
            return null;

        FileOutputStream outputStream = null;
        try {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                file.delete();
            }
            file.getParentFile().mkdirs();
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, outputStream);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    /**
     * 检测银联无卡支付输入的付款数额是否正确
     *
     * @param money 付款金额
     * @return 是否合法
     */
    public static boolean isValidMoneyValue(String money) {
        if (money.contains(".")) {
            money = money.substring(0, money.indexOf("."));
        }

        double doubleMoney = 0;
        try {
            doubleMoney = Integer.parseInt(money);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (doubleMoney < 1) return true;
        if (doubleMoney % 100 == 0) return false;
        if (money.matches("([\\d])\\1{2,}")) return false;
        char[] chars = money.toCharArray();
        if (chars.length > 2) {
            boolean valid = false;
            for (int i = 0; i < chars.length - 1; i++) {
                int value1 = Integer.parseInt(String.valueOf(chars[i]));
                int value2 = Integer.parseInt(String.valueOf(chars[i + 1]));
                if (value1 + 1 != value2) {
                    valid = true;
                    break;
                }
            }
            return valid;
        }
        return true;
    }




    public static String timeStampToString(long timeStamp) {
        long longTime = timeStamp * 1000;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sf.format(new Date(longTime));
    }

    public static PickerConfig buildReconciliationPickerConfig(Context context) {

        return buildReconciliationPickerConfig(context, Type.ALL);
    }

    public static long getFirstDayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0)
            dayOfWeek = 7;
        c.add(Calendar.DATE, -dayOfWeek + 1);
        return c.getTimeInMillis()/*sdf.format(c.getTime())*/;
    }

    public static String formatLongTime(long timeStamp, String pattern) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(c.getTime());

    }

    /**
     * string时间转换
     *
     * @param timeStamp
     * @param pattern
     * @return
     */
    public static String formatLongTime(String timeStamp, String pattern) {
        try {
            long time = Long.parseLong(timeStamp) * 1000;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(c.getTime());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
    }



    public static boolean isCurrentYear(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(timeStamp);
        int year = calendar.get(Calendar.YEAR);
        return currentYear == year;
    }



    public static Calendar calculateMonthTime(Calendar calendar, boolean isStart) {
        if (calendar == null) return null;

        if (isStart) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, days);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }
        return calendar;
    }

    public static String getWeekNumberStr(int weekNumber) {
        switch (weekNumber) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return "日";

        }
    }

    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isInstallRelativeApp(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String formatBankCardStr(String bankCardNum) {
        if (TextUtils.isEmpty(bankCardNum)) return bankCardNum;

        String regex = "(\\w{4})(.*)(\\w{4})";
//        if (bankCardNum.length() == 16) {
//            regex = "(.*)(\\w{4})";
//        } else if (bankCardNum.length() == 17) {
//            regex = "(.*)(\\w{5})";
//        } else if (bankCardNum.length() == 18) {
//            regex = "(.*)(\\w{6})";
//        } else {
//            regex = "(.*)(\\w{3})";
//        }
        Matcher m = Pattern.compile(regex).matcher(bankCardNum);
        if (m.find()) {
            String rep = m.group(2);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < rep.length(); i++) {
                builder.append("*");
            }

            String result = bankCardNum.replaceAll(rep, builder.toString());
            builder.setLength(0);
            for (int i = 0; i < result.length(); i++) {
                if (i % 4 == 0 && i > 0) {
                    builder.append(" ");
                }
                builder.append(result.charAt(i));
            }
            return builder.toString();
        }
        return bankCardNum;
    }

    public static boolean isNumeric(String str, int length) {
        if (TextUtils.isEmpty(str)) return false;
        String regex;
        if (length < 0) {
            regex = "[0-9]*";
        } else {
            regex = "\\d{" + length + "}";
        }
        return str.matches(regex);
    }

    public static String md5Encrypt(String encryptText) {
        if (TextUtils.isEmpty(encryptText)) return null;

        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(encryptText.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String buildReportTime(long timeStamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeStamp * 1000);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        if (year == y && month == m && day == d) {
            return new SimpleDateFormat("HH:mm").format(c.getTime());
        } else if (year == y && month == m && day + 1 == d) {
            return "昨天 " + new SimpleDateFormat("HH:mm").format(c.getTime());
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTime());
        }
    }





    public static String getDownLoadPath(String path) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || TextUtils.isEmpty(path))
            return null;
        File file = new File(path);
        if (!file.exists()) {
            boolean apkDir = file.mkdirs();
            if (apkDir) {
                return path;
            } else {
                return null;
            }
        } else {
            return path;
        }
    }




    public static PickerConfig buildReconciliationPickerConfig(Context context, Type type) {
        long sevenDay = 1000 * 60 * 60 * 24L * 7;
        PickerConfig config = new PickerConfig();
        config.cyclic = true;
        long todayMillis = ToolsUtil.getTodayMillis(true);
        config.mCurrentCalendar = new WheelCalendar(todayMillis);
        config.mMaxCalendar = new WheelCalendar(System.currentTimeMillis());
        long minMillis = todayMillis - sevenDay;
        config.mMinCalendar = new WheelCalendar(minMillis);
        config.mType = type;
        config.mWheelTVSize = 18;
        config.mWheelTVNormalColor = ContextCompat.getColor(context, R.color.colorCommonGrey);
        config.mWheelTVSelectorColor = ContextCompat.getColor(context, R.color.colorPrimary);
        config.mYear = "年";
        config.mMonth = "月";
        config.mDay = "日";
        config.mHour = "时";
        config.mMinute = "分";
        return config;
    }



    public static String getSDCardInfo() {
        if (!isExternalStorageReadable()) return null;

        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        DecimalFormat formatter = new DecimalFormat("#0.00");
        double blockSize = statFs.getBlockSize();
        String totalSize = formatter.format(statFs.getBlockCount() * blockSize / 1024 / 1024 / 1024);
        String availableSize = formatter.format(statFs.getFreeBlocks() * blockSize / 1024 / 1024 / 1024);
        return new StringBuffer().append(availableSize).append("GB/").append(totalSize).append("GB").toString();
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getRAMInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        double totalSize = memoryInfo.totalMem / 1024 / 1024 / 1024;
        double availableSize = memoryInfo.availMem / 1024 / 1024 / 1024;
        return new StringBuffer().append(availableSize).append("GB/").append(totalSize).append("GB").toString();
    }







    public static String getFileSize(File file) {
        if (file == null || !file.exists()) return null;
        float size = file.length() * 1.0f / 1024;
        if (size >= 1024) {
            return size / 1024 + "mb";
        }
        return size + "kb";
    }

    public static Toast toast;




    //时间戳转字符串
    public static String getTime(String timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }



    public static boolean isIntentExisting(Intent intent, Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 解析数组
     *
     * @param id 需要解析的数组id
     * @return 返回arr中定义的数组
     */
    public ArrayList<String> getNeedArrry(Context context, int id) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Resources resources = context.getResources();
            String[] stringArray = resources.getStringArray(id);
            for (int i = 0; i < stringArray.length; i++) {
                arrayList.add(stringArray[i]);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return arrayList;
    }




    /**
     * 获取屏幕高度
     */
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
}
