package com.example.frontend

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.material3.AlertDialog
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

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navController = rememberNavController(), context = null)
}

@Composable
fun RegisterScreen(
    navController: NavController, context: Context?
) {

    val sharedPreferences = context?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var showDialog by remember { mutableStateOf(false) } // 팝업 표시 여부
    var showErrorDialog by remember { mutableStateOf(false) } // 중복 이메일 오류 팝업 표시 여부

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.register),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Column(
        modifier = Modifier
            //배경 비율 채우기
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "시작하기",
            fontSize = 40.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight(900),
            color = Color(0xFF1045A1)
        )

        Spacer(modifier = Modifier.height(35.dp))

        TextField(value = name,
            onValueChange = {
                name = it
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
                    text = "Name",
                    fontSize = 20.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFEAF6FF)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.name_icon),
                    contentDescription = null,
                    tint = Color.White
                )
            })

        Spacer(modifier = Modifier.height(17.dp))

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
                    val savedEmail = sharedPreferences?.getString("saved_email", null)

                    if (savedEmail == email) {
                        showErrorDialog = true // 중복 이메일 팝업 표시
                    } else {
                        sharedPreferences?.edit()?.apply {
                            putString("saved_name", name)
                            putString("saved_email", email)
                            putString("saved_password", password)
                            apply()
                        }
                        showDialog = true // 회원가입 성공 팝업 표시
                        Log.i("RegisterInfo", "Name : $name Email : $email Password : $password")
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
                text = "회원가입",
                fontSize = 20.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(500),
                color = Color(0xFFEAF6FF)
            )
        }

        if (showDialog) {
            CustomAlertDialog(
                title = "회원가입 완료",
                message = "회원가입이 성공적으로 완료되었습니다!",
                onDismiss = { showDialog = false },
                onConfirm = { showDialog = false }
            )
        }

        if (showErrorDialog) {
            CustomAlertDialog(
                title = "회원가입 실패",
                message = "이미 사용 중인 이메일입니다.",
                onDismiss = { showErrorDialog = false },
                onConfirm = { showErrorDialog = false }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text(
                text = "계정을 가지고 있나요?",
                fontSize = 15.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(300),
                color = Color(0xFF63B4F6)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "로그인",
                modifier = Modifier.clickable { navController.navigate(Routes.LoginScreen) },
                fontSize = 15.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(900),
                color = Color(0xFF1D85E6)
            )
        }


    }

}