/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.vm.rule.design;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.vm.ast.ASTBlock;
import net.sourceforge.pmd.lang.vm.ast.ASTElseIfStatement;
import net.sourceforge.pmd.lang.vm.ast.ASTElseStatement;
import net.sourceforge.pmd.lang.vm.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.vm.ast.ASTText;
import net.sourceforge.pmd.lang.vm.ast.VmNode;
import net.sourceforge.pmd.lang.vm.rule.AbstractVmRule;

public class CollapsibleIfStatementsRule extends AbstractVmRule {

    @Override
    public Object visit(final ASTIfStatement node, final Object data) {
        handleIfElseIf(node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(final ASTElseIfStatement node, final Object data) {
        // verify that this elseif doesn't have any siblings
        if (node.getParent().children(ASTElseIfStatement.class).count() == 1) {
            handleIfElseIf(node, data);
        }
        return super.visit(node, data);
    }

    private void handleIfElseIf(final VmNode node, final Object data) {
        if (node.firstChild(ASTElseStatement.class) == null
                && node.firstChild(ASTElseIfStatement.class) == null) {
            final ASTBlock ifBlock = node.firstChild(ASTBlock.class);
            boolean violationFound = false;
            int ifCounter = 0;
            for (int i = 0; i < ifBlock.getNumChildren(); i++) {
                final Node blockChild = ifBlock.getChild(i);
                if (blockChild instanceof ASTText) {
                    if (StringUtils.isNotBlank(((ASTText) blockChild).getFirstToken().getImage())) {
                        violationFound = false;
                        break;
                    }
                } else if (blockChild instanceof ASTIfStatement) {
                    // check if it has an ELSE of ELSEIF
                    violationFound = !hasElseOrElseIf(blockChild);
                    if (!violationFound) {
                        break;
                    }
                    ifCounter++;
                } else if (blockChild instanceof ASTElseIfStatement) {
                    // check if it has an ELSE of ELSEIF
                    violationFound = !hasElseOrElseIf(blockChild);
                    if (!violationFound) {
                        break;
                    }
                    ifCounter++;
                } else {
                    // any other node - not violation
                    violationFound = false;
                    break;
                }
            }
            if (violationFound && ifCounter == 1) {
                asCtx(data).addViolation(node);
            }
        }
    }

    private boolean hasElseOrElseIf(final Node parentIfNode) {
        return parentIfNode.firstChild(ASTElseStatement.class) != null
                || parentIfNode.firstChild(ASTElseIfStatement.class) != null;
    }

}
