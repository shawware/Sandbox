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
@SuppressWarnings("nls")
public class ModelTest
{
    /* Pre-defined sport */
    private static final String FOOTBALL = "Football";
    /* Pre-defined country */
    private static final String OZ = "Australia";
    /* Pre-defined region */
    private static final String AFC = "AFC";

    /**
     * Test the node entity.
     */
    @Test
    @SuppressWarnings("static-method")
    public void nodeTest()
    {
        final Node child = new Node(Integer.valueOf(5), FOOTBALL, NodeType.Country, OZ);
        Assert.assertEquals(Integer.valueOf(5), child.getId());
        Assert.assertEquals(FOOTBALL, child.getActivity());
        Assert.assertEquals(NodeType.Country, child.getType());
        Assert.assertEquals(OZ, child.getDescription());

        final Node parent = new Node(Integer.valueOf(4), FOOTBALL, NodeType.Region, AFC);
        child.setParent(parent);
        System.out.println(parent);
        System.out.println(child);
    }
}
