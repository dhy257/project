package com.example.frontend

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFBBDEFA), // 배경색 적용
        title = {
            Text(
                text = title,
                fontFamily = pretendard, // 폰트 적용
                fontWeight = FontWeight(900),
                fontSize = 20.sp,
                color = Color(0xFF1045A1)
            )
        },
        text = {
            Text(
                text = message,
                fontFamily = pretendard, // 폰트 적용
                fontWeight = FontWeight(600),
                fontSize = 16.sp,
                color = Color(0xFF1045A1)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF63B4F6)) // 버튼 색상 적용
            ) {
                Text(
                    text = "확인",
                    fontFamily = pretendard, // 폰트 적용
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewCustomAlertDialog1() {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        CustomAlertDialog(
            title = "회원가입 완료",
            message = "회원가입이 성공적으로 완료되었습니다!",
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomAlertDialog() {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        CustomAlertDialog(
            title = "로그인 성공",
            message = "userName 님 환영합니다!",
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false }
        )
    }
}