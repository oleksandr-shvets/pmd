/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.apex.ast;

import apex.jorje.semantic.ast.compilation.Compilation;

abstract class BaseApexClass<T extends Compilation> extends AbstractApexNode<T> implements ASTUserClassOrInterface<T> {

    private ApexQualifiedName qname;

    protected BaseApexClass(T node) {
        super(node);
    }

    @Override
    public boolean isFindBoundary() {
        return true;
    }

    @Override
    public String getImage() {
        return getSimpleName();
    }

    @Override
    public String getSimpleName() {
        String apexName = getDefiningType();
        return apexName.substring(apexName.lastIndexOf('.') + 1);
    }

    @Override
    public ApexQualifiedName getQualifiedName() {
        if (qname == null) {

            ASTUserClass parent = ancestors(ASTUserClass.class).first();

            if (parent != null) {
                qname = ApexQualifiedName.ofNestedClass(parent.getQualifiedName(), this);
            } else {
                qname = ApexQualifiedName.ofOuterClass(this);
            }
        }

        return qname;
    }


}
