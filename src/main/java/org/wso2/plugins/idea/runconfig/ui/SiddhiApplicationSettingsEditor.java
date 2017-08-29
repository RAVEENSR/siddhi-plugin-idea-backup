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

package org.wso2.plugins.idea.runconfig.ui;

import com.intellij.application.options.ModulesComboBox;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.RawCommandLineEditor;
import org.wso2.plugins.idea.runconfig.SiddhiRunUtil;
import org.wso2.plugins.idea.runconfig.RunConfigurationKind;
import org.wso2.plugins.idea.runconfig.application.SiddhiApplicationConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Locale;

public class SiddhiApplicationSettingsEditor extends SettingsEditor<SiddhiApplicationConfiguration> {

    private JPanel myPanel;
    private LabeledComponent<JComboBox<RunConfigurationKind>> myRunKindComboBox;
    private LabeledComponent<TextFieldWithBrowseButton> myFileField;
    private LabeledComponent<RawCommandLineEditor> myParamsField;
    private LabeledComponent<TextFieldWithBrowseButton> myWorkingDirectoryField;
    private LabeledComponent<ModulesComboBox> myModulesComboBox;
    private LabeledComponent <TextFieldWithBrowseButton> myExtensionField;
    private Project myProject;

    public SiddhiApplicationSettingsEditor(Project project) {
        myProject = project;
        installRunKindComboBox();
        SiddhiRunUtil.installSiddhiWithMainFileChooser(project, myFileField.getComponent());
    }

    @Override
    protected void resetEditorFrom(@NotNull SiddhiApplicationConfiguration configuration) {
        myFileField.getComponent().setText(configuration.getFilePath());

        myExtensionField.getComponent().setText(configuration.getExtension());

        myRunKindComboBox.getComponent().setSelectedItem(configuration.getRunKind());

        myModulesComboBox.getComponent().setModules(configuration.getValidModules());
        myModulesComboBox.getComponent().setSelectedModule(configuration.getConfigurationModule().getModule());

        myParamsField.getComponent().setText(configuration.getParams());
        myWorkingDirectoryField.getComponent().setText(configuration.getWorkingDirectory());
    }

    @Override
    protected void applyEditorTo(@NotNull SiddhiApplicationConfiguration configuration)
            throws ConfigurationException {
        configuration.setExtension(myExtensionField.getComponent().getText());
        RunConfigurationKind runKind = (RunConfigurationKind) myRunKindComboBox.getComponent().getSelectedItem();
        configuration.setRunKind(runKind);
        configuration.setFilePath(myFileField.getComponent().getText());
        configuration.setModule(myModulesComboBox.getComponent().getSelectedModule());
        configuration.setParams(myParamsField.getComponent().getText());
        configuration.setWorkingDirectory(myWorkingDirectoryField.getComponent().getText());

        myParamsField.setVisible(true);

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

    private void createUIComponents() {
        myRunKindComboBox = new LabeledComponent<>();
        myRunKindComboBox.setComponent(new JComboBox<>());

        myFileField = new LabeledComponent<>();
        myFileField.setComponent(new TextFieldWithBrowseButton());

        myExtensionField = new LabeledComponent<>();
        myExtensionField.setComponent(new TextFieldWithBrowseButton());

        myWorkingDirectoryField = new LabeledComponent<>();
        myWorkingDirectoryField.setComponent(new TextFieldWithBrowseButton());

        myParamsField = new LabeledComponent<>();
        myParamsField.setComponent(new RawCommandLineEditor());

        myModulesComboBox = new LabeledComponent<>();
        myModulesComboBox.setComponent(new ModulesComboBox());
    }

    private static ListCellRendererWrapper<RunConfigurationKind> getRunKindListCellRendererWrapper() {
        return new ListCellRendererWrapper<RunConfigurationKind>() {
            @Override
            public void customize(JList list, @Nullable RunConfigurationKind kind, int index,
                                  boolean selected, boolean hasFocus) {
                if (kind != null) {
                    String kindName = StringUtil.capitalize(kind.toString().toLowerCase(Locale.US));
                    setText(kindName);
                }
            }
        };
    }

    private void installRunKindComboBox() {
        myRunKindComboBox.getComponent().removeAllItems();
        myRunKindComboBox.getComponent().setRenderer(getRunKindListCellRendererWrapper());
        for (RunConfigurationKind kind : RunConfigurationKind.values()) {
            myRunKindComboBox.getComponent().addItem(kind);
        }
        myRunKindComboBox.getComponent().addActionListener(e -> onRunKindChanged());
    }

    private void onRunKindChanged() {
        RunConfigurationKind selectedKind = (RunConfigurationKind) myRunKindComboBox.getComponent().getSelectedItem();
        if (selectedKind == null) {
            selectedKind = RunConfigurationKind.MAIN;
        }
        boolean isMainSelected = selectedKind == RunConfigurationKind.MAIN;
        myParamsField.setVisible(isMainSelected);
    }
}
