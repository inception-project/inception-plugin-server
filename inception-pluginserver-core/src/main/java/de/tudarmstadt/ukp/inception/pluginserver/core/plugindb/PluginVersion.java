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
import java.sql.Blob;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * 
 */
@Entity
@Table(name = "pluginVersions")
public class PluginVersion
    implements Serializable, Comparable<PluginVersion>
{
    private static final long serialVersionUID = -5190576226156258071L;

    @Id
    @GeneratedValue
    private long versionId;

    @ManyToOne
    @JoinColumn
    private String pluginId;
    
    private String name;

    private String versionNumber;

    private String author;

    private String description;

    private String license;

    @Column(nullable = true)
    private String projectPage;

    @Column(nullable = true)
    private String docPage;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTime;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    private boolean enabled;

    @Column(nullable = true) // if the file name is null, auto-generate one
    private String fileName;

    @Lob
    private Blob file;

    private Integer downloads;

    @OneToMany(mappedBy = "depender")
    private Set<PluginDependency> dependencies;

    public PluginVersion()
    {

    }

    public long getVersionId()
    {
        return versionId;
    }

    public void setVersionId(long versionId)
    {
        this.versionId = versionId;
    }

    public String getPluginId()
    {
        return pluginId;
    }

    public void setPluginId(String pluginId)
    {
        this.pluginId = pluginId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersionNumber()
    {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber)
    {
        this.versionNumber = versionNumber;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public String getProjectPage()
    {
        return projectPage;
    }

    public void setProjectPage(String projectPage)
    {
        this.projectPage = projectPage;
    }

    public String getDocPage()
    {
        return docPage;
    }

    public void setDocPage(String docPage)
    {
        this.docPage = docPage;
    }

    public Date getUploadTime()
    {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime)
    {
        this.uploadTime = uploadTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public Blob getFile()
    {
        return file;
    }

    public void setFile(Blob file)
    {
        this.file = file;
    }

    public Integer getDownloads()
    {
        return downloads;
    }

    public void setDownloads(Integer downloads)
    {
        this.downloads = downloads;
    }

    @Override
    public int compareTo(PluginVersion o)
    {
        return updateTime.compareTo(o.updateTime);
    }

}
