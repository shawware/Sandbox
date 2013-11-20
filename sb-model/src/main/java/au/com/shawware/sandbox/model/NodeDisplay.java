/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Utility class for displaying a node in its hierarchy.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class NodeDisplay
{
    /**
     * Creates a simple, line-by-line display of a node and
     * its ancestors and descendants.
     * 
     * @param node the node to display
     * 
     * @return the string to display
     */
    @SuppressWarnings("nls")
    public static String createDisplayString(final Node node)
    {
        final StringBuilder sb = new StringBuilder();
        if (node != null)
        {
            final List<NodeInfo> nodes = new ArrayList<NodeInfo>();

            // Determine the display level for given node.
            int nodeLevel = 0;
            Node ptr = node.getParent();
            while (ptr != null)
            {
                nodeLevel++;
                ptr = ptr.getParent();
            }

            // Add the node and its ancestors to the list.
            ptr = node;
            int level = nodeLevel;
            while (ptr != null)
            {
                nodes.add(0, new NodeInfo(level, ptr));
                ptr = ptr.getParent();
                level--;
            }

            addChildren(nodes, node.getChildren(), nodeLevel + 1);

            // Build the display string from the list.
            // Indent according to the display level. Mark the key node.
            final Iterator<NodeInfo> it = nodes.iterator();
            while (it.hasNext())
            {
                final NodeInfo ni = it.next();
                final Node n = ni.node;
                boolean oneLess = false;
                if (n == node)
                {
                    sb.append("**");
                    if (!n.isRoot())
                    {
                        sb.append("  ");
                    }
                    oneLess = true;
                }
                final int tabs = oneLess ? ni.level - 1 : ni.level;
                for (int i=0; i<tabs; i++)
                {
                    sb.append("    ");
                }
                sb.append(n.toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Recursively iterates through the given nodes and adds them to the given list.
     * 
     * @param nodes
     * @param children
     * @param level
     */
    private static void addChildren(final List<NodeInfo> nodes, final Set<Node> children, final int level)
    {
        final Iterator<Node> it = children.iterator();
        while (it.hasNext())
        {
            final Node child = it.next();
            nodes.add(new NodeInfo(level, child));
            addChildren(nodes, child.getChildren(), level + 1);
        }
    }
}

/**
 * Match a display level with a node.
 */
class NodeInfo
{
    /** The node's display level. */
    public int level;
    /** The node. */
    public Node node;
    /**
     * Construct a new info instance with the given attributes.
     * 
     * @param level the display level
     * @param node the node to display
     */
    public NodeInfo(final int level, final Node node)
    {
        this.level = level;
        this.node = node;
    }
}
