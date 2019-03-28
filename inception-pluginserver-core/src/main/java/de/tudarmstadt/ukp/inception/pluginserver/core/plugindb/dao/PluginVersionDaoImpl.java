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

import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginDependency;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

/**
 * Implementation of methods defined in the {@link PluginVersionDao} interface
 */
@Component("pluginVersionRepository")
public class PluginVersionDaoImpl
    implements PluginVersionDao
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void create(PluginVersion version)
    {
        entityManager.persist(version);
        entityManager.flush();
    }

    @Override
    @Transactional
    public PluginVersion update(PluginVersion version)
    {
        return entityManager.merge(version);
    }

    @Override
    @Transactional
    public boolean exists(long versionId)
    {
        return entityManager
                .createQuery("FROM " + PluginVersion.class.getName()
                        + " o WHERE o.versionId = :versionId")
                .setParameter("versionId", versionId).getResultList().size() > 0;
    }

    @Override
    @Transactional
    public int delete(long versionId)
    {
        PluginVersion toDelete = get(versionId);
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
    public void delete(PluginVersion version)
    {
        entityManager.remove(entityManager.merge(version));
    }

    @Override
    @Transactional
    public PluginVersion get(long versionId)
    {
        String query = "FROM " + PluginVersion.class.getName()
                + " o WHERE o.versionId = :versionId";
        
        List<PluginVersion> plugins = entityManager
                .createQuery(query, PluginVersion.class)
                .setParameter("pluginId", versionId)
                .setMaxResults(1)
                .getResultList();
        
        if (plugins.isEmpty()) {
            return null;
        }
        else {
            return plugins.get(0);
        }
    }

    @Override
    @Transactional
    public List<PluginVersion> list()
    {
        return entityManager
                .createQuery("FROM " + PluginVersion.class.getName(), PluginVersion.class)
                .getResultList();
    }

    @Override
    @Transactional
    public List<PluginDependency> getDependencies(PluginVersion version)
    {
        String query =
                "FROM " + PluginDependency.class.getName() +
                " WHERE depender = :version";
        return entityManager.createQuery(query, PluginDependency.class)
                .setParameter("version", version).getResultList();
    }

}
