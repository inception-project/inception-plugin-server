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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 */
@Entity
@Table(name = "pluginDependencies")
public class PluginDependency implements Serializable
{
    private static final long serialVersionUID = 5488874084292327492L;

    @Id
    @GeneratedValue
    private long dependencyId;
    
    @ManyToOne
    private PluginVersion depender;
    
    @ManyToOne
    private Plugin dependee;
    
    @Column(nullable = true)
    private PluginVersion dependeeMinVersion;
    
    @Column(nullable = true)
    private PluginVersion dependeeMaxVersion;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public long getDependencyId()
    {
        return dependencyId;
    }

    public void setDependencyId(long dependencyId)
    {
        this.dependencyId = dependencyId;
    }

    public PluginVersion getDepender()
    {
        return depender;
    }

    public void setDepender(PluginVersion depender)
    {
        this.depender = depender;
    }

    public Plugin getDependee()
    {
        return dependee;
    }

    public void setDependee(Plugin dependee)
    {
        this.dependee = dependee;
    }

    public PluginVersion getDependeeMinVersion()
    {
        return dependeeMinVersion;
    }

    public void setDependeeMinVersion(PluginVersion dependeeMinVersion)
    {
        this.dependeeMinVersion = dependeeMinVersion;
    }

    public PluginVersion getDependeeMaxVersion()
    {
        return dependeeMaxVersion;
    }

    public void setDependeeMaxVersion(PluginVersion dependeeMaxVersion)
    {
        this.dependeeMaxVersion = dependeeMaxVersion;
    }
}
