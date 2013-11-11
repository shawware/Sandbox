/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for Spring classes.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings("nls")
public class BeanTest
{
    /** The name of the file describing the beans being tested. */
    private static final String BEAN_FILE = "beans.xml"; //$NON-NLS-1$
    /** The logger to use for all output. */
    private static Log sLog;
    /** The application context to use for all beans. */
    private static ApplicationContext sContext;

    /**
     * Set up the test harness before running any test.
     */
    @BeforeClass
    public static void setUp()
    {
        sLog = LogFactory.getLog(BeanTest.class.getName());
        sContext = new ClassPathXmlApplicationContext(BEAN_FILE);
    }

    /**
     * Verify that our basic bean works and can be loaded via the context.
     */
    @Test
    public void basicBean()
    {
        final BasicBean b1 = testBasicBean("b1", true, true, 7, "Constructor Arg By Type");
        final BasicBean b2 = testBasicBean("b2", false, false, 11, "Setters");

        Assert.assertNotEquals(b1, b2);
    }

    /**
     * Tests a specific basic bean is configured correctly.
     * 
     * @param id the bean identifier
     * @param isSingleton whether the bean has singleton (or prototype) scope
     * @param isByConstructor whether the bean is injected via constructor (or setters)
     * @param expectedValue the expected value
     * @param expectedName the expected name
     * 
     * @return the bean with the given identifier
     */
    @SuppressWarnings({ "boxing" })
    private BasicBean testBasicBean(final String id, final boolean isSingleton,
        final boolean isByConstructor, final int expectedValue, final String expectedName)
    {
        final BasicBean b = sContext.getBean(id, BasicBean.class);
        verifyBasicBean(b, isByConstructor, expectedValue, expectedName);
        sLog.info(b);

        final BasicBean bb = sContext.getBean(id, BasicBean.class);
        verifyBasicBean(bb, isByConstructor, expectedValue, expectedName);
        sLog.info(bb);

        final BasicBean ba = b;
        Assert.assertTrue(b == ba);
        Assert.assertEquals(isSingleton, b == bb);
        Assert.assertEquals(ba, bb);

        return b;
    }

    /**
     * Test our compound bean is configured correctly.
     */
    @Test
    public void compositeBean()
    {
        final CompositeBean cb = sContext.getBean("cb", CompositeBean.class);
        sLog.info("cb.id:" + cb.getId());
        verifyBasicBean(cb.getId(), false, 31, "Composite ID");
        sLog.info("cb.idref:" + cb.getIdRef());
        Assert.assertEquals("ref-bean", cb.getIdRef());
        sLog.info("cb.inner: " + cb.getInner());
        verifyBasicBean(cb.getInner(), false, 43, "Inner Bean");
        sLog.info("cb.injected: " + cb.getInjected());
        verifyBasicBean(cb.getInjected(), false, 19, "Injected Bean");
        sLog.info("cb.compound: " + cb.getCompound());
        Assert.assertEquals("Something else", cb.getCompound().getName());
        sLog.info("cb.numbers: " + cb.getNumbers());
        Assert.assertEquals("[7, 17, 37]", cb.getNumbers().toString());
    }

    /**
     * Verify the given bean is as expected.
     * 
     * @param bean the bean to test
     * @param isByConstructor whether the bean was injected via constructor
     * @param expectedValue the expected value
     * @param expectedName the expected name
     */
    @SuppressWarnings("static-method")
    private void verifyBasicBean(final BasicBean bean, final boolean isByConstructor,
        final int expectedValue, final String expectedName)
    {
        Assert.assertEquals(expectedValue, bean.getValue());
        Assert.assertEquals(expectedName, bean.getName());
        Assert.assertTrue(isByConstructor == bean.isInitialisedByConstructor());
    }
}
