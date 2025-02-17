/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.scala;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.sourceforge.pmd.annotation.InternalApi;
import net.sourceforge.pmd.cpd.CpdLexer;
import net.sourceforge.pmd.cpd.ScalaCpdLexer;
import net.sourceforge.pmd.lang.LanguagePropertyBundle;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.impl.SimpleLanguageModuleBase;

import scala.meta.Dialect;

/**
 * Language Module for Scala.
 */
public class ScalaLanguageModule extends SimpleLanguageModuleBase {
    private static final String ID = "scala";

    /**
     * Create a new instance of Scala Language Module.
     */
    public ScalaLanguageModule() {
        super(LanguageMetadata.withId(ID).name("Scala")
                              .extensions("scala")
                              .addVersion("2.10")
                              .addVersion("2.11")
                              .addVersion("2.12")
                              .addDefaultVersion("2.13"),
              new ScalaLanguageHandler());
    }

    public static ScalaLanguageModule getInstance() {
        return (ScalaLanguageModule) LanguageRegistry.PMD.getLanguageById(ID);
    }

    @InternalApi
    public static @NonNull Dialect dialectOf(LanguageVersion v) {
        switch (v.getVersion()) {
        case "2.10": return scala.meta.dialects.package$.MODULE$.Scala210();
        case "2.11": return scala.meta.dialects.package$.MODULE$.Scala211();
        case "2.12": return scala.meta.dialects.package$.MODULE$.Scala212();
        case "2.13": return scala.meta.dialects.package$.MODULE$.Scala213();
        default:
            throw new IllegalArgumentException(v.getVersion());
        }
    }

    @Override
    public CpdLexer createCpdLexer(LanguagePropertyBundle bundle) {
        return new ScalaCpdLexer(bundle);
    }
}
