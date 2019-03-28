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
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import de.tudarmstadt.ukp.clarin.webanno.security.model.User;

/**
 * 
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

    @OneToMany(mappedBy = "pluginId")
    @Column(nullable = true, name = "version")
    private Set<PluginVersion> versions;

    @OneToMany(mappedBy = "dependee")
    @Column(nullable = true)
    private Set<PluginDependency> dependencies;

    @ManyToMany
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
        return versions;
    }

    public void setVersions(Set<PluginVersion> versions)
    {
        this.versions = versions;
    }

    public Set<User> getMaintainers()
    {
        return maintainers;
    }

    public void setMaintainers(Set<User> maintainers)
    {
        this.maintainers = maintainers;
    }

    public PluginVersion newestVersion()
    {
        return getVersions().stream().filter(PluginVersion::isEnabled)
                // the fist element of the sorted stream is the newest enabled version
                .sorted((a, b) -> b.getUploadTime().compareTo(a.getUploadTime())).findFirst().get();
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

    public boolean hasEnabledVersion()
    {
        return getVersions().stream().anyMatch(PluginVersion::isEnabled);
    }

}
