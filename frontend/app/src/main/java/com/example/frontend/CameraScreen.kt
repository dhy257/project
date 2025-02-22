package com.example.frontend

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasCameraPermission = isGranted
        }

    // 앱 실행 시 권한 요청
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (!hasCameraPermission) {
        //권한이 없으면 메시지 표시
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("카메라 권한이 필요합니다.", color = Color.White)
        }
        return
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx ->
            val previewView = androidx.camera.view.PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val preview = androidx.camera.core.Preview.Builder().build()
                val imageCaptureBuilder = ImageCapture.Builder().build()
                imageCapture = imageCaptureBuilder

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCaptureBuilder
                    )
                    preview.surfaceProvider = previewView.surfaceProvider

                    Log.d("CameraScreen", "CameraX 초기화 완료")
                } catch (e: Exception) {
                    Log.e("CameraScreen", "CameraX 초기화 실패: ${e.message}", e)
                }
            }, ContextCompat.getMainExecutor(context))

            previewView
        }, modifier = Modifier.fillMaxSize())

        // 뒤로가기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "뒤로 가기",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }

        // 하단 버튼 UI
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                //하단 바 맞추기
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 갤러리에서 이미지 불러오기 버튼
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        color = Color(0xFF616161),
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .clickable {
                        // 갤러리에서 이미지 불러오기
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        context.startActivity(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "이미지 불러오기",
                    fontSize = 10.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            }

            // 셔터 버튼 (shutter.png 사용)
            Image(
                painter = painterResource(id = R.drawable.shutter),
                contentDescription = "셔터 버튼",
                modifier = Modifier
                    .size(80.dp)
                    .clickable {
                        coroutineScope.launch(Dispatchers.IO) {
                            val capture = imageCapture
                            if (capture != null) {
                                val contentValues = ContentValues().apply {
                                    put(
                                        MediaStore.Images.Media.DISPLAY_NAME,
                                        "IMG_${System.currentTimeMillis()}.jpg"
                                    )
                                    //이미지 저장 형식
                                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                                    //이미지 저장 폴더 이름
                                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraApp")
                                }

                                val resolver = context.contentResolver
                                val imageUri = resolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    contentValues
                                )

                                if (imageUri != null) {
                                    val outputStream = resolver.openOutputStream(imageUri)
                                    if (outputStream != null) {
                                        val outputOptions =
                                            ImageCapture.OutputFileOptions.Builder(outputStream)
                                                .build()

                                        capture.takePicture(
                                            outputOptions,
                                            ContextCompat.getMainExecutor(context),
                                            object : ImageCapture.OnImageSavedCallback {
                                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                                        "capturedImageUri",
                                                        imageUri
                                                    )
                                                    //navController.popBackStack()
                                                    Log.d("CameraScreen", "사진 저장 완료: $imageUri")
                                                }

                                                override fun onError(exception: ImageCaptureException) {
                                                    Log.e(
                                                        "CameraScreen",
                                                        "사진 촬영 실패: ${exception.message}",
                                                        exception
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        Log.e("CameraScreen", "MediaStore에 사진을 저장할 수 없습니다.")
                                    }
                                } else {
                                    Log.e("CameraScreen", "imageCapture가 null입니다.")
                                }
                            }

                        }
                    }
            )

            // 촬영된 이미지 확인 버튼
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .clickable {
                            navController.navigate(Routes.ReciptScreen)
                        },
                ){
                    Image(
                        modifier = Modifier.size(70.dp), // 아이콘 크기 조정
                        painter = painterResource(R.drawable.receipt_icon),
                        contentDescription = "영수증 보기 아이콘"
                    )
                }

                Text(
                    text = "영수증 보기",
                    fontSize = 10.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CameraScreenPreview() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // 뒤로가기 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = "뒤로 가기",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                    }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.Gray, RoundedCornerShape(10.dp))
                    .clickable {
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "이미지 불러오기",
                    fontSize = 10.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            }

            Image(
                painter = painterResource(R.drawable.shutter),
                contentDescription = "셔터 버튼",
                modifier = Modifier
                    .size(80.dp)
                    .clickable { }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .clickable { },
                ){
                    Image(
                        modifier = Modifier.size(70.dp), // 아이콘 크기 조정
                        painter = painterResource(R.drawable.receipt_icon),
                        contentDescription = "영수증 보기 아이콘"
                    )
                }

                Text(
                    text = "영수증 보기",
                    fontSize = 10.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            }

        }
    }
}