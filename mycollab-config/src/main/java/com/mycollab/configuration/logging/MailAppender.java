/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.configuration.logging;

import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import com.mycollab.configuration.EmailConfiguration;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.Version;
import com.mycollab.core.utils.StringUtils;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class MailAppender extends SMTPAppender {

    @Override
    protected Layout<ILoggingEvent> makeSubjectLayout(String subjectStr) {
        if (subjectStr == null) {
            String version;
            version = "MyCollab " + Version.getVersion();
            subjectStr = version + " - Error: %logger{20} - %m";
        }

        return super.makeSubjectLayout(subjectStr);
    }


    @Override
    public void start() {
        EmailConfiguration conf = SiteConfiguration.getEmailConfiguration();
        if (StringUtils.isBlank(conf.getHost())) {
            return;
        }

        this.setSMTPHost(conf.getHost());
        this.setSMTPPort(conf.getPort());
        this.setUsername(conf.getUser());
        this.setPassword(conf.getPassword());
        this.setSTARTTLS(conf.getIsStartTls());
        this.setFrom(SiteConfiguration.getNotifyEmail());
        this.addTo(SiteConfiguration.getSendErrorEmail());
        super.start();
    }
}