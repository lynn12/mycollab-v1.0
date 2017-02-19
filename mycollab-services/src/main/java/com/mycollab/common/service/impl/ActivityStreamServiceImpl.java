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
package com.mycollab.common.service.impl;

import com.mycollab.common.dao.ActivityStreamMapper;
import com.mycollab.common.dao.ActivityStreamMapperExt;
import com.mycollab.common.domain.ActivityStreamWithBLOBs;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.common.service.ActivityStreamService;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class ActivityStreamServiceImpl extends DefaultService<Integer, ActivityStreamWithBLOBs, ActivityStreamSearchCriteria>
        implements ActivityStreamService {

    @Autowired
    private ActivityStreamMapper activityStreamMapper;

    @Autowired
    private ActivityStreamMapperExt activityStreamMapperExt;

    @Override
    public ICrudGenericDAO<Integer, ActivityStreamWithBLOBs> getCrudMapper() {
        return activityStreamMapper;
    }

    @Override
    public ISearchableDAO<ActivityStreamSearchCriteria> getSearchMapper() {
        return activityStreamMapperExt;
    }

    @Override
    public Integer save(ActivityStreamWithBLOBs activityStream) {
        activityStreamMapper.insertAndReturnKey(activityStream);
        return activityStream.getId();
    }
}