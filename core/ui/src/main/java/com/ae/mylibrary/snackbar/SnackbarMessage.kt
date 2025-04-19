package com.ae.mylibrary.snackbar

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.ae.mylibrary.theme.AEHealthColorScheme

sealed interface SnackbarMessage {
    val id: Long

    val message: String
    val description: String?

    @get:StringRes
    val iconRes: Int

    sealed interface ResultMessage : SnackbarMessage {

        val color: Color

        data class ErrorMessage(
            override val message: String,
            override val description: String? = null
        ) : ResultMessage {
            override val id: Long = System.currentTimeMillis()
            override val iconRes: Int = 0
            override val color: Color = AEHealthColorScheme.Red
        }

        class SuccessMessage(
            override val message: String,
            override val description: String? = null
        ) : ResultMessage {
            override val id: Long = System.currentTimeMillis()
            override val iconRes: Int = 0
            override val color: Color = AEHealthColorScheme.Green
        }
    }

    sealed interface ActionMessage : SnackbarMessage {

        data class SingleActionMessage(
            override val message: String,
            override val description: String? = null,
            val action: () -> Unit
        ) : ActionMessage {
            override val id: Long = System.currentTimeMillis()
            override val iconRes: Int = 0
        }

        data class DoubleActionMessage(
            override val message: String,
            override val description: String? = null,
            val onAgree: () -> Unit,
            val onDisagree: () -> Unit
        ) : ActionMessage {
            override val id: Long = System.currentTimeMillis()
            override val iconRes: Int = 0
        }

    }

}