/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.core.cache;

import java.lang.annotation.*;

/**
 * <b>NOTE: </b> Implement of cache just be presented in premium or ondemand
 * delivery.<br/>
 * This annotation denotes a method is cached its value base on the key compose
 * by its arguments. Mycollab cache user data base on user account value base on
 * user account
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface Cacheable {

}
