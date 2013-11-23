/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.persistence;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;

import au.com.shawware.sandbox.model.Node;
import au.com.shawware.sandbox.model.NodeDisplay;
import au.com.shawware.sandbox.model.NodeType;
import au.com.shawware.util.MutableBoolean;
import au.com.shawware.util.ValueCounter;

/**
 * Base class for repository unit tests.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
public abstract class AbstractRepositoryTest
{
    /** The logger to use for all output. */
    private Log mLog;

    /**
     * Default constructor. Setup the basics.
     */
    public AbstractRepositoryTest()
    {
        super();
        mLog = LogFactory.getLog(AbstractRepositoryTest.class.getName());
    }

    /**
     * Requires sub-classes to provide a "salt" to help distinguish test data
     * in the repository.
     * 
     * @return the sub-class's salt - must be unique across all sub-classes.
     */
    abstract protected String salt();

    /**
     * Basic tests on on the given repository.
     * 
     * @param repo the repository to use
     */
    protected void testRepository(final NodeRepository repo)
    {
        final String ACTIVITY = salt() + "-" + "Basketball";
        final Node n = repo.save(new Node(ACTIVITY, NodeType.Country, "Australia"));
        mLog.info("First node: " + n);
        Assert.assertEquals(ACTIVITY, n.getActivity());
        Assert.assertEquals(NodeType.Country, n.getType());
        List<Node> nodes;
        nodes = repo.findByActivityAndType(ACTIVITY, NodeType.Local);
        Assert.assertEquals("found a local node", 0, nodes.size());
        nodes = repo.findByActivityAndType(ACTIVITY, NodeType.Country);
        Assert.assertEquals("did not find a country node", 1, nodes.size());
        final Node n2 = repo.findOne(n.getId());
        mLog.info("node lookup: " + n2);
        repo.delete(Integer.valueOf(1));
    }

    /**
     * Creates bulk (ish) data and verifies it.
     *
     * @param repo the repository to use
     */
    protected void testBulkData(final NodeRepository repo)
    {
        final String ACTIVITY = salt() + "-" + "Football-Bulk";
        final NodeType[] types = NodeType.values();
        final ValueCounter<NodeType> counts = new ValueCounter<NodeType>();
        int i;
        for (i=0; i<types.length; i++)
        {
            counts.initialiseCount(types[i]);
        }
        final Datum[] data = new Datum[]
        {
             new Datum(NodeType.Local, "SUSFC"),
             new Datum(NodeType.Local, "BTFC"),
             new Datum(NodeType.Country, "Australia"),
             new Datum(NodeType.Region, "AFC"),
             new Datum(NodeType.World, "FIFA"),
             new Datum(NodeType.Region, "Oceania")
        };
        for (i=0; i<data.length; i++)
        {
            counts.countValue(data[i].type);
            final Node n = repo.save(new Node(ACTIVITY, data[i].type, data[i].desc));
            mLog.info(n);
        }
        for (i=0; i<types.length; i++)
        {
            final List<Node> nodes = repo.findByActivityAndType(ACTIVITY, types[i]);
            Assert.assertEquals(counts.count(types[i]), nodes.size());
            mLog.info("Found " + nodes.size() + " instance(s) of " + types[i]);
        }
    }

    /**
     * Tests the parent relationship using the given repository.
     * 
     * @param repo the repository to test with
     */
    protected void testParentRelationship(final NodeRepository repo)
    {
        final String ACTIVITY = salt() + "-" + "Football-P";
        /**
         * Test data:
         *  - the first item must be the root
         *  - we specify the parent as the node "above" with type immediately "less than" current type
         */
        final Node[] data = new Node[]
        {
             new Node(ACTIVITY, NodeType.World, "FIFA"),
             new Node(ACTIVITY, NodeType.Region, "AFC"),
             new Node(ACTIVITY, NodeType.Country, "Australia"),
             new Node(ACTIVITY, NodeType.State, "NSW"),
             new Node(ACTIVITY, NodeType.Local, "SUSFC"),
             new Node(ACTIVITY, NodeType.Local, "BTFC"),
             new Node(ACTIVITY, NodeType.State, "VIC"),
             new Node(ACTIVITY, NodeType.Local, "BFC"),
        };
        int i;
        final Node root = data[0];
        mLog.info(root);
        for (i=1; i<data.length; i++)
        {
            final Node child = data[i];
            for (int j = (i-1); j >= 0; j--)
            {
                if (data[j].getType().ordinal() == (child.getType().ordinal() - 1))
                {
                    child.setParent(data[j]);
                    mLog.info("Node[" + i + "]'s parent is Node[" + j + "]");
                    break;
                }
            }
            mLog.info(child);
        }
        final Map<Integer, Node> nodes = new HashMap<Integer, Node>();
        for (i=0; i<data.length; i++)
        {
            final Node n = repo.save(data[i]);
            mLog.info("Saved data[" + i + "]: " + n);
            nodes.put(n.getId(), n);
        }
        final Iterator<Integer> ids = nodes.keySet().iterator();
        while (ids.hasNext())
        {
            final Integer id = ids.next();
            final Node n1 = nodes.get(id);
            mLog.info("Looking for: " + n1);
            final Node n2 = repo.findOne(id);
            mLog.info("Found: " + n2);
            // TODO: write: Node.equals()?
            Assert.assertEquals(n1.getId(), n2.getId());
            Assert.assertEquals(n1.getActivity(), n2.getActivity());
            Assert.assertEquals(n1.getType(), n2.getType());
            Assert.assertEquals(n1.getDescription(), n2.getDescription());
            // Test cascade on load.
            if (!n1.isRoot())
            {
                Assert.assertEquals(n1.getParent().getId(), n2.getParent().getId());
                Assert.assertEquals(n1.getParent().getActivity(), n2.getParent().getActivity());
                Assert.assertEquals(n1.getParent().getType(), n2.getParent().getType());
                Assert.assertEquals(n1.getParent().getDescription(), n2.getParent().getDescription());
            }
            // Cascade on load "works". That is, parent nodes are loaded all the way up to the root.
            // However, if you study the actual objects, there's multiple copies created.
            // There's no efficiency or recognition they're the same.
            // The nodes will compare equal, but a lot of memory is being wasted.
        }
    }

    /**
     * Tests the child relationship using the given repository.
     * 
     * @param repo the repository to test with
     */
    @SuppressWarnings("boxing")
    protected void testChildRelationship(final NodeRepository repo)
    {
        final String ACTIVITY = salt() + "-" + "Football-C";
        /**
         * Test data:
         *  - the first item must be the root
         *  - we specify the child as the node "below" with type immediately "greater than" current type
         */
        final Node[] data = new Node[]
        {
             new Node(ACTIVITY, NodeType.World, "FIFA"),
             new Node(ACTIVITY, NodeType.Region, "AFC"),
             new Node(ACTIVITY, NodeType.Country, "Australia"),
             new Node(ACTIVITY, NodeType.State, "NSW"),
             new Node(ACTIVITY, NodeType.Local, "SUSFC"),
             new Node(ACTIVITY, NodeType.Local, "BTFC"),
             new Node(ACTIVITY, NodeType.State, "VIC"),
             new Node(ACTIVITY, NodeType.Local, "BFC"),
        };
        int i;
        final Node root = data[0];
        mLog.info(root);
        for (i=1; i<data.length; i++)
        {
            final Node child = data[i];
            for (int j = (i-1); j >= 0; j--)
            {
                if (data[j].getType().ordinal() == (child.getType().ordinal() - 1))
                {
                    data[j].addChild(child);
                    mLog.info("Node[" + j + "]'s child is Node[" + i + "]");
                    break;
                }
            }
            mLog.info(child);
        }
        // This save should cascade from the root to all nodes.
        repo.save(root);
        mLog.info("Root:\n" + NodeDisplay.createDisplayString(root));
        // This load should cascade from the root to all nodes.
        Node copy = repo.findOne(root.getId());
        mLog.info("Root(2):\n" + NodeDisplay.createDisplayString(copy));

        final MutableBoolean valuesEqual = new MutableBoolean();
        final MutableBoolean refsEqual = new MutableBoolean();
        // Comparing the root with itself matches nodes and references.
        mLog.info("Self Comparison:\n" + NodeDisplay.compareRootNodes(root, root, valuesEqual, refsEqual));
        Assert.assertThat(valuesEqual.getValue(), equalTo(true));
        Assert.assertThat(refsEqual.getValue(), equalTo(true));
        // Comparing the saved root with a loaded version of itself matches nodes, but not references.
        mLog.info("Load (1) Comparison:\n" + NodeDisplay.compareRootNodes(root, copy, valuesEqual, refsEqual));
        Assert.assertThat(valuesEqual.getValue(), equalTo(true));
        Assert.assertThat(refsEqual.getValue(), equalTo(false));
        // Comparing two loaded versions of the root matches nodes, but not references.
        mLog.info("Load (2) Comparison:\n" + NodeDisplay.compareRootNodes(copy, repo.findOne(root.getId()), valuesEqual, refsEqual));
        Assert.assertThat(valuesEqual.getValue(), equalTo(true));
        Assert.assertThat(refsEqual.getValue(), equalTo(false));
        // So there's no clever caching when we load entities using the Spring-generated repo.

        mLog.info("NSW:\n" + NodeDisplay.createDisplayString(data[3]));
        copy = repo.findOne(data[3].getId());
        mLog.info("NSW(2):\n" + NodeDisplay.createDisplayString(copy));
    }

    /**
     * Holds a test datum.
     */
    private class Datum
    {
        /* Node type datum. */
        public NodeType type;
        /* Node description datum. */
        public String desc;

        /**
         * Construct a new datum using the given data.
         * 
         * @param type node type
         * @param desc node description
         */
        public Datum(final NodeType type, final String desc)
        {
            this.type = type;
            this.desc = desc;
        }
    }
}
