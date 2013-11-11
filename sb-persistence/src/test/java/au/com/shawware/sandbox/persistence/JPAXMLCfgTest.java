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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.com.shawware.sandbox.model.Node;
import au.com.shawware.sandbox.model.NodeType;

/**
 * Simple {@link NodeRepository} test using XML configuration.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class JPAXMLCfgTest
{
    /** The logger to use for all output. */
    private static Log sLog;

    @Autowired
    private NodeRepository mRepo;

    /**
     * Set up the test harness before running any test.
     */
    @BeforeClass
    public static void setUp()
    {
        sLog = LogFactory.getLog(JPAXMLCfgTest.class.getName());
    }

    /**
     * Verify that our basic bean works and can be loaded via the context.
     */
    @Test
    public void simpleMake()
    {
        final Node n = mRepo.save(new Node(NodeType.Local));
        Assert.assertEquals(NodeType.Local, n.getType());
        Assert.assertEquals(Integer.valueOf(1), n.getId());
        sLog.info(n);
        List<Node> nodes;
        nodes = mRepo.findByType(NodeType.Local);
        Assert.assertEquals("found a local node", 1, nodes.size()); //$NON-NLS-1$
        nodes = mRepo.findByType(NodeType.Country);
        Assert.assertEquals("did not find a country node", 0, nodes.size()); //$NON-NLS-1$
        final Node n2 = mRepo.findOne(n.getId());
        sLog.info("node lookup: " + n2); //$NON-NLS-1$
    }
}
