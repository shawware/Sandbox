/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.spring;

import java.beans.ConstructorProperties;

/**
 * A basic bean with fundamental attributes to be injected into others.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public class BasicBean
{
    /** Whether this bean is constructed by constructor or setter injection. */
    private final boolean mInitialisedByConstructor;

    /** The bean's value. */
    private int mValue;
    /** The bean's name. */
    private String mName;

    /**
     * Default constructor.
     */
    public BasicBean()
    {
        super();

        mInitialisedByConstructor = false;
    }

    /**
     * Construct a new bean (using constructor injection).
     * 
     * @param value the new bean's value
     * @param name the new bean's name
     */
    @ConstructorProperties({"value", "name"})
    public BasicBean(final int value, final String name)
    {
        super();

        mValue = value;
        mName = name;

        mInitialisedByConstructor = true;
    }

    /**
     * @return Whether this bean was initialised by constructor injection.
     */
    public boolean isInitialisedByConstructor()
    {
        return mInitialisedByConstructor;
    }

    /**
     * @return this bean's value
     */
    public int getValue()
    {
        return mValue;
    }

    /**
     * Sets this bean's value.
     * 
     * @param value the new value
     */
    public void setValue(final int value)
    {
        mValue = value;
    }

    /**
     * @return this bean's value
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Sets this bean's name.
     * 
     * @param name the new name
     */
    public void setName(final String name)
    {
        mName = name;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mName == null) ? 0 : mName.hashCode());
        result = prime * result + mValue;
        return result;
    }

    @Override
    public boolean equals(final Object that)
    {
        if (this == that)
        {
            return true;
        }
        if (that == null)
        {
            return false;
        }
        if (getClass() != that.getClass())
        {
            return false;
        }
        final BasicBean other = (BasicBean) that;
        if (mValue != other.mValue)
        {
            return false;
        }
        if (mName == null)
        {
            if (other.mName != null)
            {
                return false;
            }
        }
        else if (!mName.equals(other.mName))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "BasicBean(" + mValue + ", " + mName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
