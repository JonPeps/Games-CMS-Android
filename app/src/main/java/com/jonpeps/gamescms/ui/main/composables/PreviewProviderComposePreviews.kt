package com.jonpeps.gamescms.ui.main.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jonpeps.gamescms.ui.applevel.CustomColours

data class CommonEmptyScreenHolder(val text: String, val customColours: CustomColours, val btnText: String = "OK")

class CommonEmptyScreenProvider : PreviewParameterProvider<CommonEmptyScreenHolder> {
    override val values: Sequence<CommonEmptyScreenHolder> = sequenceOf(
        CommonEmptyScreenHolder(
            "No Data", CustomColours(false)
        )
    )
}

class ErrorStringHolder(val customColours: CustomColours, val header: String, val value: String, val repeatCount: Int = 350, val btnText: String = "OK")

class ErrorClassStringProvider : PreviewParameterProvider<ErrorStringHolder> {
    override val values: Sequence<ErrorStringHolder> = sequenceOf(
        ErrorStringHolder(
            CustomColours(false), "An error occurred, correct your logic!", "An exception! "
        )
    )
}

@Composable
@Preview
fun BasicErrorPreview(@PreviewParameter(ErrorClassStringProvider::class) errorStringHolder: ErrorStringHolder) {
    BasicError(errorStringHolder.customColours, errorStringHolder.header, errorStringHolder.value, errorStringHolder.btnText) {}
}

@Composable
@Preview
fun CommonEmptyScreenPreview(@PreviewParameter(CommonEmptyScreenProvider::class) commonEmptyScreenHolder: CommonEmptyScreenHolder) {
    CommonEmptyScreen(commonEmptyScreenHolder.text, commonEmptyScreenHolder.customColours, commonEmptyScreenHolder.btnText) {}
}