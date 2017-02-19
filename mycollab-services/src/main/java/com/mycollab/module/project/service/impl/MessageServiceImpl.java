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
package com.mycollab.module.project.service.impl;

import com.google.common.eventbus.AsyncEventBus;
import com.mycollab.aspect.ClassInfo;
import com.mycollab.aspect.ClassInfoMap;
import com.mycollab.aspect.Traceable;
import com.mycollab.cache.CleanCacheEvent;
import com.mycollab.common.ModuleNameConstants;
import com.mycollab.core.cache.CleanCache;
import com.mycollab.db.persistence.ICrudGenericDAO;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultService;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.dao.MessageMapper;
import com.mycollab.module.project.dao.MessageMapperExt;
import com.mycollab.module.project.domain.Message;
import com.mycollab.module.project.domain.SimpleMessage;
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.mycollab.module.project.esb.DeleteProjectMessageEvent;
import com.mycollab.module.project.service.MessageService;
import com.mycollab.module.project.service.ProjectActivityStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
@Transactional
@Traceable(nameField = "title", extraFieldName = "projectid")
public class MessageServiceImpl extends DefaultService<Integer, Message, MessageSearchCriteria> implements MessageService {

    static {
        ClassInfoMap.put(MessageServiceImpl.class, new ClassInfo(ModuleNameConstants.PRJ, ProjectTypeConstants.MESSAGE));
    }

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageMapperExt messageMapperExt;

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Override
    public ICrudGenericDAO<Integer, Message> getCrudMapper() {
        return messageMapper;
    }

    @CleanCache
    public void postDirtyUpdate(Integer sAccountId) {
        asyncEventBus.post(new CleanCacheEvent(sAccountId, new Class[]{ProjectActivityStreamService.class}));
    }

    @Override
    public void massRemoveWithSession(List<Message> items, String username, Integer accountId) {
        super.massRemoveWithSession(items, username, accountId);
        DeleteProjectMessageEvent event = new DeleteProjectMessageEvent(items.toArray(new Message[items.size()]),
                username, accountId);
        asyncEventBus.post(event);
    }

    @Override
    public ISearchableDAO<MessageSearchCriteria> getSearchMapper() {
        return messageMapperExt;
    }

    @Override
    public SimpleMessage findById(Integer messageId, Integer sAccountId) {
        return messageMapperExt.findMessageById(messageId);
    }
}
