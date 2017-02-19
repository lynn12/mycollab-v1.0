/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.spring;

import com.mycollab.configuration.DatabaseConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Configuration
@Profile("production")
@EnableTransactionManagement
public class DataSourceConfiguration {

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DatabaseConfiguration dbConf = SiteConfiguration.getDatabaseConfiguration();
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(dbConf.getDriverClass());
        dataSource.setJdbcUrl(dbConf.getDbUrl());
        dataSource.setUsername(dbConf.getUser());
        dataSource.setPassword(dbConf.getPassword());

        Properties dsProperties = new Properties();
        dsProperties.setProperty("cachePrepStmts", "true");
        dsProperties.setProperty("prepStmtCacheSize", "250");
        dsProperties.setProperty("prepStmtCacheSqlLimit", "2048");
        dsProperties.setProperty("useServerPrepStmts", "true");
        dsProperties.setProperty("maximumPoolSize", "20");
        dsProperties.setProperty("initializationFailFast", "false");
        dataSource.setDataSourceProperties(dsProperties);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
