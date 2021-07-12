/*
 * ownCloud Android client application
 *
 * @author David A. Velasco
 * @author David González Verdugo
 * @author Shashvat Kedia
 * @author Juan Carlos Garrote Gascón
 *
 * Copyright (C) 2020 ownCloud GmbH.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.db;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;

import com.owncloud.android.R;
import com.owncloud.android.authentication.AccountUtils;
import com.owncloud.android.files.services.FileUploader;
import com.owncloud.android.ui.activity.BiometricActivity;
import com.owncloud.android.utils.FileStorageUtils;

import java.io.File;

/**
 * Helper to simplify reading of Preferences all around the app
 */
public abstract class PreferenceManager {
    /**
     * Constant to access value of last path selected by the user to upload a file shared from other app.
     * Value handled by the app without direct access in the UI.
     */
    private static final String AUTO_PREF__LAST_UPLOAD_PATH = "last_upload_path";
    private static final String AUTO_PREF__SORT_ORDER_FILE_DISP = "sortOrderFileDisp";
    private static final String AUTO_PREF__SORT_ASCENDING_FILE_DISP = "sortAscendingFileDisp";
    private static final String AUTO_PREF__SORT_ORDER_UPLOAD = "sortOrderUpload";
    private static final String AUTO_PREF__SORT_ASCENDING_UPLOAD = "sortAscendingUpload";

    // Legacy preferences - done in version 2.18
    public static final String PREF__LEGACY_CLICK_DEV_MENU = "clickDeveloperMenu";
    public static final String PREF__LEGACY_CAMERA_PICTURE_UPLOADS_ENABLED = "camera_picture_uploads";
    public static final String PREF__LEGACY_CAMERA_VIDEO_UPLOADS_ENABLED = "camera_video_uploads";
    public static final String PREF__LEGACY_CAMERA_PICTURE_UPLOADS_WIFI_ONLY = "camera_picture_uploads_on_wifi";
    public static final String PREF__LEGACY_CAMERA_VIDEO_UPLOADS_WIFI_ONLY = "camera_video_uploads_on_wifi";
    public static final String PREF__LEGACY_CAMERA_PICTURE_UPLOADS_PATH = "camera_picture_uploads_path";
    public static final String PREF__LEGACY_CAMERA_VIDEO_UPLOADS_PATH = "camera_video_uploads_path";
    public static final String PREF__LEGACY_CAMERA_UPLOADS_BEHAVIOUR = "camera_uploads_behaviour";
    public static final String PREF__LEGACY_CAMERA_UPLOADS_SOURCE = "camera_uploads_source_path";
    public static final String PREF__LEGACY_CAMERA_UPLOADS_ACCOUNT_NAME = "camera_uploads_account_name";

    public static final String PREF__CAMERA_PICTURE_UPLOADS_ENABLED = "enable_picture_uploads";
    public static final String PREF__CAMERA_VIDEO_UPLOADS_ENABLED = "enable_video_uploads";
    public static final String PREF__CAMERA_PICTURE_UPLOADS_WIFI_ONLY = "picture_uploads_on_wifi";
    public static final String PREF__CAMERA_VIDEO_UPLOADS_WIFI_ONLY = "video_uploads_on_wifi";
    public static final String PREF__CAMERA_PICTURE_UPLOADS_PATH = "picture_uploads_path";
    public static final String PREF__CAMERA_VIDEO_UPLOADS_PATH = "video_uploads_path";
    public static final String PREF__CAMERA_PICTURE_UPLOADS_BEHAVIOUR = "picture_uploads_behaviour";
    public static final String PREF__CAMERA_PICTURE_UPLOADS_SOURCE = "picture_uploads_source_path";
    public static final String PREF__CAMERA_VIDEO_UPLOADS_BEHAVIOUR = "video_uploads_behaviour";
    public static final String PREF__CAMERA_VIDEO_UPLOADS_SOURCE = "video_uploads_source_path";
    public static final String PREF__CAMERA_PICTURE_UPLOADS_ACCOUNT_NAME = "picture_uploads_account_name";
    public static final String PREF__CAMERA_VIDEO_UPLOADS_ACCOUNT_NAME = "video_uploads_account_name";

    public static final String PREF__CAMERA_UPLOADS_DEFAULT_PATH = "/CameraUpload";

    public static final String PREF__LEGACY_FINGERPRINT = "set_fingerprint";

