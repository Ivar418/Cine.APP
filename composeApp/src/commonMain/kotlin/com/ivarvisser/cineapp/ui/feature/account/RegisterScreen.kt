package com.ivarvisser.cineapp.ui.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.already_have_account
import cineapp.composeapp.generated.resources.confirm_password
import cineapp.composeapp.generated.resources.create_account_title
import cineapp.composeapp.generated.resources.email
import cineapp.composeapp.generated.resources.email_required
import cineapp.composeapp.generated.resources.first_name
import cineapp.composeapp.generated.resources.invalid_email
import cineapp.composeapp.generated.resources.last_name
import cineapp.composeapp.generated.resources.password
import cineapp.composeapp.generated.resources.password_min_length
import cineapp.composeapp.generated.resources.password_required
import cineapp.composeapp.generated.resources.passwords_do_not_match
import cineapp.composeapp.generated.resources.register_button
import cineapp.composeapp.generated.resources.required_field
import cineapp.composeapp.generated.resources.username
import cineapp.composeapp.generated.resources.username_required
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreen(
    onRegisterClick: (username: String, firstName: String, lastName: String, email: String, password: String) -> Unit,
    onBackClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var firstNameError by remember { mutableStateOf<String?>(null) }
    var lastNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$".toRegex()

    val usernameRequiredMsg = stringResource(Res.string.username_required)
    val firstNameRequiredMsg = stringResource(Res.string.required_field)
    val lastNameRequiredMsg = stringResource(Res.string.required_field)
    val emailRequiredMsg = stringResource(Res.string.email_required)
    val invalidEmailMsg = stringResource(Res.string.invalid_email)
    val passwordRequiredMsg = stringResource(Res.string.password_required)
    val passwordMinLengthMsg = stringResource(Res.string.password_min_length)
    val passwordsDoNotMatchMsg = stringResource(Res.string.passwords_do_not_match)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(Res.string.create_account_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = null
            },
            label = { Text(stringResource(Res.string.username)) },
            singleLine = true,
            isError = usernameError != null,
            supportingText = usernameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = null
                },
                label = { Text(stringResource(Res.string.first_name)) },
                singleLine = true,
                isError = firstNameError != null,
                supportingText = firstNameError?.let { { Text(it) } },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    lastNameError = null
                },
                label = { Text(stringResource(Res.string.last_name)) },
                singleLine = true,
                isError = lastNameError != null,
                supportingText = lastNameError?.let { { Text(it) } },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text(stringResource(Res.string.email)) },
            singleLine = true,
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text(stringResource(Res.string.password)) },
            singleLine = true,
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it) } },
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = null
            },
            label = { Text(stringResource(Res.string.confirm_password)) },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            isError = confirmPasswordError != null,
            supportingText = confirmPasswordError?.let { { Text(it) } },
            trailingIcon = {
                IconButton(
                    onClick = { confirmPasswordVisible = !confirmPasswordVisible }
                ) {
                    Icon(
                        imageVector = if (confirmPasswordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                usernameError = if (username.isBlank()) usernameRequiredMsg else null
                firstNameError = if (firstName.isBlank()) firstNameRequiredMsg else null
                lastNameError = if (lastName.isBlank()) lastNameRequiredMsg else null
                emailError = when {
                    email.isBlank() -> emailRequiredMsg
                    !email.matches(emailRegex) -> invalidEmailMsg
                    else -> null
                }
                passwordError = when {
                    password.isBlank() -> passwordRequiredMsg
                    password.length < 6 -> passwordMinLengthMsg
                    else -> null
                }
                confirmPasswordError = when {
                    confirmPassword != password -> passwordsDoNotMatchMsg
                    else -> null
                }

                if (usernameError == null && firstNameError == null && lastNameError == null && emailError == null &&
                    passwordError == null && confirmPasswordError == null
                ) {
                    onRegisterClick(username, firstName, lastName, email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.register_button))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackClick
        ) {
            Text(stringResource(Res.string.already_have_account))
        }
    }
}
