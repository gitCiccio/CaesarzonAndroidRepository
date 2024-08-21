package com.example.caesarzonapplication.navigation

sealed class DetailsScreen(
    val route: String,
    val title: String,
) {
    data object ProductDetailsScreen : DetailsScreen(
        route = "productDetails/{productId}",
        title = "PRODUCTDETAILS",
    )

    data object ProductSearchResultsScreen : DetailsScreen(
        route = "productSearchResults/{productId}",
        title = "PRODUCTSEARCHRESULTS",
    )

    data object UserRegistrationDetailsScreen : DetailsScreen(
        route = "userregistration",
        title = "USERREGISTRATION",
    )

    data object UserPageDetailsScreen : DetailsScreen(
        route = "userpage",
        title = "USERPAGE",
    )

}