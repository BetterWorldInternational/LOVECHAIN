package org.betterworldinternational.hugapi.util;

import org.sql2o.Sql2o;

public class DBUtil {

    private static Sql2o sql2o;

    static{ //static declaration and initialization 
        sql2o = new Sql2o("jdbc:mysql://localhost:3306/hug?useUnicode=yes&characterEncoding=UTF-8", "root", "root");
    }

    public static Sql2o sql2o() {
        return sql2o;
    }
}
