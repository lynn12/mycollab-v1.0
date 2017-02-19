/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.shell.view.components;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.CacheableComponent;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.6
 */
public abstract class AbstractAboutWindow extends MWindow implements CacheableComponent {
    public AbstractAboutWindow() {
        super(UserUIContext.getMessage(ShellI18nEnum.OPT_ABOUT_MYCOLLAB));
        this.withModal(true).withResizable(false).withWidth("600px").withCenter();
    }
}
