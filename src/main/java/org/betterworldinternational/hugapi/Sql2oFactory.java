package org.betterworldinternational.hugapi;

import org.sql2o.Sql2o;

class Sql2oFactory {
    public Sql2o create() {
        return new Sql2o("jdbc:mysql://localhost:3306/hug?useUnicode=yes&characterEncoding=UTF-8", "root", "root");
    }
}