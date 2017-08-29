///*
// *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *  http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//
//package org.wso2.plugins.idea.util;
//
//import com.intellij.openapi.module.Module;
//import com.intellij.openapi.module.ModuleUtilCore;
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.projectRoots.Sdk;
//import com.intellij.openapi.roots.ModuleRootManager;
//import com.intellij.openapi.roots.OrderRootType;
//import com.intellij.openapi.roots.ProjectRootManager;
//import com.intellij.openapi.vfs.VirtualFile;
//import com.intellij.psi.PsiDirectory;
//import com.intellij.psi.PsiManager;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//public class SiddhiUtil {
//
//    private SiddhiUtil() {
//
//    }
//
//    /**
//     * Returns the first library root found in the project.
//     *
//     * @param project project to find the library root
//     * @return first library root
//     */
//    public static String getLibraryRoot(Project project) {
//        Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
//        if (projectSdk != null) {
//            VirtualFile[] roots = projectSdk.getSdkModificator().getRoots(OrderRootType.SOURCES);
//            for (VirtualFile root : roots) {
//                return root.getPath();
//            }
//        }
//        return "";
//    }
//
//    /**
//     * Returns whether the given file is a library file or not.
//     *
//     * @param project     project to get the library roots
//     * @param virtualFile file to find in the library
//     * @return {@code true} if the given file is in the libraries. {@code false} otherwise.
//     */
//    public static boolean isLibraryFile(Project project, VirtualFile virtualFile) {
//        Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
//        String path = virtualFile.getPath();
//        if (projectSdk != null) {
//            VirtualFile[] roots = projectSdk.getSdkModificator().getRoots(OrderRootType.SOURCES);
//            for (VirtualFile root : roots) {
//                if (path.startsWith(root.getPath())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Returns whether the given file is a workspace file or not.
//     *
//     * @param project     project to find the file
//     * @param virtualFile file to find in the project
//     * @return {@code true} if the given file is in the project. {@code false} otherwise.
//     */
//    public static boolean isWorkspaceFile(Project project, VirtualFile virtualFile) {
//        String filePath = virtualFile.getPath();
//        if (filePath.startsWith(project.getBasePath())) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Suggest the package name for a directory.
//     *
//     * @param directory directory which needs to be processed
//     * @return suggested package name or empty string if package name cannot be determined.
//     */
//    @NotNull
//    public static String suggestPackageNameForDirectory(@Nullable PsiDirectory directory) {
//        // If the directory is not null, get the package name
//        if (directory != null) {
//            VirtualFile virtualFile = directory.getVirtualFile();
//            Project project = directory.getProject();
//            // Check directories in content roots.
//            VirtualFile[] contentRoots = ProjectRootManager.getInstance(project).getContentRoots();
//            for (VirtualFile contentRoot : contentRoots) {
//                if (!directory.getVirtualFile().getPath().startsWith(contentRoot.getPath())) {
//                    continue;
//                }
//                return getImportPath(virtualFile, contentRoot);
//            }
//
//            // First we check the sources of module sdk.
//            Module module = ModuleUtilCore.findModuleForPsiElement(directory);
//            if (module != null) {
//                Sdk moduleSdk = ModuleRootManager.getInstance(module).getSdk();
//                String root = getImportPath(directory, virtualFile, moduleSdk);
//                if (root != null) {
//                    return root;
//                }
//            }
//
//            // Then we check the sources of project sdk.
//            Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
//            String root = getImportPath(directory, virtualFile, projectSdk);
//            if (root != null) {
//                return root;
//            }
//
//            // If the package name cannot be constructed, return empty string
//            return "";
//        }
//        // If the directory is null, return empty string
//        return "";
//    }
//
//    @Nullable
//    private static String getImportPath(@NotNull PsiDirectory directory, VirtualFile virtualFile, Sdk sdk) {
//        if (sdk != null) {
//            VirtualFile[] roots = sdk.getSdkModificator().getRoots(OrderRootType.SOURCES);
//            for (VirtualFile root : roots) {
//                if (!directory.getVirtualFile().getPath().startsWith(root.getPath())) {
//                    continue;
//                }
//                return getImportPath(virtualFile, root);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Returns the import path.
//     *
//     * @param virtualFile file which we are checking
//     * @param root        root directory which contains the file
//     * @return import path of the file
//     */
//    private static String getImportPath(VirtualFile virtualFile, VirtualFile root) {
//        // Get the relative path of the file in the project
//        String trimmedPath = virtualFile.getPath().replace(root.getPath(), "");
//        // Node: In virtual file paths, separators will always be "/" regardless of the OS.
//        // Remove the separator at the beginning of the string
//        trimmedPath = trimmedPath.replaceFirst("/", "");
//        // Replace all other separators with . to get the package path
//        trimmedPath = trimmedPath.replaceAll("/", ".");
//        return trimmedPath;
//    }
//
//    public static String suggestPackageNameForFile(@NotNull Project project, @NotNull VirtualFile virtualFile) {
//        VirtualFile parent = virtualFile.getParent();
//        PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(parent);
//        return suggestPackageNameForDirectory(psiDirectory);
//    }
//}
