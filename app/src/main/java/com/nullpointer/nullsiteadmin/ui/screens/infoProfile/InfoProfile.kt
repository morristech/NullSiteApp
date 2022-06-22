package com.nullpointer.nullsiteadmin.ui.screens.infoProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nullpointer.nullsiteadmin.R
import com.nullpointer.nullsiteadmin.core.states.Resource
import com.nullpointer.nullsiteadmin.models.InfoType
import com.nullpointer.nullsiteadmin.models.PersonalInfo
import com.nullpointer.nullsiteadmin.ui.screens.infoProfile.dialogChange.DialogChangeInfo
import com.nullpointer.nullsiteadmin.ui.screens.infoProfile.dialogChange.viewModel.DialogChangeViewModel
import com.nullpointer.nullsiteadmin.ui.screens.infoProfile.viewModel.EditableViewModel

@Composable
fun InfoProfile(
    editableVM: EditableViewModel = hiltViewModel(),
    dialogChangeVM: DialogChangeViewModel = hiltViewModel(),
) {

    val stateInfoProfile by editableVM.personalInfo.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        }
    ) {

        when (stateInfoProfile) {
            is Resource.Failure -> TODO()
            is Resource.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val infoState = (stateInfoProfile as Resource.Success<PersonalInfo>).data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EditImgProfile(infoState.urlImg)
                    EditInfoProfile(
                        personalInfo = infoState,
                        actionEditText = dialogChangeVM::showDialog
                    )
                }
                if (dialogChangeVM.isVisible) {
                    DialogChangeInfo(
                        actionAccept = {

                        },
                        dialogChangeVM = dialogChangeVM
                    )
                }
            }
        }
    }

}

@Composable
private fun EditImgProfile(
    urlImg: String
) {
    Card(
        shape = CircleShape, modifier = Modifier
            .padding(20.dp)
            .size(150.dp)
    ) {
        AsyncImage(model = urlImg, contentDescription = "")
    }
}

@Composable
private fun EditInfoProfile(
    personalInfo: PersonalInfo,
    modifier: Modifier = Modifier,
    actionEditText: (infoType: InfoType, initValue: String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabelAndInfo(
            infoType = InfoType.NAME,
            text = personalInfo.name,
            actionEditText = actionEditText
        )
        Spacer(modifier = Modifier.height(10.dp))
        LabelAndInfo(
            infoType = InfoType.PROCESSION,
            text = personalInfo.profession,
            actionEditText = actionEditText
        )
        Spacer(modifier = Modifier.height(10.dp))
        LabelAndInfo(
            infoType = InfoType.DESCRIPTION,
            text = personalInfo.description,
            modifier = Modifier.height(200.dp),
            actionEditText = actionEditText
        )
    }
}

@Composable
private fun LabelAndInfo(
    infoType: InfoType,
    text: String,
    modifier: Modifier = Modifier,
    actionEditText: (infoType: InfoType, initValue: String) -> Unit,
) {
    Column(horizontalAlignment = Alignment.End) {
        OutlinedTextField(
            modifier = modifier
                .clickable { actionEditText(infoType, text) }
                .fillMaxWidth(.9f),
            value = text,
            onValueChange = {},
            enabled = false,
            label = { Text(infoType.label) },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = ""
                )
            }
        )
        Text(
            text = "${text.length}/250",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(end = 5.dp)
        )
    }

}
