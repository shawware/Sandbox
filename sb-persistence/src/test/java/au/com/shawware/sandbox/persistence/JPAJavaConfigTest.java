/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.persistence;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import au.com.shawware.sandbox.model.Node;
import au.com.shawware.sandbox.model.NodeType;

/**
 * Test our JPA configuration.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfiguration.class})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class JPAJavaConfigTest
{
    /** The logger to use for all output. */
    private static Log sLog;
    /** The application context to use for all beans. */
    private static ApplicationContext sContext;
    /** The node repository to use for all transactions. */
    private static NodeRepository sRepo;

    @Autowired
    private EntityManager mManager;

    /**
     * Set up the test harness before running any test.
     */
    @BeforeClass
    public static void setUp()
    {
        sLog = LogFactory.getLog(JPAXMLCfgTest.class.getName());
        sContext = new AnnotationConfigApplicationContext(JPAConfiguration.class);
        sRepo = sContext.getBean(NodeRepository.class);
    }

    /**
     * Test for the presences of the entity manager.
     */
    @Test
    public void testManager()
    {
        Assert.assertNotNull(mManager);
    }

    /**
     * Basic tests on our repository.
     */
    @Test
    @SuppressWarnings("static-method")
    public void testRepository()
    {
        final Node n = sRepo.save(new Node(NodeType.Country));
        sLog.info("First node: " + n); //$NON-NLS-1$
        Assert.assertEquals(NodeType.Country, n.getType());
        Assert.assertEquals(Integer.valueOf(1), n.getId());
        List<Node> nodes;
        nodes = sRepo.findByType(NodeType.Local);
        Assert.assertEquals("found a local node", 0, nodes.size()); //$NON-NLS-1$
        nodes = sRepo.findByType(NodeType.Country);
        Assert.assertEquals("did not find a country node", 1, nodes.size()); //$NON-NLS-1$
        final Node n2 = sRepo.findOne(n.getId());
        sLog.info("node lookup: " + n2); //$NON-NLS-1$
    }
}
