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
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginDependency;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

/**
 * Implementation of methods defined in the {@link PluginDao} interface
 */
@Component("pluginRepository")
public class PluginDaoImpl
    implements PluginDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(Plugin plugin)
    {
        entityManager.persist(plugin);
        entityManager.flush();
    }

    @Override
    @Transactional
    public Plugin update(Plugin plugin)
    {
        return entityManager.merge(plugin);
    }

    @Override
    @Transactional
    public boolean exists(final String pluginId)
    {
        return entityManager
                .createQuery("FROM " + Plugin.class.getName() + " o WHERE o.id = :pluginId")
                .setParameter("pluginId", pluginId).getResultList().size() > 0;
    }

    @Override
    @Transactional
    public int delete(String pluginId)
    {
        Plugin toDelete = get(pluginId);
        if (toDelete == null) {
            return 0;
        }
        else {
            delete(toDelete);
            return 1;
        }
    }

    @Override
    @Transactional
    public void delete(Plugin plugin)
    {
        entityManager.remove(entityManager.merge(plugin));
    }

    @Override
    @Transactional
    public Plugin get(String pluginId)
    {
        Validate.notBlank(pluginId, "Plugin ID must be specified");

        String query = "FROM " + Plugin.class.getName() + " o WHERE o.id = :pluginId";

        List<Plugin> plugins = entityManager.createQuery(query, Plugin.class)
                .setParameter("pluginId", pluginId).setMaxResults(1).getResultList();

        if (plugins.isEmpty()) {
            return null;
        }
        else {
            return plugins.get(0);
        }
    }

    @Override
    @Transactional
    public List<Plugin> list()
    {
        return entityManager.createQuery("FROM " + Plugin.class.getName(), Plugin.class)
                .getResultList();
    }

    @Override
    @Transactional
    public List<PluginVersion> getVersions(Plugin plugin)
    {
        String query = "FROM " + PluginVersion.class.getName() + " WHERE pluginId = :plugin";
        return entityManager.createQuery(query, PluginVersion.class).setParameter("plugin", plugin)
                .getResultList();
    }

    @Override
    @Transactional
    public List<PluginDependency> getDependencies(Plugin plugin)
    {
        String query = "FROM " + PluginDependency.class.getName() + " WHERE dependee = :plugin";
        return entityManager.createQuery(query, PluginDependency.class)
                .setParameter("plugin", plugin).getResultList();
    }

    @Override
    @Transactional
    public List<Plugin> getMaintained(User user)
    {
        // Something like this might be better, but in HQL selects don't work with join tables:
        //
        // String query = "FROM plugin_maintainers WHERE username = :user";
        // return entityManager.createQuery(query, Plugin.class)
        // .setParameter("user", user.getUsername()).getResultList();

        return list().stream()
                .filter(x -> x.getMaintainers().contains(user))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createPluginAndVersion(Plugin plugin, PluginVersion version)
    {
        entityManager.persist(plugin);
        entityManager.persist(version);
        entityManager.flush();
    }

}
