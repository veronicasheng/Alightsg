package com.alwinyong.alightsg;


import android.content.SharedPreferences;

public class WakerSettings {
    public static final String SELECTED_MRT_KEY = "sg.mrtwaker.SelectedMrt";
    public static final String SELECTED_MRT_POS_KEY = "SELECTED_MRT_POS_KEY";
    private static final long DEFAULT_POINT_RADIUS = 800L;
    private static final String POINT_LATITUDE_KEY = "POINT_LATITUDE_KEY";
    private static final String POINT_LONGITUDE_KEY = "POINT_LONGITUDE_KEY";
    private static final String RADIUS_SETTING_KEY = "RADIUS_SETTING_KEY";
    private SharedPreferences sharedPreferences;

    public WakerSettings(SharedPreferences paramSharedPreferences) {
        this.sharedPreferences = paramSharedPreferences;
    }

    public float getLatitude() {
        return this.sharedPreferences.getFloat("POINT_LATITUDE_KEY", 0.0F);
    }

    public float getLongitude() {
        return this.sharedPreferences.getFloat("POINT_LONGITUDE_KEY", 0.0F);
    }

    public int getMrtPosition() {
        return this.sharedPreferences.getInt("SELECTED_MRT_POS_KEY", 0);
    }

    public long getRadius() {
        return this.sharedPreferences.getLong("RADIUS_SETTING_KEY", 800L);
    }

    public void saveLatitude(float paramFloat) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putFloat("POINT_LATITUDE_KEY", paramFloat);
        localEditor.commit();
    }

    public void saveLongitude(float paramFloat) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putFloat("POINT_LONGITUDE_KEY", paramFloat);
        localEditor.commit();
    }

    public void saveMrtPosition(int paramInt) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putInt("SELECTED_MRT_POS_KEY", paramInt);
        localEditor.commit();
    }

    public void saveRadius(long paramLong) {
        SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
        localEditor.putLong("RADIUS_SETTING_KEY", paramLong);
        localEditor.commit();
    }
}