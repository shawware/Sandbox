/*
 * Copyright (C) 2013 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * http://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.sandbox.model;

/**
 * Enumerates the various types of {@link Node}.
 * Each type nests within the previous type, with 'World' at the root.
 * Therefore the ordinal values can be used to make useful comparisons.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
public enum NodeType
{
    /** The root type. */
    World,
    /** Break the world into distinct regions. */
    Region,
    /** Nation */
    Country,
    /** Standard first-level divisions within a nation. */
    State,
    /** Division within a country's state. Locale dependent. */
    Local
}
