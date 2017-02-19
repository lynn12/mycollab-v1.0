/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.html

import com.hp.gagawa.java.elements.Img
import com.mycollab.configuration.StorageFactory

/**
  * @author MyCollab Ltd.
  * @since 4.6.0
  */
object LinkUtils {
  
  def newAvatar(avatarId: String) = new Img("", StorageFactory.getAvatarPath(avatarId, 16)).setWidth("16").
    setHeight("16").setStyle("display: inline-block; vertical-align: top;").setCSSClass("circle-box")
  
  def accountLogoPath(accountId: Integer, logoId: String) = if (logoId == null) StorageFactory.generateAssetRelativeLink("icons/logo.png")
  else StorageFactory.getLogoPath(accountId, logoId, 150)
}
