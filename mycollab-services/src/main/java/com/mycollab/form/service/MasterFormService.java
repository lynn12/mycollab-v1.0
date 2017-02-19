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
package com.mycollab.form.service;

import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IService;
import com.mycollab.form.view.builder.type.DynaForm;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface MasterFormService extends IService {
    @Cacheable
    DynaForm findCustomForm(@CacheKey Integer sAccountId, String moduleName);

    @CacheEvict
    void saveCustomForm(@CacheKey Integer sAccountId, String moduleName, DynaForm form);
}
