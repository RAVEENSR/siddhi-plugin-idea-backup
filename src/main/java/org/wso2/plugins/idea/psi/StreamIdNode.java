package org.wso2.plugins.idea.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.adaptor.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;
import org.wso2.plugins.idea.SiddhiTypes;

public class StreamIdNode extends IdentifierDefSubtree {

    public StreamIdNode(@NotNull ASTNode node) {
        super(node, SiddhiTypes.IDENTIFIER);
    }
}
