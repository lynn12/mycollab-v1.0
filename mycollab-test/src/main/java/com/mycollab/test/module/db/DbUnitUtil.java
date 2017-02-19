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
package com.mycollab.test.module.db;

import com.mycollab.test.service.DataSourceFactoryBean;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DbUnitUtil {

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceFactoryBean().getDataSource();
        File file = new File("src/main/resources/mycollab.dtd");
        IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
        connection.getConfig().setProperty(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
        // write DTD file
        FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream(file));
    }
}
