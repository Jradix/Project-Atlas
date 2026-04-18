package com.atlas.logger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Atlas Logger Application class.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation
 * and serve as the application-level dependency container.
 */
@HiltAndroidApp
class AtlasApplication : Application()
