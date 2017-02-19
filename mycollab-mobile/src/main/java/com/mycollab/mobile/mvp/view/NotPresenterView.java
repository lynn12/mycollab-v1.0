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
package com.mycollab.mobile.mvp.view;

import com.mycollab.common.i18n.LicenseI18nEnum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
@ViewComponent
public class NotPresenterView extends AbstractMobilePageView {
    void display() {
        setContent(new MVerticalLayout().withFullWidth().with(new MVerticalLayout(ELabel.fontIcon(FontAwesome.WARNING),
                new ELabel(UserUIContext.getMessage(LicenseI18nEnum.FEATURE_NOT_AVAILABLE))).alignAll(Alignment.MIDDLE_CENTER)));
    }
}
