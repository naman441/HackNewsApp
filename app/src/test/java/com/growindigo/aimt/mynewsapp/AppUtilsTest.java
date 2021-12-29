package com.growindigo.aimt.mynewsapp;

import junit.framework.TestCase;

import java.time.LocalDate;

public class AppUtilsTest extends TestCase {

    public void testFormatDate() {
        assertEquals(AppUtils.formatDate(LocalDate.now()), "29/12/2021");
    }

    public void testGetTime() {
        assertEquals(AppUtils.getTime(1640763558), "29/12/2021");
    }
}