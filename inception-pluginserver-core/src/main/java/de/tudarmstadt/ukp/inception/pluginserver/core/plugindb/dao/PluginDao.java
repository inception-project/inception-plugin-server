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

import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginDependency;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

/**
 * Provide methods for plugin management such as create, update, list plugins
 */
public interface PluginDao
{
    /**
     * Create a new {@link Plugin}
     * 
     * @param plugin
     *            the plugin to create
     */
    void create(Plugin plugin);

    /**
     * Update existing {@link Plugin}
     * 
     * @param plugin
     *            the plugin to update
     * @return the plugin
     */
    Plugin update(Plugin plugin);

    /**
     * Check if a {@link Plugin} with this ID exists
     * 
     * @param pluginId
     *            the plugin ID
     * @return if the plugin exists
     */
    boolean exists(String pluginId);

    /**
     * Delete a {@link Plugin} using the plugin ID
     * 
     * @param pluginId
     *            the ID
     * @return how many plugins were deleted
     */
    int delete(String pluginId);

    /**
     * Delete this {@link Plugin}
     * 
     * @param plugin
     *            the plugin to be deleted
     */
    void delete(Plugin plugin);

    /**
     * Get a {@link Plugin} using ist ID
     * 
     * @param pluginId
     *            the ID
     * @return the plugin if it exists, else null
     */
    Plugin get(String pluginId);

    /**
     * Get all {@link Plugin}s in the system
     * 
     * @return a list of all plugins
     */
    List<Plugin> list();

    /**
     * Get all versions of a plugin
     * 
     * @param plugin
     *            the {@link Plugin} object
     * @return a list of all versions of this plugin
     */
    List<PluginVersion> getVersions(Plugin plugin);

    /**
     * Get all dependency relations where something depends on this {@link Plugin}
     * 
     * @param plugin
     *            the dependee
     * @return a list of all dependencies with this as the dependee
     */
    List<PluginDependency> getDependencies(Plugin plugin);

    /**
     * Get all {@link Plugin}s that this {@link User} maintains
     * 
     * @param user
     *            the user
     * @return a list of all plugins this user maintains
     */
    List<Plugin> getMaintained(User user);

    /**
     * Create a new {@link Plugin} and a new {@link PluginVersion} in a single transaction
     * 
     * @param plugin
     *            the plugin to create
     * @param version
     *            the plugin version to create
     */
    void createPluginAndVersion(Plugin plugin, PluginVersion version);
}
