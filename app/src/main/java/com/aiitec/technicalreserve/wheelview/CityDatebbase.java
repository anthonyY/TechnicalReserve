package com.aiitec.technicalreserve.wheelview;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.provider.BaseColumns;


import com.aiitec.technicalreserve.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class CityDatebbase extends SQLiteOpenHelper {

    private File file = null;
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "aiitec_city.db";
    public static String DB_PATH ;
    private SQLiteDatabase database;
    private static final int DB_VERSION = 1;
    /** 省市区表 */
    private static final String TABLE_REGION = "region";

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public static String getDbPath() {
        return DB_PATH;
    }

    private static CityDatebbase mInstance;
    private Context context;

    protected CityDatebbase(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        openDatabase();
    }

    public synchronized static CityDatebbase getInstance(Context context) {
        DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + context.getPackageName();
        if (mInstance == null) {
            mInstance = new CityDatebbase(context);
        }
        return mInstance;
    }

    public synchronized static void destoryInstance() {
        if (mInstance != null) {
            mInstance.close();
        }
    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            file = new File(dbfile);
            if (!file.exists()) {
//                InputStream is = context.getAssets().open("aiitec_open.db");
                InputStream is = context.getResources().openRawResource(
                        R.raw.aiitec_open);
                FileOutputStream fos = new FileOutputStream(dbfile);
                if (is != null) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count = 0;
                    while ((count = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                        fos.flush();
                    }
                } else {
                }
                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return database;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 查询省市区列表
     * 
     * @return 省市区列表
     */
    public synchronized ArrayList<Region> findRegions() {
        if (database == null || !database.isOpen()) {
            database = getReadableDatabase();
        }
        Cursor cr = database.query(TABLE_REGION, null, null, null, null, null,
                null);
        ArrayList<Region> regions = new ArrayList<Region>();
        if (cr.moveToFirst()) {
            do {

                String name = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.NAME));
                String pinyin = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.PINYIN));
                int id = cr.getInt(cr.getColumnIndexOrThrow(RegionField.ID));
                int parentId = cr.getInt(cr
                        .getColumnIndexOrThrow(RegionField.PARENT_ID));
                Region region = new Region();
                region.setName(name);
                region.setParentId(parentId);
                region.setPinyin(pinyin);
                region.setId(id);
                regions.add(region);
            } while (cr.moveToNext());
        }
        cr.close();
        return regions;
    }

    /**
     * 查询省市区列表
     * 
     * @return 省市区列表
     */
    public synchronized ArrayList<Region> findProvinceRegions() {
        if (database == null || !database.isOpen()) {
            database = getReadableDatabase();
        }
        Cursor cr = database.query(TABLE_REGION, null, RegionField.PARENT_ID
                + "=1", null, null, null, null);
        ArrayList<Region> regions = new ArrayList<Region>();
        if (cr.moveToFirst()) {
            do {
                String name = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.NAME));
                String pinyin = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.PINYIN));
                int id = cr.getInt(cr.getColumnIndexOrThrow(RegionField.ID));
                int parentId = cr.getInt(cr
                        .getColumnIndexOrThrow(RegionField.PARENT_ID));
                Region region = new Region();
                region.setName(name);
                region.setParentId(parentId);
                region.setPinyin(pinyin);
                region.setId(id);
                regions.add(region);

            } while (cr.moveToNext());
        }
        cr.close();
        return regions;
    }

    /**
     * 查询省市区列表
     * 
     * @return 省市区列表
     */
    public synchronized ArrayList<Region> findCityRegions(int parentId) {
        if (database == null || !database.isOpen()) {
            database = getReadableDatabase();
        }
        Cursor cr = database.query(TABLE_REGION, null, RegionField.PARENT_ID
                + "=" + parentId, null, null, null, null);
        ArrayList<Region> regions = new ArrayList<Region>();
        if (cr.moveToFirst()) {
            do {
                String name = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.NAME));
                String pinyin = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.PINYIN));
                int id = cr.getInt(cr.getColumnIndexOrThrow(RegionField.ID));
                Region region = new Region();
                region.setName(name);
                region.setParentId(parentId);
                region.setPinyin(pinyin);
                region.setId(id);
                regions.add(region);
            } while (cr.moveToNext());
        }
        cr.close();
        return regions;
    }

    /**
     * 通过id查询省市区列表
     * 
     * @return 省市区列表
     */
    public synchronized Region findRegionsFromId(int id) {
        if (database == null || !database.isOpen()) {
            database = getReadableDatabase();
        }
        Cursor cr = database.query(TABLE_REGION, null, "id=" + id, null, null,
                null, null);
        Region region = null;
        if (cr.moveToFirst()) {
            String name = cr.getString(cr
                    .getColumnIndexOrThrow(RegionField.NAME));
            String pinyin = cr.getString(cr
                    .getColumnIndexOrThrow(RegionField.PINYIN));
            int parentId = cr.getInt(cr
                    .getColumnIndexOrThrow(RegionField.PARENT_ID));
            region = new Region();
            region.setName(name);
            region.setParentId(parentId);
            region.setPinyin(pinyin);
            region.setId(id);
        }
        cr.close();
        return region;
    }

    public synchronized ArrayList<Region> findRegionsFromSearch(String keyword) {
        if (database == null || !database.isOpen()) {
            database = getReadableDatabase();
        }
        Cursor cr = database.query(TABLE_REGION, null, "(name like '%"
                + keyword + "%' or pinyin like '%" + keyword
                + "%' ) and deep=2", null, null, null, null);
        ArrayList<Region> regions = new ArrayList<Region>();
        if (cr.moveToFirst()) {
            do {
                String name = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.NAME));
                String pinyin = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.PINYIN));
                int parentId = cr.getInt(cr
                        .getColumnIndexOrThrow(RegionField.PARENT_ID));
                int id = cr.getInt(cr.getColumnIndexOrThrow(RegionField.ID));
                Region region = new Region();
                region.setName(name);
                region.setParentId(parentId);
                region.setPinyin(pinyin);
                region.setId(id);
                regions.add(region);
            } while (cr.moveToNext());
        }
        cr.close();
        return regions;
    }

    public synchronized ArrayList<Region> findAllRegions() {
        if (database == null || !database.isOpen()) {
            database = getReadableDatabase();
        }
        Cursor cr = database.query(TABLE_REGION, null, "deep=2", null, null,
                null, null);
        ArrayList<Region> regions = new ArrayList<Region>();
        if (cr.moveToFirst()) {
            do {
                String name = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.NAME));
                String pinyin = cr.getString(cr
                        .getColumnIndexOrThrow(RegionField.PINYIN));
                int parentId = cr.getInt(cr
                        .getColumnIndexOrThrow(RegionField.PARENT_ID));
                int id = cr.getInt(cr.getColumnIndexOrThrow(RegionField.ID));
                Region region = new Region();
                region.setName(name);
                region.setParentId(parentId);
                region.setPinyin(pinyin);
                region.setId(id);
                regions.add(region);
            } while (cr.moveToNext());
        }
        cr.close();
        return regions;
    }

    /**
     * 省市区缓存字段 只允许读
     * 
     * @author Anthony
     */
    public static class RegionField implements BaseColumns {
        public static final String ID = "id";
        public static final String PARENT_ID = "parent_id";
        public static final String NAME = "name";
        public static final String PINYIN = "pinyin";
        public static final String TIMESTAMP = "timestamp";
    }
}
