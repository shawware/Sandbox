/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.spring;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * A composite bean with other beans as attributes.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class CompositeBean
{
    /** The identity of this composite bean. */
    private BasicBean mId;
    /** A reference to another bean. */
    private String mIdRef;
    /** An inner bean. */
    private BasicBean mInner;
    /** A bean injected by annotation. */
    private BasicBean mInjected;
    /** A bean, created internally, but available for compound name use. */
    private final BasicBean mCompound;
    /** A list of numbers - to test collections. */
    private List<Integer> mNumbers;

    /**
     * Default constructor.
     */
    public CompositeBean()
    {
        super();
        mCompound = new BasicBean(29, "Replace me!"); //$NON-NLS-1$
        mNumbers = new ArrayList<Integer>(); // Create an empty list.
    }

    /**
     * @return this bean's ID
     */
    public BasicBean getId()
    {
        return mId;
    }

    /**
     * Sets this bean's ID.
     * 
     * @param id the new ID
     */
    public void setId(final BasicBean id)
    {
        mId = id;
    }

    /**
     * @return this bean's ID ref
     */
    public String getIdRef()
    {
        return mIdRef;
    }

    /**
     * Sets this bean's ID ref.
     * 
     * @param idRef the new ID ref
     */
    public void setIdRef(final String idRef)
    {
        mIdRef = idRef;
    }

    /**
     * @return this bean's inner bean
     */
    public BasicBean getInner()
    {
        return mInner;
    }

    /**
     * Sets this bean's inner bean.
     * 
     * @param inner the new inner bean
     */
    public void setInner(final BasicBean inner)
    {
        mInner = inner;
    }

    /**
     * @return this bean's injected bean
     */
    public BasicBean getInjected()
    {
        return mInjected;
    }

    /**
     * Sets this bean's injected bean.
     * 
     * @param injected the new injected bean
     */
    @Inject
    public void setInjected(@Named("injectee") final BasicBean injected)
    {
        mInjected = injected;
    }

    /**
     * @return this bean's compound bean
     */
    public BasicBean getCompound()
    {
        return mCompound;
    }

    /**
     * @return this bean's numbers
     */
    public List<Integer> getNumbers()
    {
        return mNumbers;
    }

    /**
     * Set this bean's numbers.
     * 
     * @param numbers the new numbers
     */
    public void setNumbers(final List<Integer> numbers)
    {
        mNumbers = numbers;
    }
}
