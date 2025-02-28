package com.nullpointer.nullsiteadmin.ui.screens.detailsEmail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.nullsiteadmin.R
import com.nullpointer.nullsiteadmin.core.utils.sendEmail
import com.nullpointer.nullsiteadmin.core.utils.shareViewModel
import com.nullpointer.nullsiteadmin.core.utils.toFormat
import com.nullpointer.nullsiteadmin.models.EmailContact
import com.nullpointer.nullsiteadmin.presentation.EmailsViewModel
import com.nullpointer.nullsiteadmin.ui.interfaces.ActionRootDestinations
import com.nullpointer.nullsiteadmin.ui.navigator.RootNavGraph
import com.nullpointer.nullsiteadmin.ui.screens.animation.LottieContainer
import com.nullpointer.nullsiteadmin.ui.screens.states.SimpleScreenState
import com.nullpointer.nullsiteadmin.ui.screens.states.rememberSimpleScreenState
import com.nullpointer.nullsiteadmin.ui.share.ToolbarBackWithDeleter
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.FULL_ROUTE_PLACEHOLDER


@RootNavGraph
@Destination(
    deepLinks = [
        DeepLink(uriPattern = "https://www.nullsiteadmin.com/$FULL_ROUTE_PLACEHOLDER")
    ]
)
@Composable
fun EmailDetailsScreen(
    email: EmailContact,
    rootDestinations: ActionRootDestinations,
    emailsViewModel: EmailsViewModel = shareViewModel(),
    emailsDetailsState: SimpleScreenState = rememberSimpleScreenState()
) {
    Scaffold(
        topBar = {
            ToolbarEmailDetails(actionBack = rootDestinations::backDestination) {
                emailsViewModel.deleterEmail(email.id)
                rootDestinations.backDestination()
            }
        },
        floatingActionButton = {
            ButtonReplyEmail {
                emailsDetailsState.context.sendEmail(email = email.email)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ContainerAnimateEmail()
            ContainerHeaderEmail(emailFrom = email.email, nameFrom = email.name)
            Spacer(modifier = Modifier.height(10.dp))
            BodyEmail(
                timestamp = email.timestamp.toFormat(emailsDetailsState.context),
                subject = email.subject,
                body = email.message
            )
        }
    }
}

@Composable
private fun ToolbarEmailDetails(actionBack: () -> Unit, actionDeleter: () -> Unit) {
    ToolbarBackWithDeleter(
        title = stringResource(R.string.title_email_details),
        actionBack = actionBack,
        contentDescription = stringResource(id = R.string.description_deleter_email),
        actionDeleter = actionDeleter
    )
}

@Composable
private fun BodyEmail(
    timestamp: String,
    subject: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Card(shape = RoundedCornerShape(10.dp), modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = timestamp,
                modifier = Modifier.align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = subject)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = body)
        }
    }
}

@Composable
private fun ButtonReplyEmail(
    actionReplay: () -> Unit
) {
    FloatingActionButton(onClick = actionReplay) {
        Icon(
            painter = painterResource(id = R.drawable.ic_reply),
            contentDescription = stringResource(R.string.description_reply_email)
        )
    }
}

@Composable
private fun ContainerAnimateEmail(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        LottieContainer(
            modifier = Modifier.size(150.dp),
            animation = R.raw.email
        )
    }
}

@Composable
private fun ContainerHeaderEmail(
    emailFrom: String,
    nameFrom: String,
    modifier: Modifier = Modifier
) {
    Card(shape = RoundedCornerShape(10.dp), modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = emailFrom)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = nameFrom)
        }
    }
}