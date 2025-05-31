package com.example.myshop.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val account = GoogleSignIn.getLastSignedInAccount(context)

    val displayName = account?.displayName ?: "Chưa có tên"
    val email = account?.email ?: "Không có email"
    val photoUrl = account?.photoUrl.toString()
    val userId = account?.id ?: "Không rõ"
    val joinedDate = "28/05/2025"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        if (photoUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = photoUrl),
                contentDescription = "Ảnh đại diện",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = displayName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = email, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "ID người dùng: $userId", fontSize = 14.sp, color = Color.DarkGray)
        Text(text = "Ngày tham gia: $joinedDate", fontSize = 14.sp, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* TODO: Cho phép chỉnh sửa hồ sơ hoặc đăng xuất */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
        ) {
            Text("Edit Profile", color = Color.White)
        }
        Button(
            onClick = {
                // Sign out Firebase
                FirebaseAuth.getInstance().signOut()

                // Sign out GoogleSignInClient (nếu bạn dùng Google Sign-In)
                val googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                googleSignInClient.signOut().addOnCompleteListener {
                    // Sau khi Google sign out xong mới chuyển về IntroActivity
                    val intent = Intent(context, IntroActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)

                    if (context is android.app.Activity) {
                        context.finish()
                    }
                }
            }
            ,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Log Out", color = Color.White)
        }
    }
}
