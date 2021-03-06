/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.persistence;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test our JPA configuration.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfiguration.class})
@Transactional()
public class JPAJavaConfigTest extends AbstractRepositoryTest
{
    /** The application context to use for all beans. */
    private static ApplicationContext sContext;
    /** The node repository to use for all transactions. */
    private static NodeRepository sRepo;

    @Override
    protected String salt()
    {
        return "Java"; //$NON-NLS-1$
    }

    /**
     * Set up the test harness before running any test.
     */
    @BeforeClass
    public static void setUp()
    {
        sContext = new AnnotationConfigApplicationContext(JPAConfiguration.class);
        sRepo = sContext.getBean(NodeRepository.class);
    }

    /**
     * Basic tests on our repository.
     */
    @Test
    public void testRepository()
    {
        testRepository(sRepo);
    }

    /**
     * Tests on bulk data.
     */
    @Test
    public void bulkDataTest()
    {
        testBulkData(sRepo);
    }

    /**
     * Tests on parent relationship.
     */
    @Test
    public void parentRelationshipTest()
    {
        testParentRelationship(sRepo);
    }

    /**
     * Tests on child relationship.
     */
    @Test
    public void childRelationshipTest()
    {
        testChildRelationship(sRepo);
    }
}
