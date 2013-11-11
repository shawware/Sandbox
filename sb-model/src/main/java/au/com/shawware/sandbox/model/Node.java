/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Defines a node within a tree. Follows the Composite pattern.
 * Implemented as a simple bean / entity.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Entity
public class Node
{
    /** The node's unique identifier. */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /** The node's type. */
    private NodeType type;

    /**
     * Default constructor for a node.
     */
    public Node()
    {
        super();
    }

    /**
     * Constructs a new node where the ID is not yet known.
     * This is typically the case before the Node is stored.
     * 
     * @param type the new node's type
     */
    public Node(final NodeType type)
    {
        this();
        this.type = type;
    }

    /**
     * Constructs a new node where the ID is known.
     * 
     * @param id the new node's unique identifier
     * @param type the new node's type
     */
    public Node(final Integer id, final NodeType type)
    {
        this(type);
        this.id = id;
    }

    /**
     * @return this node's unique identifier 
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets this node's unique identifier.
     * 
     * @param id the new id
     */
    public void setId(final Integer id)
    {
        this.id = id;
    }

    /**
     * @return this node's type 
     */
    public NodeType getType()
    {
        return type;
    }

    /**
     * Sets this node's type.
     * 
     * @param type the new type
     */
    public void setType(final NodeType type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Node (" + id + ", " + type.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
