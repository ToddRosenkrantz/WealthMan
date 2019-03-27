package com.example.WealthMan.detail.picker.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.WealthMan.detail.picker.data.Type;
import com.example.WealthMan.detail.picker.data.WheelCalendar;


/**
 * Created by jzxiang on 16/5/15.
 */
public class PickerConfig implements Parcelable {

    public Type mType = DefaultConfig.TYPE;
    public int mThemeColor = DefaultConfig.COLOR;

    public String mCancelString = DefaultConfig.CANCEL;
    public String mSureString = DefaultConfig.SURE;
    public String mTitleString = DefaultConfig.TITLE;
    public int mToolBarTVColor = DefaultConfig.TOOLBAR_TV_COLOR;

    public int mWheelTVNormalColor = DefaultConfig.TV_NORMAL_COLOR;
    public int mWheelTVSelectorColor = DefaultConfig.TV_SELECTOR_COLOR;
    public int mWheelTVSize = DefaultConfig.TV_SIZE;
    public boolean cyclic = DefaultConfig.CYCLIC;

    public String mYear = DefaultConfig.YEAR;
    public String mMonth = DefaultConfig.MONTH;
    public String mDay = DefaultConfig.DAY;
    public String mHour = DefaultConfig.HOUR;
    public String mMinute = DefaultConfig.MINUTE;

    /**
     * The min timeMillseconds
     */
    public WheelCalendar mMinCalendar = new WheelCalendar(0);

    /**
     * The max timeMillseconds
     */
    public WheelCalendar mMaxCalendar = new WheelCalendar(0);

    /**
     * The default selector timeMillseconds
     */
    public WheelCalendar mCurrentCalendar = new WheelCalendar(System.currentTimeMillis());

    public PickerConfig() {
    }

    protected PickerConfig(Parcel in) {
        mThemeColor = in.readInt();
        mCancelString = in.readString();
        mSureString = in.readString();
        mTitleString = in.readString();
        mToolBarTVColor = in.readInt();
        mWheelTVNormalColor = in.readInt();
        mWheelTVSelectorColor = in.readInt();
        mWheelTVSize = in.readInt();
        cyclic = in.readByte() != 0;
        mYear = in.readString();
        mMonth = in.readString();
        mDay = in.readString();
        mHour = in.readString();
        mMinute = in.readString();
        mMinCalendar = in.readParcelable(WheelCalendar.class.getClassLoader());
        mMaxCalendar = in.readParcelable(WheelCalendar.class.getClassLoader());
        mCurrentCalendar = in.readParcelable(WheelCalendar.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mThemeColor);
        dest.writeString(mCancelString);
        dest.writeString(mSureString);
        dest.writeString(mTitleString);
        dest.writeInt(mToolBarTVColor);
        dest.writeInt(mWheelTVNormalColor);
        dest.writeInt(mWheelTVSelectorColor);
        dest.writeInt(mWheelTVSize);
        dest.writeByte((byte) (cyclic ? 1 : 0));
        dest.writeString(mYear);
        dest.writeString(mMonth);
        dest.writeString(mDay);
        dest.writeString(mHour);
        dest.writeString(mMinute);
        dest.writeParcelable(mMinCalendar, flags);
        dest.writeParcelable(mMaxCalendar, flags);
        dest.writeParcelable(mCurrentCalendar, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PickerConfig> CREATOR = new Creator<PickerConfig>() {
        @Override
        public PickerConfig createFromParcel(Parcel in) {
            return new PickerConfig(in);
        }

        @Override
        public PickerConfig[] newArray(int size) {
            return new PickerConfig[size];
        }
    };
}
