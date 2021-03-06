/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.plugins.idea.debugger;

import com.intellij.util.containers.ContainerUtil;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XSuspendContext;
import org.wso2.plugins.idea.debugger.dto.Frame;
import org.wso2.plugins.idea.debugger.dto.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SiddhiSuspendContext extends XSuspendContext {

    @NotNull
    private final SiddhiExecutionStack myStack;

    public SiddhiSuspendContext(@NotNull SiddhiDebugProcess process, @NotNull Message message) {
        myStack = new SiddhiExecutionStack(process, message.getThreadId(), message.getFrames());
    }

    @Nullable
    @Override
    public XExecutionStack getActiveExecutionStack() {
        return myStack;
    }

    @NotNull
    @Override
    public XExecutionStack[] getExecutionStacks() {
        return new XExecutionStack[]{myStack};
    }

    static class SiddhiExecutionStack extends XExecutionStack {

        private final String threadId;
        @NotNull
        private final SiddhiDebugProcess myProcess;
        @NotNull
        private final List<SiddhiStackFrame> myStack;

        public SiddhiExecutionStack(@NotNull SiddhiDebugProcess process, String threadId, List<Frame> frames) {
            super("Thread #" + threadId);
            this.threadId = threadId;
            this.myProcess = process;
            this.myStack = ContainerUtil.newArrayListWithCapacity(frames.size());
            for (Frame frame : frames) {
                myStack.add(new SiddhiStackFrame(myProcess, frame));
            }
        }

        @Nullable
        @Override
        public XStackFrame getTopFrame() {
            return ContainerUtil.getFirstItem(myStack);
        }

        @Override
        public void computeStackFrames(int firstFrameIndex, @NotNull XStackFrameContainer container) {
            container.addStackFrames(myStack, true);
        }

        public String getThreadId() {
            return threadId;
        }
    }
}
