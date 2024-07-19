package com.example.appbannoithat.Screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appbannoithat.Model.NguoiDungDN
import com.example.appbannoithat.Model.updateSocket
import com.example.appbannoithat.ViewModel.ViewModel
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController, viewModel: ViewModel) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.clickable {
                    navController.navigate("trangchu")
                },
                title = {
                    Text(
                        text = "Trang chủ",
                        color = Color.White,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        navController.navigate("trangchu")
                                    }
                                )
                                .size(20.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.8f)
                )
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            LoginForm(scope, snackbarHostState, navController, viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    navController: NavHostController,
    viewModel: ViewModel,
) {
    var accountName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var accountNameErr by remember { mutableStateOf("") }
    var passwordErr by remember { mutableStateOf("") }

    val loginError by viewModel.loginErr
    val isLoggedIn by viewModel.isLoggedIn

    val context = LocalContext.current

    val socketId = viewModel.socketID.observeAsState()

    val acc = viewModel.acc.observeAsState()
    val idUser = acc.value?._id
    if (isLoggedIn) {
        passwordErr = ""
        accountNameErr = ""
        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
        navController.navigate("trangchu")
        val objUp = idUser?.let {
            updateSocket(
                id = it,
                socket =  socketId.value.toString()
            )
        }
        if (objUp != null) {
            viewModel.updateSocket(objUp)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextAcc("Tài khoản người dùng", Color.DarkGray)

        OutlinedTextField(
            value = accountName,
            onValueChange = {
                accountName = it
                accountNameErr = ""
            },
            placeholder = {
                Text(
                    "Nhập tài khoản",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray.copy(
                            alpha = 0.4f
                        )
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                cursorColor = Color.Black,
            ),
            isError = accountNameErr.isNotEmpty(),
        )

        if (accountNameErr.isNotEmpty()) {
            TextAcc(accountNameErr)
        }

        Spacer(modifier = Modifier.height(30.dp))
        TextAcc("Mật khẩu", Color.DarkGray)
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordErr = ""
            },
            placeholder = {
                Text(
                    "Nhập mật khẩu",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray.copy(
                            alpha = 0.4f
                        )
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                cursorColor = Color.Black,
            ),
            isError = passwordErr.isNotEmpty(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (passwordErr.isNotEmpty()) {
            TextAcc(passwordErr)
        }
        Spacer(modifier = Modifier.height(30.dp))

        if (loginError != null) {
            Text(
                text = loginError ?: "",
                fontSize = 13.sp,
                color = Color.Red.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 10.dp),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        if (accountName.isEmpty()) {
                            accountNameErr = "Hãy nhập tên tài khoản"
                        }
                        if (password.isEmpty()) {
                            passwordErr = "Hãy nhập mật khẩu"
                        }

                        val accReq = NguoiDungDN(
                            ten_tai_khoan = accountName,
                            mat_khau = password
                        )

                        viewModel.postLogin(accReq)
                    }
                )
                .fillMaxWidth()
                .height(60.dp)
                .padding(10.dp)
                .background(Color(0xFF02DC66), shape = RoundedCornerShape(5.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Bạn chưa có tài khoản?",
                fontSize = 13.sp,
                color = Color.DarkGray.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Đăng ký",
                fontSize = 13.sp,
                color = Color.Black.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            viewModel.registerDH()
                            navController.navigate("register") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
            )
        }
    }
}