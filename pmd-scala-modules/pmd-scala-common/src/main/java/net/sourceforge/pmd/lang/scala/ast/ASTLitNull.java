/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.scala.ast;

import scala.meta.Lit;

/**
 * The ASTLitNull node implementation.
 */
public final class ASTLitNull extends AbstractScalaNode<Lit.Null> {

    ASTLitNull(Lit.Null scalaNode) {
        super(scalaNode);
    }

    @Override
    protected <P, R> R acceptVisitor(ScalaParserVisitor<? super P, ? extends R> visitor, P data) {
        return visitor.visit(this, data);
    }

    public String getValue() {
        return String.valueOf(node.value());
    }
}
