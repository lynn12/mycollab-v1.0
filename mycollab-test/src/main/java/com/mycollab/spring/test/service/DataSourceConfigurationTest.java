/**
 * This file is part of mycollab-test.
 *
 * mycollab-test is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-test is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-test.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.spring.test.service;

import com.mycollab.test.service.DataSourceFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@Configuration
@Profile("test")
@MapperScan("com.mycollab.**.dao")
public class DataSourceConfigurationTest {

    @Bean
    public DataSource dataSource() {
        return new DataSourceFactoryBean().getDataSource();
    }

    @Bean
    public DataSourceTransactionManager txManager() {
        DataSourceTransactionManager bean = new DataSourceTransactionManager();
        bean.setDataSource(dataSource());
        return bean;
    }
}
