package com.example.myshop.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshop.Helper.ManagmentCart



class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val managmentCart = ManagmentCart(this)
        val totalAmount = managmentCart.getTotalFee() + 0.02 * managmentCart.getTotalFee() + 10.0 // tổng tiền

        setContent {
            CheckoutScreen(totalAmount = totalAmount) {
                Toast.makeText(this, "Order successful!", Toast.LENGTH_SHORT).show()

                // Quay lại MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish() // Kết thúc CheckoutActivity để không quay lại nữa
            }
        }
    }
}

@Composable
fun CheckoutScreen(totalAmount: Double, onConfirm: () -> Unit) {
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val isValid = address.isNotBlank() && phone.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Enter your delivery information", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Delivery Address") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Total amount: $${"%.2f".format(totalAmount)}", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

        Button(
            onClick = onConfirm,
            enabled = isValid,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(text = "Confirm Payment")
        }
    }
}
