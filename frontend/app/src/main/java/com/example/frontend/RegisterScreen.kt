package com.example.frontend

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Preview
@Composable
fun RegisterScreen(navController: NavController) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    Image(
        painter = painterResource(R.drawable.register),
        contentDescription = null,
        contentScale = ContentScale.None
    )

    Column(
        modifier = Modifier
            .width(393.dp)
            .height(852.dp),
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
                unfocusedIndicatorColor = Color.Transparent
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
                unfocusedIndicatorColor = Color.Transparent
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
            visualTransformation = PasswordVisualTransformation(),
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
                unfocusedIndicatorColor = Color.Transparent
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
                Log.i("RegisterInfo", "Name : $name Email : $email Password : $password")
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
                text = "로그인", modifier = Modifier.clickable {navController.navigate(Routes.LoginScreen)},
                fontSize = 15.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight(900),
                color = Color(0xFF1D85E6)
            )
        }


    }


}

