/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.persistence;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;

import au.com.shawware.sandbox.model.Node;
import au.com.shawware.sandbox.model.NodeType;
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
     * Basic tests on on the given repository.
     * 
     * @param repo the repository to use
     */
    protected void testRepository(final NodeRepository repo)
    {
        final Node n = repo.save(new Node(NodeType.Country, "Australia"));
        mLog.info("First node: " + n);
        Assert.assertEquals(NodeType.Country, n.getType());
        Assert.assertEquals(Integer.valueOf(1), n.getId());
        List<Node> nodes;
        nodes = repo.findByType(NodeType.Local);
        Assert.assertEquals("found a local node", 0, nodes.size());
        nodes = repo.findByType(NodeType.Country);
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
            final Node n = repo.save(new Node(data[i].type, data[i].desc));
            mLog.info(n);
        }
        for (i=0; i<types.length; i++)
        {
            final List<Node> nodes = repo.findByType(types[i]);
            Assert.assertEquals(counts.count(types[i]), nodes.size());
            mLog.info("Found " + nodes.size() + " instance(s) of " + types[i]);
        }
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
        Datum(final NodeType type, final String desc)
        {
            this.type = type;
            this.desc = desc;
        }
    }
}
