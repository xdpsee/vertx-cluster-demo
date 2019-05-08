package pri.zhenhui.demo.tracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBUtils {

    private static DaoSession daoSession;
    private static SQLiteDatabase database;

    public static void initDB(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "positions.db", null);
        database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static SQLiteDatabase getDatabase() {
        return database;
    }

    public static PositionDao getPositionDao() {
        return daoSession.getPositionDao();
    }
}
