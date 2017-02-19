/**
 * This file is part of mycollab-servlet.
 *
 * mycollab-servlet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-servlet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-servlet.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.billing.servlet;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.configuration.EnDecryptHelper;
import com.mycollab.core.InvalidPasswordException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.PasswordCheckerUtil;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.servlet.GenericHttpServlet;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@WebServlet(urlPatterns = "/user/recoverypassword/action/*", name = "updateUserPasswordServlet")
public class ResetPasswordHandler extends GenericHttpServlet {

    @Autowired
    private UserService userService;

    @Override
    protected void onHandleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            PasswordCheckerUtil.checkValidPassword(password);
        } catch (InvalidPasswordException e) {
            throw new UserInvalidInputException(e.getMessage());
        }

        User user = userService.findUserByUserName(username);
        if (user == null) {
            throw new UserInvalidInputException(LocalizationHelper.getMessage(Locale.US,
                    ErrorI18nEnum.ERROR_USER_IS_NOT_EXISTED, username));
        } else {
            user.setPassword(EnDecryptHelper.encryptSaltPassword(password));
            userService.updateWithSession(user, username);
        }
    }
}
