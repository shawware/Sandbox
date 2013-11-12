/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Simple {@link NodeRepository} test using XML configuration.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class JPAXMLCfgTest extends AbstractRepositoryTest
{
    @Autowired
    private NodeRepository mRepo;

    /**
     * Verify that our basic bean works and can be loaded via the context.
     */
    @Test
    public void simpleMake()
    {
        testRepository(mRepo);
    }
}