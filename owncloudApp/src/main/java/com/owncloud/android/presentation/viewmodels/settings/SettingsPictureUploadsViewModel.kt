/**
 * ownCloud Android client application
 *
 * @author Juan Carlos Garrote Gascón
 *
 * Copyright (C) 2021 ownCloud GmbH.
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

package com.owncloud.android.presentation.viewmodels.settings

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.owncloud.android.data.preferences.datasources.SharedPreferencesProvider
import com.owncloud.android.datamodel.OCFile
import com.owncloud.android.db.PreferenceManager.CameraUploadsConfiguration
import com.owncloud.android.db.PreferenceManager.PREF__CAMERA_PICTURE_UPLOADS_ACCOUNT_NAME
import com.owncloud.android.db.PreferenceManager.PREF__CAMERA_PICTURE_UPLOADS_ENABLED
import com.owncloud.android.db.PreferenceManager.PREF__CAMERA_PICTURE_UPLOADS_PATH
import com.owncloud.android.db.PreferenceManager.PREF__CAMERA_PICTURE_UPLOADS_SOURCE
import com.owncloud.android.db.PreferenceManager.PREF__CAMERA_UPLOADS_DEFAULT_PATH
import com.owncloud.android.providers.AccountProvider
import com.owncloud.android.providers.CameraUploadsHandlerProvider
import com.owncloud.android.ui.activity.LocalFolderPickerActivity
import com.owncloud.android.ui.activity.UploadPathActivity
import java.io.File

class SettingsPictureUploadsViewModel(
    private val preferencesProvider: SharedPreferencesProvider,
    private val cameraUploadsHandlerProvider: CameraUploadsHandlerProvider,
    private val accountProvider: AccountProvider
) : ViewModel() {

    fun isPictureUploadEnabled() =
        preferencesProvider.getBoolean(PREF__CAMERA_PICTURE_UPLOADS_ENABLED, false)

    fun setEnablePictureUpload(value: Boolean) {
        preferencesProvider.putBoolean(PREF__CAMERA_PICTURE_UPLOADS_ENABLED, value)

        if (value) {
            // Use current account as default. It should never be null. If no accounts are attached, picture uploads are hidden
            accountProvider.getCurrentOwnCloudAccount()?.name?.let { name ->
                preferencesProvider.putString(
                    key = PREF__CAMERA_PICTURE_UPLOADS_ACCOUNT_NAME,
                    value = name
                )
            }
        } else {
            // Reset fields after disabling the feature
            preferencesProvider.removePreference(key = PREF__CAMERA_PICTURE_UPLOADS_PATH)
            preferencesProvider.removePreference(key = PREF__CAMERA_PICTURE_UPLOADS_ACCOUNT_NAME)
        }
    }

    fun updatePicturesLastSync() = cameraUploadsHandlerProvider.updatePicturesLastSync(0)

    fun getPictureUploadsAccount() = preferencesProvider.getString(
        key = PREF__CAMERA_PICTURE_UPLOADS_ACCOUNT_NAME,
        defaultValue = null
    )

    fun getLoggedAccountNames(): Array<String> = accountProvider.getLoggedAccounts().map { it.name }.toTypedArray()

    fun getPictureUploadsPath() = preferencesProvider.getString(
        key = PREF__CAMERA_PICTURE_UPLOADS_PATH,
        defaultValue = PREF__CAMERA_UPLOADS_DEFAULT_PATH
    )

    fun getPictureUploadsSourcePath() = preferencesProvider.getString(
        key = PREF__CAMERA_PICTURE_UPLOADS_SOURCE,
        defaultValue = CameraUploadsConfiguration.getDefaultSourcePath()
    )

    fun handleSelectPictureUploadsPath(data: Intent?) {
        val folderToUpload = data?.getParcelableExtra<OCFile>(UploadPathActivity.EXTRA_FOLDER)
        folderToUpload?.remotePath?.let {
            preferencesProvider.putString(
                key = PREF__CAMERA_PICTURE_UPLOADS_PATH,
                value = it
            )
        }
    }

    fun handleSelectPictureUploadsSourcePath(data: Intent?) {
        // If the source path has changed, update camera uploads last sync
        var previousSourcePath = preferencesProvider.getString(
            key = PREF__CAMERA_PICTURE_UPLOADS_SOURCE,
            defaultValue = CameraUploadsConfiguration.getDefaultSourcePath()
        )

        previousSourcePath = previousSourcePath?.trimEnd(File.separatorChar)

        if (previousSourcePath != data?.getStringExtra(LocalFolderPickerActivity.EXTRA_PATH)) {
            val currentTimeStamp = System.currentTimeMillis()
            cameraUploadsHandlerProvider.updatePicturesLastSync(currentTimeStamp)
        }

        data?.getStringExtra(LocalFolderPickerActivity.EXTRA_PATH)?.let {
            preferencesProvider.putString(
                key = PREF__CAMERA_PICTURE_UPLOADS_SOURCE,
                value = it
            )
        }
    }

    fun schedulePictureUploadsSyncJob() = cameraUploadsHandlerProvider.schedulePictureUploadsSyncJob()
}
