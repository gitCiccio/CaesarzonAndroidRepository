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
    version = 5, exportSchema = false)
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
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
        val MIGRATION_4_5: Migration = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Step 1: Create a new table without the foreign key constraint
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `foto_prodotti` (
                `id_foto_prodotto` TEXT PRIMARY KEY NOT NULL,
                `id_prodotto` TEXT NOT NULL,
                `immagine` BLOB NOT NULL
            )
            """
                )

                // Step 2: Copy the data from the old table to the new table
                db.execSQL(
                    """
            INSERT INTO `new_foto_prodotti` (id_foto_prodotto, id_prodotto, immagine)
            SELECT id_foto_prodotto, id_prodotto, immagine
            FROM `foto_prodotti`
            """
                )

                // Step 3: Drop the old table
                db.execSQL("DROP TABLE `foto_prodotti`")

                // Step 4: Rename the new table to the old table's name
                db.execSQL("ALTER TABLE `new_foto_prodotti` RENAME TO `foto_prodotti`")

                // Step 5: Optionally, create any necessary indexes
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_foto_prodotti_id_prodotto` ON `foto_prodotti` (`id_prodotto`)")
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