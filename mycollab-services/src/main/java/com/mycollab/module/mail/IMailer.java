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
package com.mycollab.module.mail;

import com.mycollab.common.domain.MailRecipientField;
import com.mycollab.module.user.domain.SimpleUser;

import java.util.List;

/**
 * Abstract mailer. Note: in MyCollab we now support send HTML content email
 * only.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface IMailer {
    /**
     * @param fromEmail
     * @param fromName
     * @param toEmails
     * @param ccEmails
     * @param bccEmails
     * @param subject
     * @param html
     */
    void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmails, List<MailRecipientField> ccEmails,
                      List<MailRecipientField> bccEmails, String subject, String html);

    /**
     * @param fromEmail
     * @param fromName
     * @param toEmails
     * @param ccEmails
     * @param bccEmails
     * @param subject
     * @param html
     * @param attachments
     */
    void sendHTMLMail(String fromEmail, String fromName, List<MailRecipientField> toEmails, List<MailRecipientField> ccEmails,
                      List<MailRecipientField> bccEmails, String subject, String html, List<? extends AttachmentSource> attachments);
}
