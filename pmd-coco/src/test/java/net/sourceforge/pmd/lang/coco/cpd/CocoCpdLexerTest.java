/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.coco.cpd;

import org.junit.jupiter.api.Test;

import net.sourceforge.pmd.cpd.test.CpdTextComparisonTest;
import net.sourceforge.pmd.lang.coco.CocoLanguageModule;

class CocoCpdLexerTest extends CpdTextComparisonTest {
    CocoCpdLexerTest() {
        super(CocoLanguageModule.getInstance(), ".coco");
    }

    @Test
    void testAnnotatedSource() {
        doTest("simple_machine");
    }

    @Test
    void testDocstring() {
        doTest("enum");
    }
}
