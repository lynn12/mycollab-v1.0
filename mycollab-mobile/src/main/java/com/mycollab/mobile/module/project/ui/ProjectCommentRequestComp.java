/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.ui;


import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.UI;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class ProjectCommentRequestComp extends MHorizontalLayout {
    public ProjectCommentRequestComp(final String typeVal, final String typeIdVal, final Integer extraTypeIdVal) {
        withMargin(true);
        ELabel hintLbl = ELabel.html(FontAwesome.COMMENT.getHtml() + " " + UserUIContext.getMessage(GenericI18Enum.ACTION_ADD_COMMENT))
                .withStyleName(UIConstants.META_INFO);
        this.addComponent(hintLbl);
        this.addLayoutClickListener(layoutClickEvent -> {
            ((NavigationManager) UI.getCurrent().getContent()).navigateTo(new ProjectCommentInputView(typeVal, typeIdVal, extraTypeIdVal));
        });
    }
}
