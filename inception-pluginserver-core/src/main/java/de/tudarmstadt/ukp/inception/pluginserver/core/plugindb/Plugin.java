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
package de.tudarmstadt.ukp.inception.pluginserver.core.plugindb;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.comparator.Comparators;

import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginDao;

/**
 * This class represents a plugin. Most plugin data belong to the individual versions, but
 * maintainers and dependencies as the dependee are not specific to a version.
 */
@Entity
@Table(name = "plugins")
public class Plugin
    implements Serializable
{
    private static final long serialVersionUID = 8884136628242402811L;

    @Id
    private String id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PluginVersion> versions;

    @OneToMany(mappedBy = "dependee", cascade = { CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @Column(nullable = true)
    private Set<PluginDependency> dependencies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "plugin_maintainers",
        joinColumns = @JoinColumn(name = "pluginId", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    private Set<User> maintainers;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public Set<PluginVersion> getVersions()
    {
        if (versions == null) {
            versions = new HashSet<>();
        }
        return versions;
    }

    public void setVersions(Set<PluginVersion> versions)
    {
        this.versions = versions;
    }

    /**
     * This method returns a set of all user accounts that can manage this plugin with the
     * PluginManagerPage. At the moment, no UI to add maintainers exists, therefore the set only
     * ever contains the user who created the plugin.
     * 
     * @return a set that contains only the plugin creator
     */
    public Set<User> getMaintainers()
    {
        return maintainers;
    }

    public void setMaintainers(Set<User> maintainers)
    {
        this.maintainers = maintainers;
    }

    /**
     * This method is used to determine the "name" of a plugin for lists like in PluginManagerPage.
     * If the plugin has at least one enabled version, the newest enabled version is returned,
     * otherwise the newest version.
     * 
     * @return either the newes or the newest enabled version of this plugin
     * 
     * @throws NoSuchElementException
     *             if the plugin has no versions at all
     */
    public PluginVersion newestVersion()
    {
        Iterator<PluginVersion> versions = getVersions().stream()
                .sorted(Comparators.comparable().reversed()).iterator();

        PluginVersion newest = versions.next();

        if (!newest.isEnabled()) {
            while (versions.hasNext()) {
                PluginVersion v;
                if ((v = versions.next()).isEnabled()) {
                    return v;
                }
            }
        }

        return newest;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, created);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Plugin other = (Plugin) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
        return other.created.equals(created);
    }

    /**
     * This method returns true if at least one version of this plugin can be downloaded.
     * 
     * @return if at least one version is enabled
     */
    public boolean hasEnabledVersion()
    {
        return getVersions().stream().anyMatch(PluginVersion::isEnabled);
    }

    /**
     * This method is used by the PluginManagerPage to generate human-readable but still unique
     * plugin IDs.
     * 
     * @param firstVersion
     *            the plugin version whose name is used for the plugin ID
     * @param pluginRepository
     *            a PluginDao to check whether the generated ID is unique
     * @return a unique plugin ID
     */
    public String makeId(PluginVersion firstVersion, PluginDao pluginRepository)
    {
        String created = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String name = Arrays.stream(firstVersion.getName().split("[^\\p{IsAlphabetic}]+"))
                .map(x -> (x.length() > 3) ? x.substring(0, 3) : x).reduce("", (a, b) -> a + b);

        if (name.length() > 14) {
            name = name.substring(0, 13);
        }

        String id = created + "_" + name;

        if (pluginRepository.exists(id)) {
            int counter = 2;
            while (pluginRepository.exists(id + counter)) {
                ++counter;
            }
            id = id + counter;
        }

        setId(id);
        return id;
    }

}
