package com.example.myshop.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myshop.Helper.ManagmentCart
import com.example.myshop.Model.ItemsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CheckoutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val managmentCart = ManagmentCart(this)
        val totalAmount = managmentCart.getTotalFee() + 0.02 * managmentCart.getTotalFee() + 10.0

        val cartItems = intent.getSerializableExtra("cart_items") as? ArrayList<ItemsModel> ?: arrayListOf()

        setContent {
            CheckoutScreen(totalAmount = totalAmount, cartItems = cartItems) {
                Toast.makeText(this, "Order successful!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
        }
    }

}

@Composable
fun CheckoutScreen(totalAmount: Double, cartItems: ArrayList<ItemsModel>, onConfirm: () -> Unit) {
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    val isValid = address.isNotBlank() && phone.isNotBlank()

    val context = LocalContext.current
    val totalPrice = cartItems.sumOf { it.price * it.numberInCart }


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
            onClick = {
                saveOrderToFirestore(
                    cartItems = cartItems,
                    totalPrice = totalAmount,
                    onSuccess = {
                        Toast.makeText(context, "Order successful!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(context,  MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    onError = { e ->
                        Log.e("OrderSave", "Failed: ${e.message}")

                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                )
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Confirm Payment")
        }
    }
}

private fun saveOrderToFirestore(cartItems: List<ItemsModel>, totalPrice: Double, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
    val db = Firebase.firestore
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val items = cartItems.map {
        mapOf(
            "title" to it.title,
            "price" to it.price,
            "quantity" to it.numberInCart,
            "picUrl" to it.picUrl
        )
    }

    val orderData = mapOf(
        "userId" to userId,
        "items" to items,
        "totalPrice" to totalPrice,
        "timestamp" to com.google.firebase.Timestamp.now()
    )

    db.collection("orders")
        .add(orderData)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            onError(e)
        }
}




