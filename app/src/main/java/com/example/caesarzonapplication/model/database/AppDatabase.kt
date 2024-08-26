package com.example.caesarzonapplication.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.caesarzonapplication.model.dao.notificationDao.AdminNotificationDao
import com.example.caesarzonapplication.model.dao.notificationDao.BanDao
import com.example.caesarzonapplication.model.dao.notificationDao.ReportDao
import com.example.caesarzonapplication.model.dao.notificationDao.SupportDao
import com.example.caesarzonapplication.model.dao.notificationDao.UserNotificationDao
import com.example.caesarzonapplication.model.dao.productDao.ProductDao
import com.example.caesarzonapplication.model.dao.productDao.ProductImageDao
import com.example.caesarzonapplication.model.dao.productDao.ProductOrderDao
import com.example.caesarzonapplication.model.dao.userDao.AddressDao
import com.example.caesarzonapplication.model.dao.userDao.CardDao
import com.example.caesarzonapplication.model.dao.userDao.CityDataDao
import com.example.caesarzonapplication.model.dao.userDao.FollowerDao
import com.example.caesarzonapplication.model.dao.userDao.ProfileImageDao
import com.example.caesarzonapplication.model.dao.userDao.UserDao
import com.example.caesarzonapplication.model.dao.wishlistDao.WishlistDao
import com.example.caesarzonapplication.model.dao.wishlistDao.WishlistProductDao
import com.example.caesarzonapplication.model.entities.notificationEntity.AdminNotification
import com.example.caesarzonapplication.model.entities.notificationEntity.Ban
import com.example.caesarzonapplication.model.entities.notificationEntity.Report
import com.example.caesarzonapplication.model.entities.notificationEntity.Support
import com.example.caesarzonapplication.model.entities.notificationEntity.UserNotification
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.Product
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductImage
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductOrder
import com.example.caesarzonapplication.model.entities.userEntity.Address
import com.example.caesarzonapplication.model.entities.userEntity.Card
import com.example.caesarzonapplication.model.entities.userEntity.CityData
import com.example.caesarzonapplication.model.entities.userEntity.Follower
import com.example.caesarzonapplication.model.entities.userEntity.ProfileImage
import com.example.caesarzonapplication.model.entities.userEntity.User
import com.example.caesarzonapplication.model.entities.wishListEntity.Wishlist
import com.example.caesarzonapplication.model.entities.wishListEntity.WishlistProduct
import com.example.caesarzonapplication.model.utils.BitmapConverter
import com.example.caesarzonapplication.model.utils.Converters

@Database (entities = [AdminNotification::class, Ban::class, Report::class, Support::class, UserNotification::class,ProfileImage::class,
                      Product::class, ProductOrder::class, ProductImage::class,
                      Address::class, Card::class, CityData::class, Follower::class, User::class,
                      Wishlist::class, WishlistProduct::class],
    version = 3, exportSchema = false)
@TypeConverters(Converters::class, BitmapConverter::class)
abstract class AppDatabase: RoomDatabase()  {
    abstract fun adminNotificationDao(): AdminNotificationDao
    abstract fun banDao(): BanDao
    abstract fun reportDao(): ReportDao
    abstract fun supportDao(): SupportDao
    abstract fun userNotificationDao(): UserNotificationDao
    abstract fun productDao(): ProductDao
    abstract fun productOrderDao(): ProductOrderDao
    abstract fun addressDao(): AddressDao
    abstract fun cardDao(): CardDao
    abstract fun cityDataDao(): CityDataDao
    abstract fun followerDao(): FollowerDao
    abstract fun userDao(): UserDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun wishlistProductDao(): WishlistProductDao
    abstract fun profileImageDao(): ProfileImageDao
    abstract fun productImageDao(): ProductImageDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Droppa la vecchia tabella indirizzo
                db.execSQL("DROP TABLE IF EXISTS indirizzo")

                // Crea la nuova tabella indirizzo con la struttura aggiornata
                db.execSQL("""
                    CREATE TABLE indirizzo (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        id_indirizzo TEXT NOT NULL,
                        nome_strada TEXT NOT NULL,
                        numero_civico TEXT NOT NULL,
                        tipo_strada TEXT NOT NULL,
                        id_dati_comune TEXT NOT NULL,
                        FOREIGN KEY(id_dati_comune) REFERENCES CityData(id)
                    )
                """)
            }
        }
    }

   override fun clearAllTables() {
        adminNotificationDao().deleteAllAdminNotifications()
        banDao().deleteAllBans()
        reportDao().deleteAllReports()
        supportDao().deleteAllSupport()
        userNotificationDao().deleteAllUserNotifications()
        productDao().deleteAllProducts()
        productOrderDao().deleteAllProductOrders()
        addressDao().deleteAll()
        cardDao().deleteAllCards()
        cityDataDao().deleteAllCityData()
        followerDao().deleteAllFollowers()
        wishlistDao().deleteAllWishlist()
        wishlistProductDao().deleteAllWishlistProducts()
    }



    /*fun closeDatabase(){
        INSTANCE?.close()
        INSTANCE = null
    }da capire come gestirla*/
}