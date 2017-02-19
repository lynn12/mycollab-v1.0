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

import org.junit.Assert;
import org.junit.Test;

/**
 * @author MyCollab Ltd.
 * @since 5.0.6
 */
public class VersionTest {
    @Test
    public void testHigherVersion() {
        Assert.assertFalse(Version.isEditionNewer("5.0.5", "5.0.6"));
        Assert.assertFalse(Version.isEditionNewer("5.0.5", "5.0.5"));
        Assert.assertTrue(Version.isEditionNewer("5.0.5", "5.0.4"));
        Assert.assertTrue(Version.isEditionNewer("6.0.0", "5.0.4"));
    }
}
