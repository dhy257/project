package com.example.frontend

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.extrafunc.CustomAlertDialog


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        navController = rememberNavController(), context = null

    )
}

@Composable
fun LoginScreen(
    navController: NavController, context: Context?
) {

    val sharedPreferences = context?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)


    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var userName by remember { mutableStateOf("") } // 사용자 이름 저장

    var showDialog by remember { mutableStateOf(false) } // 팝업 표시 여부
    var loginSuccess by remember { mutableStateOf(false) } // 로그인 성공 여부

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.login),
        contentDescription = "로그인 배경",
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            //화면 비율 맞추기
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "환영합니다!",
            fontSize = 40.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight(900),
            color = Color(0xFF1045A1)
        )

        Spacer(modifier = Modifier.height(35.dp))

        TextField(value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier
                .width(323.dp)
                .background(color = Color(0xFFBBDEFA), shape = RoundedCornerShape(size = 10.dp)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFBBDEFA),
                focusedContainerColor = Color(0xFFBBDEFA),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color(0xFF1045A1),
                unfocusedTextColor = Color(0xFF1045A1)
            ),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(500)
            ),
            shape = RoundedCornerShape(size = 10.dp),
            placeholder = {
                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFEAF6FF)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.email_icon),
                    contentDescription = null,
                    tint = Color.White
                )
            })

        Spacer(modifier = Modifier.height(17.dp))

        TextField(value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier
                .width(323.dp)
                .background(color = Color(0xFFBBDEFA), shape = RoundedCornerShape(size = 10.dp)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFBBDEFA),
                focusedContainerColor = Color(0xFFBBDEFA),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color(0xFF1045A1),
                unfocusedTextColor = Color(0xFF1045A1)
            ),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(500)
            ),
            shape = RoundedCornerShape(size = 10.dp),
            placeholder = {
                Text(
                    text = "Password",
                    fontSize = 20.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFEAF6FF)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.password_icon),
                    contentDescription = null,
                    tint = Color.White
                )
            })

        Spacer(modifier = Modifier.height(35.dp))

        Button(
            onClick = {
                if (context != null) { // context가 null이 아닐 때만 실행
                    Log.i("LogInfo", "Email : $email Password : $password")
                    val savedEmail = sharedPreferences?.getString("saved_email", null)
                    val savedPassword = sharedPreferences?.getString("saved_password", null)

                    if (email == savedEmail && password == savedPassword) {
                        loginSuccess = true
                        userName = sharedPreferences?.getString("saved_name", "") ?: ""
                        showDialog = true // 로그인 성공 팝업 표시
                    } else {
                        loginSuccess = false
                        showDialog = true // 로그인 실패 팝업 표시
                        Log.e("LoginError", "로그인 실패: 이메일 또는 비밀번호가 일치하지 않음")
                    }
                }
            },
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                )
                .width(323.dp)
                .background(color = Color(0xFF63B4F6), shape = RoundedCornerShape(size = 10.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF63B4F6)
            )
        ) {
            Text(
                text = "로그인",
                fontSize = 20.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(500),
                color = Color(0xFFEAF6FF)
            )
        }

        // 로그인 결과 팝업
        if (showDialog) {
            CustomAlertDialog(
                title = if (loginSuccess) "로그인 성공" else "로그인 실패",
                message = if (loginSuccess) "$userName 님 환영합니다!" else "이메일 또는 비밀번호가 올바르지 않습니다.",
                onDismiss = { showDialog = false },
                onConfirm = {
                    showDialog = false
                    if (loginSuccess) navController.navigate(Routes.MainScreen)
                }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text(
                text = "계정을 가지고 있지 않나요?",
                fontSize = 15.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(300),
                color = Color(0xFF63B4F6)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "회원가입", modifier = Modifier.clickable {
                    navController.navigate(Routes.RegisterScreen)
                },
                fontSize = 15.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(900),
                color = Color(0xFF1D85E6)
            )
        }


    }


}