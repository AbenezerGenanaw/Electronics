package com.example.electronicgadgetsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.electronicgadgetsapp.ui.theme.ElectronicGadgetsAppTheme




class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateListOf<Product>()
    val cartItems: List<Product> = _cartItems

    fun addToCart(product: Product) {
        _cartItems.add(product)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElectronicGadgetsAppTheme {
                val cartViewModel: CartViewModel = viewModel()
                AppNavigation(cartViewModel)
            }
        }
    }


}

@Composable
fun AppNavigation(cartViewModel: CartViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "productList") {
        composable("productList") {
            ProductsList(products, navController)
        }
        composable(
            route = "productDetail/{productName}",
            arguments = listOf(navArgument("productName") { type = NavType.StringType })
        ) { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")
            val product = products.firstOrNull { it.productName == productName }
            product?.let {
                ProductDetailScreen(product = it, cartViewModel = cartViewModel)
            }
        }
    }
}

@Composable
fun ProductsList(products: List<Product>, navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = products) { product ->
            ProductItem(product = product, onClick = {
                navController.navigate("productDetail/${product.productName}")
            })
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = product.productName,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.productDescription,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\$${product.cost}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProductDetailScreen(product: Product, cartViewModel: CartViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = product.productName, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        // Optionally include an image here using the product.imageResId
        Image(
            painter = painterResource(id = product.imageRestId),
            contentDescription = product.productName,
            modifier = Modifier
                .height(200.dp) // Set a fixed height or adjust as necessary
                .fillMaxWidth() // Make the image fill the available width
                .clip(RoundedCornerShape(8.dp)) // Optional: Clip the image with rounded corners
        )
        Text(text = product.productDescription, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        Text(text = "Price: \$${product.cost}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { cartViewModel.addToCart(product) }) {
            Text("Add to Cart")
        }
    }
}

private val products = listOf(
    Product("iPad", "iPad Pro 11-inch", 400.0, R.drawable.ipad_image), // Replace R.drawable.ipad_image with your actual drawable resource
    Product("MacBook M3 Pro", "12-core CPU\n18-core GPU", 2500.00, R.drawable.macbook_image),
    // We can add more products as needed
)
