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

import au.com.shawware.util.MutableBoolean;

/**
 * Utility class for displaying and comparing nodes and node trees.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
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
            for (final NodeInfo ni : nodes)
            {
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
                final int tabs = ni.level - (oneLess ? 1 : 0);
                for (int i=0; i<tabs; i++)
                {
                    sb.append("    ");
                }
                sb.append(n.toString());
                sb.append(" (")
                   .append(System.identityHashCode(n))
                   .append(")");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Recursively iterates through the given nodes and adds them to the given list.
     * 
     * @param nodes the list of nodes to add to
     * @param children the set of child nodes to process
     * @param level the level the child nodes are at
     */
    private static void addChildren(final List<NodeInfo> nodes, final Set<Node> children, final int level)
    {
        for (final Node child : children)
        {
            nodes.add(new NodeInfo(level, child));
            addChildren(nodes, child.getChildren(), level + 1);
        }
    }

    /**
     * Compares to root nodes for equality and reference equality.
     * Iterates over the entire node tree.
     * If any node or reference is found to be not matching, the
     * corresponding 'out' parameter is set to <code>false</code>.
     * 
     * @param n1 the first node
     * @param n2 the second node
     * @param valuesEqual whether the node values compare equal
     * @param referencesEqual whether the node references compare equal
     * 
     * @return string representation of comparison
     */
    public static String compareRootNodes(final Node n1, final Node n2,
        final MutableBoolean valuesEqual, final MutableBoolean referencesEqual)
    {
        valuesEqual.setValue(true);
        referencesEqual.setValue(true);
        final StringBuilder sb = new StringBuilder();
        if (n1.isRoot() && n2.isRoot())
        {
            compareNodes(sb, n1, n2, valuesEqual, referencesEqual);
        }
        else
        {
            valuesEqual.setValue(false);
            referencesEqual.setValue(false);
            sb.append("Not comparing root nodes\n");
        }
        return sb.toString();
    }

    /**
     * Compares the given nodes and adds the comparison result to the given
     * string builder. Recursively calls itself on the nodes' children.
     * 
     * @param sb where to record the comparison results
     * @param n1 the first node to compare
     * @param n2 the second node to compare
     * @param valuesEqual whether the node values compare equal
     * @param referencesEqual whether the node references compare equal
     */
    private static void compareNodes(final StringBuilder sb, final Node n1, final Node n2,
        final MutableBoolean valuesEqual, final MutableBoolean referencesEqual)
    {
        sb.append("Node: ");
        if ((n1.getId() != null) && n1.getId().equals(n2.getId()))
        {
            sb.append("[")
              .append(n1.getId())
              .append("] ");
            boolean match = true;
            if (n1.getDescription().equals(n2.getDescription()))
            {
                sb.append(n1.getDescription());
            }
            else
            {
                sb.append("Mismatched descriptions: \"" + n1.getDescription() + "\", \"" + n2.getDescription() + "\"");
                match = false;
            }
            if (!n1.getActivity().equals(n2.getActivity()))
            {
                sb.append("\n\tMismatched activities: " + n1.getActivity() + ", " + n2.getActivity());
                match = false;
            }
            if (!n1.getType().equals(n2.getType()))
            {
                sb.append("\n\tMismatched types: " + n1.getType() + ", " + n2.getDescription());
                match = false;
            }
            if (match)
            {
                sb.append(" => attributes match, ");
                sb.append("object references " + ((n1 == n2) ? "match" : "do not match"));
                if (n1 != n2)
                {
                    referencesEqual.setValue(false);
                }
            }
            else
            {
                valuesEqual.setValue(false);
            }
            sb.append("\n");
            if (n1.getChildren().size() == n2.getChildren().size())
            {
                final Iterator<Node> c1 = n1.getSortedChildren();
                final Iterator<Node> c2 = n2.getSortedChildren();
                while (c1.hasNext())
                {
                    compareNodes(sb, c1.next(), c2.next(), valuesEqual, referencesEqual);
                }
            }
            else
            {
                valuesEqual.setValue(false);
                sb.append("Different number of children\n");
            }
        }
        else
        {
            valuesEqual.setValue(false);
            sb.append("Mismatched IDs: " + n1.getId() + ", " + n2.getId() + "\n");
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
