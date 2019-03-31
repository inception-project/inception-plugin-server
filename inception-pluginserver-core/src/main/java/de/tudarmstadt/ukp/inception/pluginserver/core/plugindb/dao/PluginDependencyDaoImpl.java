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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginDependency;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

/**
 * Implementation of methods defined in the {@link PluginDependencyDao} interface
 */
@Component("dependencyRepository")
public class PluginDependencyDaoImpl
    implements PluginDependencyDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(PluginDependency dependency)
    {
        entityManager.persist(dependency);
        entityManager.flush();
    }

    @Override
    @Transactional
    public PluginDependency update(PluginDependency dependency)
    {
        return entityManager.merge(dependency);
    }

    @Override
    @Transactional
    public boolean exists(long dependencyId)
    {
        return entityManager
                .createQuery("FROM " + PluginDependency.class.getName()
                        + " o WHERE o.dependencyId = :depId")
                .setParameter("depId", dependencyId).getResultList().size() > 0;
    }

    @Override
    @Transactional
    public int delete(long dependencyId)
    {
        PluginDependency toDelete = get(dependencyId);
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
    public void delete(PluginDependency dependency)
    {
        entityManager.remove(entityManager.merge(dependency));
    }

    @Override
    @Transactional
    public PluginDependency get(long dependencyId)
    {
        String query = "FROM " + PluginDependency.class.getName()
                + " o WHERE o.dependencyId = :depId";

        List<PluginDependency> plugins = entityManager.createQuery(query, PluginDependency.class)
                .setParameter("depId", dependencyId).setMaxResults(1).getResultList();

        if (plugins.isEmpty()) {
            return null;
        }
        else {
            return plugins.get(0);
        }
    }

    @Override
    @Transactional
    public List<PluginDependency> list()
    {
        return entityManager
                .createQuery("FROM " + PluginDependency.class.getName(), PluginDependency.class)
                .getResultList();
    }

    @Override
    @Transactional
    public boolean hasNonDependerRelations(PluginVersion version)
    {
        String query = "FROM " + PluginDependency.class.getName()
                + " WHERE dependeeMinVersion = :version OR dependeeMaxVersion = :version";

        return !entityManager.createQuery(query, PluginDependency.class)
                .setParameter("version", version).getResultList().isEmpty();
    }

    @Override
    @Transactional
    public boolean hasDependers(Plugin plugin)
    {
        String query = "FROM " + PluginDependency.class.getName() + " WHERE dependee = :plugin";

        return !entityManager.createQuery(query, PluginDependency.class)
                .setParameter("plugin", plugin).getResultList().isEmpty();
    }

}
