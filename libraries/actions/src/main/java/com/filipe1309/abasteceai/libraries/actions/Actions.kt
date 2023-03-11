package com.filipe1309.abasteceai.libraries.actions

import android.content.Context
import android.content.Intent

object Actions {
    private const val FEATURES_PKG_NAME = "com.filipe1309.abasteceai.features"
    fun openComparatorIntent(context: Context) = internalIntent(context, "${FEATURES_PKG_NAME}.comparator.open")
    // fun openHistoryIntent
    // fun openFuelsIntent
    // fun openNewsIntent
    // fun openSettingsIntent

    @Suppress("SameParameterValue")
    private fun internalIntent(context: Context, action: String) = Intent(action).setPackage(context.packageName)
}
