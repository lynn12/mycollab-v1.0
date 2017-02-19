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
package com.mycollab.shell.view;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Resource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ForgotPasswordViewImpl extends AbstractVerticalPageView implements ForgotPasswordView {
    private static final long serialVersionUID = 1L;

    public ForgotPasswordViewImpl() {
        this.addComponent(new ForgotPwdForm());
    }

    private class ForgotPwdForm extends CustomComponent {
        private static final long serialVersionUID = 1L;
        private final TextField nameOrEmailField;

        ForgotPwdForm() {
            CustomLayout customLayout = CustomLayoutExt.createLayout("forgotPassword");
            customLayout.setStyleName("forgotPwdForm");

            Resource logoResource = AccountAssetsResolver.createLogoResource(MyCollabUI.getBillingAccount().getLogopath(), 150);
            customLayout.addComponent(new Image(null, logoResource), "logo-here");

            customLayout.addComponent(new ELabel(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(),
                    ShellI18nEnum.BUTTON_FORGOT_PASSWORD)), "formHeader");
            customLayout.addComponent(ELabel.html(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(),
                    ShellI18nEnum.OPT_FORGOT_PASSWORD_INTRO)), "intro-text");
            nameOrEmailField = new TextField(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), GenericI18Enum.FORM_EMAIL));
            customLayout.addComponent(nameOrEmailField, "nameoremail");

            MButton sendEmail = new MButton(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), ShellI18nEnum.BUTTON_RESET_PASSWORD), clickEvent -> {
                String username = nameOrEmailField.getValue();
                if (StringUtils.isValidEmail(username)) {
                    UserService userService = AppContextUtil.getSpringBean(UserService.class);
                    User user = userService.findUserByUserName(username);

                    if (user == null) {
                        NotificationUtil.showErrorNotification(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), GenericI18Enum.ERROR_USER_IS_NOT_EXISTED, username));
                    } else {
                        userService.requestToResetPassword(user.getUsername());
                        NotificationUtil.showNotification(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), GenericI18Enum.OPT_SUCCESS),
                                LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), ShellI18nEnum.OPT_EMAIL_SENDER_NOTIFICATION));
                        EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null));
                    }
                } else {
                    NotificationUtil.showErrorNotification(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), ErrorI18nEnum.NOT_VALID_EMAIL, username));
                }
            }).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);
            customLayout.addComponent(sendEmail, "loginButton");

            MButton memoBackBtn = new MButton(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), ShellI18nEnum.BUTTON_IGNORE_RESET_PASSWORD),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null)))
                    .withStyleName(WebThemes.BUTTON_LINK);
            customLayout.addComponent(memoBackBtn, "forgotLink");

            this.setCompositionRoot(customLayout);
        }
    }
}
