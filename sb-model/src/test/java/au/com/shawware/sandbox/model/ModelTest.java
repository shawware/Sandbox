/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for our sandbox model.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class ModelTest
{
    /**
     * Test the node entity.
     */
    @Test
    @SuppressWarnings("static-method")
    public void nodeTest()
    {
        final Node n = new Node(Integer.valueOf(5), NodeType.Country);
        System.out.println(n);
        Assert.assertEquals(Integer.valueOf(5), n.getId());
        Assert.assertEquals(NodeType.Country, n.getType());
    }
}
