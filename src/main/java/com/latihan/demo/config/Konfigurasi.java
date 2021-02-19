package com.latihan.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@Configuration
public class Konfigurasi {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "sql2oo")
    public Sql2o  getSql2o() {
        return new Sql2o(dataSource);
    }

}
