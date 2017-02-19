/**
 * This file is part of mycollab-jackrabbit.
 *
 * mycollab-jackrabbit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-jackrabbit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-jackrabbit.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm;

import com.mycollab.configuration.DatabaseConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import org.apache.jackrabbit.core.fs.db.DbFileSystem;

/**
 * Db file system of mycollab jackrabbit storage
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DbFileSystemExt extends DbFileSystem {

    public DbFileSystemExt() {
        DatabaseConfiguration dbConf = SiteConfiguration.getDatabaseConfiguration();
        this.schema = "mysql";
        this.driver = dbConf.driverClass();
        this.user = dbConf.getUser();
        this.password = dbConf.getPassword();
        this.url = dbConf.getDbUrl();
    }
}
