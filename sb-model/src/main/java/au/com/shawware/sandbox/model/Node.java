/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.model;

import javax.persistence.Column;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ID")
    private Integer mID;

    /** The node's type. */
    // Naming this attribute as mType breaks autowiring this type's repository??
    @Column (name = "Type", nullable = false)
    private NodeType type;

    /** The node's description. */
    @Column (name = "Description", nullable = false, length = 200)
    private String mDesc;

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
     * @param desc the new node's description
     */
    public Node(final NodeType type, final String desc)
    {
        this();
        this.type = type;
        mDesc = desc;
    }

    /**
     * Constructs a new node where the ID is known.
     * 
     * @param id the new node's unique identifier
     * @param type the new node's type
     * @param desc the new node's description
     */
    public Node(final Integer id, final NodeType type, final String desc)
    {
        this(type, desc);
        mID = id;
    }

    /**
     * @return this node's unique identifier 
     */
    public Integer getId()
    {
        return mID;
    }

    /**
     * Sets this node's unique identifier.
     * 
     * @param id the new id
     */
    public void setId(final Integer id)
    {
        mID = id;
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

    /**
     * @return this node's description 
     */
    public String getDescription()
    {
        return mDesc;
    }

    /**
     * Sets this node's description.
     * 
     * @param desc the new description
     */
    public void setDescription(final String desc)
    {
        mDesc = desc;
    }

    @Override
    public String toString()
    {
        return "Node (" + mID + ", " + type.toString() + ", " + mDesc + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
}
