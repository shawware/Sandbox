/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Defines a node within a tree. Follows the Composite pattern.
 * Implemented as a simple bean / entity.
 * 
 * Note: Spring seems to have trouble if persisted attributes'
 * names don't match the method names exactly. So we avoid our
 * usual naming convention of 'mProperty' and use 'property'.
 * 
 * The underlying cause of the issue appears to be that Spring
 * uses reflection (or similar) and probes the properties
 * themselves rather than their bean method names.
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
    private Integer id;

    /** The node's activity. */
    @Column (name = "Activity", nullable = false, length = 200)
    private String activity;

    /** The node's type. */
    @Column (name = "Type", nullable = false, updatable = false)
    private NodeType type;

    /** The node's description. */
    @Column (name = "Description", nullable = false, length = 200)
    private String desc;

    /** The node's parent, <code>null</code> if root. */
    // Don't cascade on persist if you're going to save nodes with the parent set.
    @ManyToOne(optional = true, cascade = { CascadeType.REMOVE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "ParentID")
    private Node parent;

    /** The node's children, <code>null</code> or empty if leaf. */
    @OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "parent")
    private Set<Node> children;

    /**
     * Default constructor for a node.
     */
    public Node()
    {
        super();
        children = new HashSet<Node>();
    }

    /**
     * Constructs a new node where the ID is not yet known.
     * This is typically the case before the Node is stored.
     * 
     * @param activity the new node's activity
     * @param type the new node's type
     * @param desc the new node's description
     */
    public Node(final String activity, final NodeType type, final String desc)
    {
        this();
        this.activity = activity;
        this.type = type;
        this.desc = desc;
    }

    /**
     * Constructs a new node where the ID is known.
     * 
     * @param id the new node's unique identifier
     * @param activity the new node's activity
     * @param type the new node's type
     * @param desc the new node's description
     */
    public Node(final Integer id, final String activity, final NodeType type, final String desc)
    {
        this(activity, type, desc);
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
     * @return this node's activity 
     */
    public String getActivity()
    {
        return activity;
    }

    /**
     * Sets this node's activity.
     * 
     * @param activity the new activity
     */
    public void setActivity(final String activity)
    {
        this.activity = activity;
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
        return desc;
    }

    /**
     * Sets this node's description.
     * 
     * @param desc the new description
     */
    public void setDescription(final String desc)
    {
        this.desc = desc;
    }

    /**
     * @return this node's parent 
     */
    public Node getParent()
    {
        return parent;
    }

    /**
     * Sets this node's parent.
     * 
     * @param parent the new parent
     */
    public void setParent(final Node parent)
    {
        // TODO: prevent cycles and self-reference.
        // TODO: check for correct type nesting
        this.parent = parent;
    }

    /**
     * Adds the given node as a child of this one.
     * Sets the child node's parent accordingly.
     * 
     * @param child the new child node
     */
    public void addChild(final Node child)
    {
        // TODO: check if child already in this set.
        children.add(child);
        child.setParent(this);
    }

    /**
     * @return this node's children 
     */
    public Set<Node> getChildren()
    {
        return children;
    }

    /**
     * Sets this node's children.
     * 
     * @param children the new children
     */
    public void setChildren(final Set<Node> children)
    {
        // TODO: check for cycles, etc.
        // TODO: check for correct type nesting
        this.children = children;
    }

    /**
     * @return Whether this node is the root node.
     */
    @Transient
    public boolean isRoot()
    {
        return (parent == null);
    }

    /**
     * @return Whether this node is a leaf node.
     */
    @Transient
    public boolean isLeaf()
    {
        return ((children == null) || (children.size() == 0));
    }

    @Override
    @SuppressWarnings("nls")
    public String toString()
    {
        final StringBuilder s = new StringBuilder();
        s.append("Node (")
         .append(id)
         .append(", ")
         .append(activity)
         .append(", ")
         .append(type.toString())
         .append(", ")
         .append(desc)
         .append(")");
        return s.toString();
    }
}
