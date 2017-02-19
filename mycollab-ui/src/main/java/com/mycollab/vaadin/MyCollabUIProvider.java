/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin;

import com.mycollab.core.MyCollabException;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class MyCollabUIProvider extends UIProvider {
    private static final long serialVersionUID = 1L;

    static final String MOBILE_APP = "com.mycollab.mobile.MobileApplication";
    static final String DESKTOP_APP = "com.mycollab.web.DesktopApplication";

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        VaadinRequest request = event.getRequest();
        String uiClass;
        String userAgent;

        try {
            userAgent = request.getHeader("user-agent").toLowerCase();
        } catch (Exception e) {
            return null;
        }

        uiClass = userAgent.contains("mobile") ? MOBILE_APP : DESKTOP_APP;

        try {
            return (Class<? extends UI>) Class.forName(uiClass);
        } catch (ClassNotFoundException e) {
            throw new MyCollabException(e);
        }
    }
}
