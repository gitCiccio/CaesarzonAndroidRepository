package com.example.caesarzonapplication.navigation

sealed class DetailsScreen(
    val route: String,
    val title: String,
) {
    data object ProductDetailsScreen : DetailsScreen(
        route = "productDetails",
        title = "PRODUCTDETAILS",
    )

    data object ProductSearchResultsScreen : DetailsScreen(
        route = "productSearchResults",
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