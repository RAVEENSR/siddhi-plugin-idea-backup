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

package org.wso2.plugins.idea.codeInsight.imports;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "Siddhi",
        storages = @Storage(file = StoragePathMacros.APP_CONFIG + "/editor.codeInsight.xml")
)
public class SiddhiCodeInsightSettings implements PersistentStateComponent<SiddhiCodeInsightSettings> {

    private boolean myShowImportPopup = true;
    private boolean myAddUnambiguousImportsOnTheFly = false;

    public static SiddhiCodeInsightSettings getInstance() {
        return ServiceManager.getService(SiddhiCodeInsightSettings.class);
    }

    @Nullable
    @Override
    public SiddhiCodeInsightSettings getState() {
        return this;
    }

    @Override
    public void loadState(SiddhiCodeInsightSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isShowImportPopup() {
        return myShowImportPopup;
    }

    public void setShowImportPopup(boolean showImportPopup) {
        myShowImportPopup = showImportPopup;
    }

    public boolean isAddUnambiguousImportsOnTheFly() {
        return myAddUnambiguousImportsOnTheFly;
    }

    public void setAddUnambiguousImportsOnTheFly(boolean addUnambiguousImportsOnTheFly) {
        myAddUnambiguousImportsOnTheFly = addUnambiguousImportsOnTheFly;
    }
}
