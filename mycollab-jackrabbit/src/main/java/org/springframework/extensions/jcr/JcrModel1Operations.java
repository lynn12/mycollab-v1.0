/**
 * This file is part of mycollab-jackrabbit.
 *
 * mycollab-jackrabbit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-jackrabbit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-jackrabbit.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.springframework.extensions.jcr;

import java.util.List;
import java.util.Map;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.ValueFactory;
import javax.jcr.query.QueryResult;

import org.xml.sax.ContentHandler;

/**
 * Interface used for delimiting Jcr operations based on what the underlying repository supports (in this case
 * model 1 operations). Normally not used but useful for casting to restrict access in some situations.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public interface JcrModel1Operations {

    /**
     * @see javax.jcr.Session#getAttribute(java.lang.String)
     */
    Object getAttribute(String name);

    /**
     * @see javax.jcr.Session#getAttributeNames()
     */
    String[] getAttributeNames();

    /**
     * @see javax.jcr.Session#getImportContentHandler(java.lang.String, int)
     */
    ContentHandler getImportContentHandler(String parentAbsPath, int uuidBehavior);

    /**
     * @see javax.jcr.Session#getItem(java.lang.String)
     */
    Item getItem(String absPath);

    /**
     * @see javax.jcr.Session#getNamespacePrefix(java.lang.String)
     */
    String getNamespacePrefix(String uri);

    /**
     * @see javax.jcr.Session#getNamespacePrefixes()
     */
    String[] getNamespacePrefixes();

    /**
     * @see javax.jcr.Session#getNamespaceURI(java.lang.String)
     */
    String getNamespaceURI(String prefix);

    /**
     * @see javax.jcr.Session#getNodeByUUID(java.lang.String)
     * @deprecated use {@link #getNodeByIdentifier(String)}
     */
    @Deprecated
    Node getNodeByUUID(String uuid);

    /**
     * @see javax.jcr.Session#getNodeByIdentifier(java.lang.String)
     */
    Node getNodeByIdentifier(String id);

    /**
     * @see javax.jcr.Session#getRootNode();
     */
    Node getRootNode();

    /**
     * @see javax.jcr.Session#getUserID()
     */
    String getUserID();

    /**
     * @see javax.jcr.Session#getValueFactory()
     */
    ValueFactory getValueFactory();

    /**
     * @see javax.jcr.Session#isLive()
     */
    boolean isLive();

    /**
     * @see javax.jcr.Session#itemExists(java.lang.String)
     */
    boolean itemExists(String absPath);

    /**
     * Execute a persistent query from the given node.
     * @see javax.jcr.query.QueryManager#getQuery(javax.jcr.Node)
     * @param node node to be dumped
     * @return query result
     */
    QueryResult query(Node node);

    /**
     * Execute a query with the given strings with XPATH as default language. It's the same as
     * #query(java.lang.String, java.lang.String)
     * @see javax.jcr.query.QueryManager#createQuery(java.lang.String, java.lang.String)
     * @param statement query statement
     * @return query result
     */
    QueryResult query(String statement);

    /**
     * Execute a query with the given strings.
     * @see javax.jcr.query.QueryManager#createQuery(java.lang.String, java.lang.String)
     * @param statement query statement
     * @param language language statement
     * @return query result
     */
    QueryResult query(String statement, String language);

    /**
     * Default method for doing multiple queries. It assumes the language is XPATH and that errors will not be
     * ignored.
     * @param list a list of queries that will be executed against the repository
     * @return a map containing the queries as keys and results as values
     */
    Map<String, QueryResult> query(final List<String> list);

    /**
     * Utility method for executing a list of queries against the repository. Reads the queries given and
     * returns the results in a map.
     * <p/>
     * If possible the map will be a LinkedHashSet on JDK 1.4+, otherwise LinkedHashSet from Commons
     * collections 3.1 if the package is found. If the above fails a HashMap will be returned.
     * @see org.springframework.core.CollectionFactory
     * @param list list of queries
     * @param language language of the queries. If null XPATH is assumed.
     * @param ignoreErrors if true it will populate unfound nodes with null
     * @return a map containing the queries as keys and results as values
     */
    Map<String, QueryResult> query(final List<String> list, final String language, final boolean ignoreErrors);

}