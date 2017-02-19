/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.core;

/**
 * Generic exception of MyCollab. All exceptions occurs in MyCollab should be
 * wrapped into this exception type.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MyCollabException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MyCollabException(String message) {
        super(message);
    }

    public MyCollabException(Throwable e) {
        super(e);
    }

    public MyCollabException(String message, Throwable e) {
        super(message, e);
    }
}
