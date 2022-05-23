package com.example.fazalmapbox.utils

import com.example.swiftbay.Model.*
import com.example.swiftbay.helper.okHttpClient
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface APIInterface {
    @POST(USERS_LOGIN)
    fun loginUser(@Body modelLogin: ModelLogin): Call<LoginResponse>

    @POST(USERS_SIGNUP)
    fun signupUser(@Body modelSignup: ModelSignup): Call<SignupResponse>

    @POST(CART)
    fun addToCart(
        @Header("Authorization") Authorization: String?,
        @Body addToCartModel: AddToCartModel
    ):Call<AddToCartResponse>

    @GET(CART)
    fun getCart(
        @Header("Authorization") Authorization: String?,
    ):Call<AddToCartResponse>

    @GET(ORDER)
    fun getOrder(
        @Header("Authorization") Authorization: String?,
    ):Call<OrderResponse>

    @GET(ORDER + NEW)
    fun getRiderOrder(
        @Header("Authorization") Authorization: String?,
    ):Call<OrderResponse>

    @PATCH(ORDER + RIDER + GET_BY_ID)
    fun getRiderOrderUpdate(
        @Path("id") id: String?,
        @Header("Authorization") Authorization: String?,
        @Body updateStutsModel: UpdateStutsModel
    ):Call<OrderUpdateResponse>

    @POST(ORDER)
    fun placeOrder(
        @Header("Authorization") Authorization: String?,
        @Body placeOrderModel: PlaceOrderModel
    ):Call<OrderResponse>

    @POST(ORDER)
    fun placeOrder2(
        @Header("Authorization") Authorization: String?,
        @Body placeOrderModel2: PlaceModel
    ):Call<OrderResponse>

    @GET(ENDPIONT_RESTAURANTS)
    fun getRestaurants(): Call<RestaurantsResponse>

    @GET(ENDPIONT_RESTAURANTS)
    fun getRestaurants(
        @Query("name") name: String,): Call<RestaurantsResponse>

    @GET(GET_SELLER_RESTAURANTS)
    fun getSellerRestaurants(@Header("Authorization") Authorization: String?): Call<SellerRestaurantsResponse>

    @Multipart
    @POST(ENDPIONT_RESTAURANTS)
    fun createRestaurants(
        @Header("Authorization") Authorization: String?,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("address") address: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
    ): Call<CreateResturantResponse>

    @Multipart
    @PUT(ENDPIONT_RESTAURANTS + GET_BY_ID)
    fun updateRestaurants(
        @Path("id") restId: String?,
        @Header("Authorization") Authorization: String?,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<CreateResturantResponse>

    @GET(GET_SELLER_PRODUCTS + GET_BY_ID)
    fun sellerProducts(
        @Path("id") id: String?,
    ): Call<AllProductsResponse>

    @Multipart
    @PUT(ENDPIONT_RESTAURANTS + GET_BY_ID)
    fun updateRestaurants(
        @Path("id") restId: String?,
        @Header("Authorization") Authorization: String?,
        @Part("name") name: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("city") city: RequestBody?,
    ): Call<CreateResturantResponse>

    @Multipart
    @PUT(PRODUCTS + GET_BY_ID)
    fun updateProduct(
        @Path("id") restId: String?,
        @Header("Authorization") Authorization: String?,
        @Part("productName") productName: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?
    ): Call<CreateProductResponse>

    @Multipart
    @PUT(PRODUCTS + GET_BY_ID)
    fun updateProduct(
        @Path("id") restId: String?,
        @Header("Authorization") Authorization: String?,
        @Part("productName") productName: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part productImage: MultipartBody.Part?
    ): Call<CreateProductResponse>

    @Multipart
    @POST(PRODUCTS + GET_BY_ID)
    fun createProduct(
        @Path("id") restId: String?,
        @Header("Authorization") Authorization: String?,
        @Part("productName") productName: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody?,
        @Part productImage: MultipartBody.Part?,
    ): Call<CreateProductResponse>

    @DELETE(ENDPIONT_RESTAURANTS + GET_BY_ID)
    fun deleteRestaurants(
        @Path("id") restId: String?,
        @Header("Authorization") Authorization: String?,
    ): Call<CreateResturantResponse>

    @PATCH(CART)
    fun deleteCart(
        @Header("Authorization") Authorization: String?,
        @Body deleteCartModel:DeleteCartModel
    ): Call<AddToCartResponse>

    @DELETE(PRODUCTS + GET_BY_ID)
    fun deleteProduct(
        @Path("id") restId: String?,
        @Header("Authorization") Authorization: String?,
    ): Call<DelProductResponse>


    companion object {
        const val USERS_LOGIN = "users/login"
        const val USERS_SIGNUP = "users/signup"
        const val ENDPIONT_RESTAURANTS = "resturaunt"
        const val CART = "cart"
        const val ORDER = "order"
        const val NEW = "/new"
        const val RIDER = "/rider"
        const val GET_SELLER_RESTAURANTS = "resturaunt/seller"
        const val GET_SELLER_PRODUCTS = "products/resturaunt"
        const val PRODUCTS = "products"
        const val base_url = "https://swift-bay.herokuapp.com/swiftbay/"
        const val GET_BY_ID = "/{id}"

        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}