/*
 * Copyright 2019
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao;

import java.util.List;

import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginDependency;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

/**
 * Provide methods for plugin version management such as create, update, list versions
 */
public interface PluginVersionDao
{
    /**
     * Create a new {@link PluginVersion}
     * 
     * @param version
     *            the plugin version to cerate
     */
    void create(PluginVersion version);

    /**
     * Update existing {@link PluginVersion}
     * 
     * @param versionId
     *            the version to update
     * @return the updated version
     */
    PluginVersion update(PluginVersion version);

    /**
     * Check if a {@link PluginVersion} with this ID exists
     * 
     * @param versionId
     *            the version ID
     * @return if such a version exists
     */
    boolean exists(long versionId);

    /**
     * Delete a {@link PluginVersion} using the ID
     * 
     * @param versionId
     *            the version ID
     * @return how many versions were deleted
     */
    int delete(long versionId);

    /**
     * Delete this {@link PluginVersion}
     * 
     * @param version
     *            the version to be deleted
     */
    void delete(PluginVersion version);

    /**
     * Get a {@link PluginVersion} using the ID
     * 
     * @param versionId
     *            the version ID
     * @return the version if it exists, else null
     */
    PluginVersion get(long versionId);

    /**
     * Get all plugin versions in the system
     * 
     * @return a list of all plugin versions
     */
    List<PluginVersion> list();

    /**
     * Get all dependency relations where this {@link PluginVersion} depends on something
     * 
     * @param version
     *            the depender
     * @return a list of all dependencies with this as the depender
     */
    List<PluginDependency> getDependencies(PluginVersion version);

    /**
     * Updates only a {@link PluginVersion}'s enabled flag, ignoring all other attributes
     * 
     * @param version
     *            the version to be made visible or invisible
     * @return the updated version
     */
    PluginVersion updateVisibility(PluginVersion version);

    /**
     * Increments a {@link PluginVersion}'s download counter by 1, ignoring all other attributes
     * 
     * @param version
     *            the version to be updated
     * @return the updated version
     */
    PluginVersion registerDownload(PluginVersion version);
}
