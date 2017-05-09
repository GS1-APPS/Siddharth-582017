package org.gs1us.sgg.gbservice.api;

public enum ImportPrevalidationColumnStatus
{
    /** A column with no value in any row */
    EMPTY,
    /** A column with a value in some, but not all rows */
    PARTIAL,
    /** A column with a value in every row */
    FULL
}