    public static void migrateFingerprintToBiometricKey(Context context) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);

        // Check if legacy fingerprint key exists, delete it and migrate its value to the new key
        if (sharedPref.contains(PREF__LEGACY_FINGERPRINT)) {
            boolean currentFingerprintValue = sharedPref.getBoolean(PREF__LEGACY_FINGERPRINT, false);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(PREF__LEGACY_FINGERPRINT);
            editor.putBoolean(BiometricActivity.PREFERENCE_SET_BIOMETRIC, currentFingerprintValue);
            editor.apply();
        }
    }

    public static void deleteOldSettingsPreferences(Context context) {
        SharedPreferences sharedPref = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (sharedPref.contains(PREF__LEGACY_CLICK_DEV_MENU)) {
            editor.remove(PREF__LEGACY_CLICK_DEV_MENU);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_PICTURE_UPLOADS_ENABLED)) {
            editor.remove(PREF__LEGACY_CAMERA_PICTURE_UPLOADS_ENABLED);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_VIDEO_UPLOADS_ENABLED)) {
            editor.remove(PREF__LEGACY_CAMERA_VIDEO_UPLOADS_ENABLED);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_PICTURE_UPLOADS_WIFI_ONLY)) {
            editor.remove(PREF__LEGACY_CAMERA_PICTURE_UPLOADS_WIFI_ONLY);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_VIDEO_UPLOADS_WIFI_ONLY)) {
            editor.remove(PREF__LEGACY_CAMERA_VIDEO_UPLOADS_WIFI_ONLY);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_PICTURE_UPLOADS_PATH)) {
            editor.remove(PREF__LEGACY_CAMERA_PICTURE_UPLOADS_PATH);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_VIDEO_UPLOADS_PATH)) {
            editor.remove(PREF__LEGACY_CAMERA_VIDEO_UPLOADS_PATH);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_UPLOADS_BEHAVIOUR)) {
            editor.remove(PREF__LEGACY_CAMERA_UPLOADS_BEHAVIOUR);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_UPLOADS_SOURCE)) {
            editor.remove(PREF__LEGACY_CAMERA_UPLOADS_SOURCE);
        }
        if (sharedPref.contains(PREF__LEGACY_CAMERA_UPLOADS_ACCOUNT_NAME)) {
            editor.remove(PREF__LEGACY_CAMERA_UPLOADS_ACCOUNT_NAME);
        }
        editor.apply();
    }

    public static boolean cameraPictureUploadEnabled(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(PREF__CAMERA_PICTURE_UPLOADS_ENABLED, false);
    }

    public static boolean cameraVideoUploadEnabled(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(PREF__CAMERA_VIDEO_UPLOADS_ENABLED, false);
    }

    public static boolean cameraPictureUploadViaWiFiOnly(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(PREF__CAMERA_PICTURE_UPLOADS_WIFI_ONLY, false);
    }

    public static boolean cameraVideoUploadViaWiFiOnly(Context context) {
        return getDefaultSharedPreferences(context).getBoolean(PREF__CAMERA_VIDEO_UPLOADS_WIFI_ONLY, false);
    }

    public static CameraUploadsConfiguration getCameraUploadsConfiguration(Context context) {
        CameraUploadsConfiguration result = new CameraUploadsConfiguration();
        SharedPreferences prefs = getDefaultSharedPreferences(context);
        result.setEnabledForPictures(
                prefs.getBoolean(PREF__CAMERA_PICTURE_UPLOADS_ENABLED, false)
        );
        result.setEnabledForVideos(
                prefs.getBoolean(PREF__CAMERA_VIDEO_UPLOADS_ENABLED, false)
        );
        result.setWifiOnlyForPictures(
                prefs.getBoolean(PREF__CAMERA_PICTURE_UPLOADS_WIFI_ONLY, false)
        );
        result.setWifiOnlyForVideos(
                prefs.getBoolean(PREF__CAMERA_VIDEO_UPLOADS_WIFI_ONLY, false)
        );
        result.setUploadAccountNameForPictures(
                prefs.getString(PREF__CAMERA_PICTURE_UPLOADS_ACCOUNT_NAME, "" )
        );
        result.setUploadAccountNameForVideos(
                prefs.getString(PREF__CAMERA_VIDEO_UPLOADS_ACCOUNT_NAME, "" )
        );
        String uploadPath = prefs.getString(
                PREF__CAMERA_PICTURE_UPLOADS_PATH,
                PREF__CAMERA_UPLOADS_DEFAULT_PATH + File.separator
        );
        result.setUploadPathForPictures(
                uploadPath.endsWith(File.separator) ? uploadPath : uploadPath + File.separator
        );
        uploadPath = prefs.getString(
                PREF__CAMERA_VIDEO_UPLOADS_PATH,
                PREF__CAMERA_UPLOADS_DEFAULT_PATH + File.separator
        );
        result.setUploadPathForVideos(
                uploadPath.endsWith(File.separator) ? uploadPath : uploadPath + File.separator
        );
        result.setBehaviourAfterUploadPictures(
                prefs.getString(
                        PREF__CAMERA_PICTURE_UPLOADS_BEHAVIOUR,
                        context.getResources().getStringArray(R.array.pref_behaviour_entryValues)[0]
                )
        );
        result.setBehaviourAfterUploadVideos(
                prefs.getString(
                        PREF__CAMERA_VIDEO_UPLOADS_BEHAVIOUR,
                        context.getResources().getStringArray(R.array.pref_behaviour_entryValues)[0]
                )
        );
        result.setSourcePathPictures(
                prefs.getString(
                        PREF__CAMERA_PICTURE_UPLOADS_SOURCE,
                        CameraUploadsConfiguration.getDefaultSourcePath()
                )
        );
        result.setSourcePathVideos(
                prefs.getString(
                        PREF__CAMERA_VIDEO_UPLOADS_SOURCE,
                        CameraUploadsConfiguration.getDefaultSourcePath()
                )
        );
        return result;
    }

    /**
     * Gets the path where the user selected to do the last upload of a file shared from other app.
     *
     * @param context Caller {@link Context}, used to access to shared preferences manager.
     * @return path     Absolute path to a folder, as previously stored by {@link #setLastUploadPath(String, Context)},
     * or empty String if never saved before.
     */
    public static String getLastUploadPath(Context context) {
        return getDefaultSharedPreferences(context).getString(AUTO_PREF__LAST_UPLOAD_PATH, "");
    }

    /**
     * Saves the path where the user selected to do the last upload of a file shared from other app.
     *
     * @param path    Absolute path to a folder.
     * @param context Caller {@link Context}, used to access to shared preferences manager.
     */
    public static void setLastUploadPath(String path, Context context) {
        saveStringPreference(AUTO_PREF__LAST_UPLOAD_PATH, path, context);
    }

    /**
     * Gets the sort order which the user has set last.
     *
     * @param context Caller {@link Context}, used to access to shared preferences manager.
     * @return sort order     the sort order, default is {@link FileStorageUtils#SORT_NAME} (sort by name)
     */
    public static int getSortOrder(Context context, int flag) {
        if (flag == FileStorageUtils.FILE_DISPLAY_SORT) {
            return getDefaultSharedPreferences(context)
                    .getInt(AUTO_PREF__SORT_ORDER_FILE_DISP, FileStorageUtils.SORT_NAME);
        } else {
            return getDefaultSharedPreferences(context)
                    .getInt(AUTO_PREF__SORT_ORDER_UPLOAD, FileStorageUtils.SORT_DATE);
        }
    }

    /**
     * Save the sort order which the user has set last.
     *
     * @param order   the sort order
     * @param context Caller {@link Context}, used to access to shared preferences manager.
     */
    public static void setSortOrder(int order, Context context, int flag) {
        if (flag == FileStorageUtils.FILE_DISPLAY_SORT) {
            saveIntPreference(AUTO_PREF__SORT_ORDER_FILE_DISP, order, context);
        } else {
            saveIntPreference(AUTO_PREF__SORT_ORDER_UPLOAD, order, context);
        }
    }

    /**
     * Gets the ascending order flag which the user has set last.
     *
     * @param context Caller {@link Context}, used to access to shared preferences manager.
     * @return ascending order     the ascending order, default is true
     */
    public static boolean getSortAscending(Context context, int flag) {
        if (flag == FileStorageUtils.FILE_DISPLAY_SORT) {
            return getDefaultSharedPreferences(context)
                    .getBoolean(AUTO_PREF__SORT_ASCENDING_FILE_DISP, true);
        } else {
            return getDefaultSharedPreferences(context)
                    .getBoolean(AUTO_PREF__SORT_ASCENDING_UPLOAD, true);
        }
    }

    /**
     * Saves the ascending order flag which the user has set last.
     *
     * @param ascending flag if sorting is ascending or descending
     * @param context   Caller {@link Context}, used to access to shared preferences manager.
     */
    public static void setSortAscending(boolean ascending, Context context, int flag) {
        if (flag == FileStorageUtils.FILE_DISPLAY_SORT) {
            saveBooleanPreference(AUTO_PREF__SORT_ASCENDING_FILE_DISP, ascending, context);
        } else {
            saveBooleanPreference(AUTO_PREF__SORT_ASCENDING_UPLOAD, ascending, context);
        }
    }

    private static void saveBooleanPreference(String key, boolean value, Context context) {
        SharedPreferences.Editor appPreferences = getDefaultSharedPreferences(context.getApplicationContext()).edit();
        appPreferences.putBoolean(key, value);
        appPreferences.apply();
    }

    private static void saveStringPreference(String key, String value, Context context) {
        SharedPreferences.Editor appPreferences = getDefaultSharedPreferences(context.getApplicationContext()).edit();
        appPreferences.putString(key, value);
        appPreferences.apply();
    }

    private static void saveIntPreference(String key, int value, Context context) {
        SharedPreferences.Editor appPreferences = getDefaultSharedPreferences(context.getApplicationContext()).edit();
        appPreferences.putInt(key, value);
        appPreferences.apply();
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    /**
     * Aggregates preferences related to camera uploads in a single object.
     */
    public static class CameraUploadsConfiguration {

        private boolean mEnabledForPictures;
        private boolean mEnabledForVideos;
        private boolean mWifiOnlyForPictures;
        private boolean mWifiOnlyForVideos;
        private String mUploadAccountNameForPictures;
        private String mUploadAccountNameForVideos;
        private String mUploadPathForPictures;
        private String mUploadPathForVideos;
        private String mBehaviourAfterUploadPictures;
        private String mBehaviourAfterUploadVideos;
        private String mSourcePathPictures;
        private String mSourcePathVideos;

        public static String getDefaultSourcePath() {
            return FileStorageUtils.getDefaultCameraSourcePath();
        }

        public boolean isEnabledForPictures() {
            return mEnabledForPictures;
        }

        public void setEnabledForPictures(boolean uploadPictures) {
            mEnabledForPictures = uploadPictures;
        }

        public boolean isEnabledForVideos() {
            return mEnabledForVideos;
        }

        public void setEnabledForVideos(boolean uploadVideos) {
            mEnabledForVideos = uploadVideos;
        }

        public boolean isWifiOnlyForPictures() {
            return mWifiOnlyForPictures;
        }

        public void setWifiOnlyForPictures(boolean wifiOnlyForPictures) {
            mWifiOnlyForPictures = wifiOnlyForPictures;
        }

        public boolean isWifiOnlyForVideos() {
            return mWifiOnlyForVideos;
        }

        public void setWifiOnlyForVideos(boolean wifiOnlyForVideos) {
            mWifiOnlyForVideos = wifiOnlyForVideos;
        }

        public String getUploadAccountNameForPictures() {
            return mUploadAccountNameForPictures;
        }

        public String getUploadAccountNameForVideos() {
            return mUploadAccountNameForVideos;
        }

        public void setUploadAccountNameForPictures(String uploadAccountName) {
            mUploadAccountNameForPictures = uploadAccountName;
        }

        public void setUploadAccountNameForVideos(String uploadAccountName) {
            mUploadAccountNameForVideos = uploadAccountName;
        }

        public String getUploadPathForPictures() {
            return mUploadPathForPictures;
        }

        public void setUploadPathForPictures(String uploadPathForPictures) {
            mUploadPathForPictures = uploadPathForPictures;
        }

        public String getUploadPathForVideos() {
            return mUploadPathForVideos;
        }

        public void setUploadPathForVideos(String uploadPathForVideos) {
            mUploadPathForVideos = uploadPathForVideos;
        }

        public int getBehaviourAfterUploadPictures() {
            if (mBehaviourAfterUploadPictures.equalsIgnoreCase("MOVE")) {
                return FileUploader.LOCAL_BEHAVIOUR_MOVE;
            }
            return FileUploader.LOCAL_BEHAVIOUR_FORGET; // "NOTHING"
        }

        public void setBehaviourAfterUploadPictures(String behaviourAfterUploadPictures) {
            mBehaviourAfterUploadPictures = behaviourAfterUploadPictures;
        }

        public int getBehaviourAfterUploadVideos() {
            if (mBehaviourAfterUploadVideos.equalsIgnoreCase("MOVE")) {
                return FileUploader.LOCAL_BEHAVIOUR_MOVE;
            }
            return FileUploader.LOCAL_BEHAVIOUR_FORGET; // "NOTHING"
        }

        public void setBehaviourAfterUploadVideos(String behaviourAfterUploadVideos) {
            mBehaviourAfterUploadVideos = behaviourAfterUploadVideos;
        }

        public String getSourcePathPictures() {
            return mSourcePathPictures;
        }

        public void setSourcePathPictures(String sourcePathPictures) {
            mSourcePathPictures = sourcePathPictures;
        }

        public String getSourcePathVideos() {
            return mSourcePathVideos;
        }

        public void setSourcePathVideos(String sourcePathVideos) {
            mSourcePathVideos = sourcePathVideos;
        }
    }
}
