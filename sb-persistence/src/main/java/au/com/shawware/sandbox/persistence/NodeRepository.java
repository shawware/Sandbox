/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import au.com.shawware.sandbox.model.Node;
import au.com.shawware.sandbox.model.NodeType;

/**
 * Simple {@link Node} Repository. We extend {@link CrudRepository}
 * to take advantage of the standard CRUD methods, as well as adding
 * our own.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public interface NodeRepository extends CrudRepository<Node, Integer>
{
    /**
     * Retrieve all the nodes with the given activity and type.
     * 
     * @param type the type of look for
     * 
     * @return the matching nodes
     */
    public List<Node> findByType(final NodeType type);

    /**
     * Retrieve all the nodes with the given activity and type.
     * 
     * @param activity the activity to look for
     * @param type the type of look for
     * 
     * @return the matching nodes
     */
    public List<Node> findByActivityAndType(final String activity, final NodeType type);
}
