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

/**
 * Base class for repository unit tests.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
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
    @SuppressWarnings("nls")
    protected void testRepository(final NodeRepository repo)
    {
        final Node n = repo.save(new Node(NodeType.Country));
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
    }
}
