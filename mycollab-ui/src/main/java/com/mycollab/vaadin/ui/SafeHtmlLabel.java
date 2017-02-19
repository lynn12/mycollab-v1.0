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
package com.mycollab.vaadin.ui;

import com.mycollab.core.utils.StringUtils;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
public class SafeHtmlLabel extends Label {
    public SafeHtmlLabel(String value) {
        super(StringUtils.formatRichText(value), ContentMode.HTML);
        this.addStyleName(UIConstants.LABEL_WORD_WRAP);
    }

    public SafeHtmlLabel(String value, int trimCharacters) {
        Document doc = Jsoup.parse(value);
        String content = doc.body().text();
        content = StringUtils.trim(content, trimCharacters);
        this.setValue(content);
        this.addStyleName(UIConstants.LABEL_WORD_WRAP);
    }
}
