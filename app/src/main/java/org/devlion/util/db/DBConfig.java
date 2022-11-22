package org.devlion.util.db;

public class DBConfig {

    public final String DATABASE_NAME = "LOG.db";
    public final String TABLE_NAME = "TB_LOG";

    public final int _DATABASE_VERSION = 1;

    public final String LOG_ID = "LOG_ID";
    public final String LOG_DT = "LOG_DT";

    public final String CELL_PHONE = "CELL_PHONE";
    public final String COMPANY_PHONE = "COMPANY_PHONE";
    public final String COMPANY_CODE = "COMPANY_CODE";
    public final String AD_DISPLAY_NAME = "AD_DISPLAY_NAME";
    public final String CHARGE_JOB = "CHARGE_JOB";
    public final String MAIN_DEPT_CODE = "MAIN_DEPT_CODE";
    public final String COMPANY_NAME = "COMPANY_NAME";
    public final String DEPT_NAME_NAME = "DEPT_NAME_NAME";

    public String CREATE_QUERY = "create table if not exists " + TABLE_NAME + "("
            + LOG_ID + " integer primary key autoincrement, "
            + LOG_DT + " date not null, "

            + CELL_PHONE + " text, "
            + COMPANY_PHONE + " text, "
            + COMPANY_CODE + " text, "
            + AD_DISPLAY_NAME + " text, "
            + CHARGE_JOB + " text, "
            + MAIN_DEPT_CODE + " text, "
            + COMPANY_NAME + " text, "
            + DEPT_NAME_NAME + " text"
            + ");";
}
