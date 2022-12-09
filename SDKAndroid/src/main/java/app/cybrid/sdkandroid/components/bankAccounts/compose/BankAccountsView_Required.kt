@file:OptIn(DelicateCoroutinesApi::class)

package app.cybrid.sdkandroid.components.bankAccounts.compose

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import app.cybrid.sdkandroid.R
import app.cybrid.sdkandroid.components.BankAccountsView
import app.cybrid.sdkandroid.components.bankAccounts.view.BankAccountsViewModel
import app.cybrid.sdkandroid.core.Constants
import app.cybrid.sdkandroid.ui.Theme.robotoFont
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun BankAccountsView_Required(viewModel: BankAccountsViewModel?) {

    // -- Activity Result for Plaid
    val getPlaidResult = rememberLauncherForActivityResult(OpenPlaidLink()) {
        when (it) {
            is LinkSuccess -> {

                if (it.metadata.accounts.size == 1) {

                    viewModel?.uiState?.value = BankAccountsView.BankAccountsViewState.LOADING
                    GlobalScope.launch {
                        viewModel?.createExternalBankAccount(
                            publicToken = it.publicToken,
                            account = it.metadata.accounts[0])
                    }
                } else {
                    // -- Log multiple accounts or empty accounts
                    viewModel?.uiState?.value = BankAccountsView.BankAccountsViewState.ERROR
                }
            }
            is LinkExit -> {}
        }
    }

    // -- Content
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .testTag(Constants.BankAccountsView.RequiredView.id)
    ) {

        val (text, buttons) = createRefs()

        Row(
            modifier = Modifier.constrainAs(text) {
                start.linkTo(parent.start, margin = 0.dp)
                top.linkTo(parent.top, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
                bottom.linkTo(parent.bottom, margin = 0.dp)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.bank_accounts_view_required_text),
                modifier = Modifier
                    .padding(start = 10.dp),
                fontFamily = robotoFont,
                fontWeight = FontWeight.Medium,
                fontSize = 19.sp,
                lineHeight = 32.sp,
                color = colorResource(id = R.color.black)
            )
        }
        // -- Buttons
        ConstraintLayout(
            Modifier.constrainAs(buttons) {
                start.linkTo(parent.start, margin = 10.dp)
                end.linkTo(parent.end, margin = 10.dp)
                bottom.linkTo(parent.bottom, margin = 20.dp)
                width = Dimension.fillToConstraints
                height = Dimension.value(50.dp)
            }
        ) {

            val (continueButton) = createRefs()

            // -- Continue Button
            Button(
                onClick = {
                    BankAccountsView.openPlaid(
                        plaidToken = viewModel?.latestWorkflow?.plaidLinkToken!!,
                        getPlaidResult = getPlaidResult)
                },
                modifier = Modifier
                    .constrainAs(continueButton) {
                        start.linkTo(parent.start, margin = 10.dp)
                        end.linkTo(parent.end, margin = 10.dp)
                        top.linkTo(parent.top, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 4.dp,
                    disabledElevation = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.primary_color),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(id = R.string.bank_accounts_view_required_button),
                    color = Color.White,
                    fontFamily = robotoFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }
        }
    }
}