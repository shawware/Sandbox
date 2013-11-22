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
    /* Pre-defined root */
    private static final String FIFA = "FIFA";
    /* Pre-defined region */
    private static final String AFC = "AFC";
    /* Pre-defined country */
    private static final String OZ = "Australia";

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
        parent.addChild(child);

        final Node fifa = new Node(Integer.valueOf(0), FOOTBALL, NodeType.World, FIFA);
        fifa.addChild(parent);

        System.out.println(NodeDisplay.createDisplayString(fifa));
        System.out.println(NodeDisplay.createDisplayString(parent));
        System.out.println(NodeDisplay.createDisplayString(child));
    }
}
