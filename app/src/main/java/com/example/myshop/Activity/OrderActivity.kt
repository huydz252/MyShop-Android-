package com.example.myshop.Activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myshop.Model.ItemsModel

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nhận dữ liệu từ intent
        val orderItems = intent.getSerializableExtra("cart_items") as? ArrayList<ItemsModel> ?: arrayListOf()
        val totalPrice = intent.getDoubleExtra("totalPrice", 0.0)

        setContent {
            MaterialTheme {
                OrderScreen(orderItems, totalPrice)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(orderItems: List<ItemsModel>, totalPrice: Double) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Your Order") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (orderItems.isEmpty()) {
                Text("No items in your order.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(orderItems) { item ->
                        OrderItemCard(item = item)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Total: $${"%.2f".format(totalPrice)}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(item: ItemsModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(80.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = item.picUrl.firstOrNull(),
                contentDescription = item.title,
                modifier = Modifier.size(80.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "Quantity: ${item.numberInCart}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Price: $${item.price}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
