/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.fortran.cpd;

import org.junit.jupiter.api.Test;

import net.sourceforge.pmd.cpd.test.CpdTextComparisonTest;

/**
 * @author rpelisse
 *
 */
class FortranCpdLexerTest extends CpdTextComparisonTest {

    FortranCpdLexerTest() {
        super("fortran", ".for");
    }

    @Test
    void testSample() {
        doTest("sample");
    }
}
