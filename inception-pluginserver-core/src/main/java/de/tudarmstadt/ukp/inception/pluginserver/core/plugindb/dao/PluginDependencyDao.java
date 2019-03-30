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

import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginDependency;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

/**
 * Provide methods for plugin dependency management
 * such as create, update, list dependency relations
 */
public interface PluginDependencyDao
{
    /**
     * Create a new {@link PluginDependency}
     * 
     * @param dependency
     *            the dependency relation to create
     */
    void create(PluginDependency dependency);

    /**
     * Update existing {@link PluginDependency}
     * 
     * @param dependency
     *            the dependency relation to update
     * @return the dependency relation
     */
    PluginDependency update(PluginDependency dependency);

    /**
     * Check if a {@link PluginDependency} with this ID exists
     * 
     * @param dependencyId
     *            the dependency ID
     * @return if such a relation exists
     */
    boolean exists(long dependencyId);

    /**
     * Delete a {@link PluginDependency} using the dependency ID
     * 
     * @param dependencyId
     *            the dependency ID
     * @return how many dependencies were deleted
     */
    int delete(long dependencyId);

    /**
     * Delete this {@link PluginDependency}
     * 
     * @param dependency
     *            the dependency relation to be deleted
     */
    void delete(PluginDependency dependency);

    /**
     * Get a {@link PluginDependency} using the ID
     * 
     * @param dependencyId
     *            the dependency ID
     * @return the relation if it exists, else null
     */
    PluginDependency get(long dependencyId);

    /**
     * Get all dependency relations in the system
     * 
     * @return a list of all dependencies
     */
    List<PluginDependency> list();

    boolean hasNonDependerRelations(PluginVersion version);

    boolean hasDependers(Plugin plugin);
}
