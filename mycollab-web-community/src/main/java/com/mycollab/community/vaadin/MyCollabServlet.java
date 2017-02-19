/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.vaadin;

import com.mycollab.core.Version;
import com.mycollab.vaadin.MyCollabBootstrapListener;
import com.mycollab.vaadin.MyCollabUIProvider;
import com.vaadin.addon.touchkit.annotations.CacheManifestEnabled;
import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.addon.touchkit.settings.TouchKitSettings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@WebServlet(name = "MyCollabApplication", urlPatterns = "/*", asyncSupported = true, loadOnStartup = 0, initParams =
        {@WebInitParam(name = "closeIdleSessions", value = "true"),
                @WebInitParam(name = "resourceCacheTime", value = "8640000"),
                @WebInitParam(name = "maxIdleTime", value = "10000"),
                @WebInitParam(name = "org.atmosphere.websocket.maxIdleTime", value = "86400000")})
@CacheManifestEnabled(false)
public class MyCollabServlet extends TouchKitServlet {
    private static final long serialVersionUID = 1L;

    private MyCollabUIProvider uiProvider = new MyCollabUIProvider();

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        TouchKitSettings s = getTouchKitSettings();
        s.getWebAppSettings().setWebAppCapable(true);
        String contextPath = getServletConfig().getServletContext().getContextPath();
        s.getApplicationIcons().addApplicationIcon(contextPath + "VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/icons/icon.png");
        s.getWebAppSettings().setStartupImage(contextPath + "VAADIN/themes/" + Version.THEME_MOBILE_VERSION + "/icons/icon.png");

        getService().addSessionInitListener(sessionInitEvent -> {
            sessionInitEvent.getSession().addBootstrapListener(new MyCollabBootstrapListener());
            sessionInitEvent.getSession().addUIProvider(uiProvider);
        });
    }
}
