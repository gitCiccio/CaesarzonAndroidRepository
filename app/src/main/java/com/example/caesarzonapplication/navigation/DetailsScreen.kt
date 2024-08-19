package com.example.caesarzonapplication.navigation

sealed class DetailsScreen(
    val route: String,
    val title: String,
) {
    data object AddProductDetailsScreen : DetailsScreen(
        route = "addproduct",
        title = "ADDPRODUCT",
    )

    data object AdminScreen : DetailsScreen(
        route = "admin",
        title = "ADMIN",
    )

    data object SupportRequestDetailsScreen : DetailsScreen(
        route = "supportrequest",
        title = "SUPPORTREQUEST",
    )

    data object ProductDetailsScreen : DetailsScreen(
        route = "productDetails/{productId}",
        title = "PRODUCTDETAILS",
    )

    data object ProductSearchResultsScreen : DetailsScreen(
        route = "productSearchResults/{productId}",
        title = "PRODUCTSEARCHRESULTS",
    )

    data object ReportsDetailsScreen : DetailsScreen(
        route = "reports",
        title = "REPORTS",
    )

    data object UserRegistrationDetailsScreen : DetailsScreen(
        route = "userregistration",
        title = "USERREGISTRATION",
    )

    data object UserSearchDetailsScreen : DetailsScreen(
        route = "usersearch",
        title = "USERSEARCH",
    )

    data object UserPageDetailsScreen : DetailsScreen(
        route = "userpage",
        title = "USERPAGE",
    )

}