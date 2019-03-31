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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
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
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import de.tudarmstadt.ukp.clarin.webanno.security.model.User;

/**
 * This class represents a version of a plugin. It contains the plugin file and all metadata that
 * can change between plugin versions.
 */
@Entity
@Table(name = "pluginVersions")
public class PluginVersion
    implements Serializable, Comparable<PluginVersion>
{
    public static final int MAX_FILE_SIZE = 52428800; // 50 MiB

    private static final long serialVersionUID = -5190576226156258071L;

    @Id
    @GeneratedValue
    private long versionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id", nullable = false, updatable = false)
    private Plugin plugin;

    private String name;

    private String versionNumber;

    private String author;

    @Column(length = 500)
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

    @Column(length = 100, // very long file names might not work on some systems
            nullable = true) // if a file name is null, the getter creates one
    private String fileName;

    @Lob
    @Type(type = "org.hibernate.type.MaterializedBlobType")
    @Column(length = MAX_FILE_SIZE)
    private byte[] file;

    private Integer fileSize;

    private int downloads;

    @OneToMany(mappedBy = "depender", cascade = { CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH })
    @Column(nullable = true)
    private Set<PluginDependency> dependencies;

    private String minAppVersion;

    @Column(nullable = true)
    private String maxAppVersion;

    public PluginVersion()
    {
        // intentionally empty
    }

    public PluginVersion(Plugin plugin, User currentUser)
    {
        this.downloads = 0;
        this.setDependencies(new HashSet<>());
        this.enabled = false;
        this.plugin = plugin;

        if (plugin.getVersions() == null) {
            plugin.setVersions(new HashSet<>(Arrays.asList(this)));
        }
        else {
            plugin.getVersions().add(this);
        }

        if (plugin.getMaintainers() == null) {
            plugin.setMaintainers(new HashSet<>(Arrays.asList(currentUser)));
        }
        else {
            plugin.getMaintainers().add(currentUser);
        }
    }

    public long getVersionId()
    {
        return versionId;
    }

    public void setVersionId(long versionId)
    {
        this.versionId = versionId;
    }

    public Plugin getPlugin()
    {
        return plugin;
    }

    public void setPlugin(Plugin plugin)
    {
        this.plugin = Objects.requireNonNull(plugin);
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
        if (fileName != null) {
            return fileName;
        }
        else if (name != null && versionNumber != null) {
            return name + " " + versionNumber + ".jar";
        }
        else {
            return "";
        }
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public boolean hasFileName()
    {
        return fileName != null;
    }

    public Integer getFileSize()
    {
        if (fileSize == null) {
            setFileSize(getFile().length);
        }
        return fileSize;
    }

    public void setFileSize(Integer fileSize)
    {
        this.fileSize = fileSize;
    }

    public byte[] getFile()
    {
        return file;
    }

    public void setFile(byte[] file)
    {
        this.file = file;
        this.setFileSize(file.length);
    }

    public int getDownloads()
    {
        return downloads;
    }

    public void setDownloads(int downloads)
    {
        this.downloads = downloads;
    }

    public Set<PluginDependency> getDependencies()
    {
        return dependencies;
    }

    public void setDependencies(Set<PluginDependency> dependencies)
    {
        this.dependencies = dependencies;
    }

    public String getMinAppVersion()
    {
        return minAppVersion;
    }

    public void setMinAppVersion(String minAppVersion)
    {
        this.minAppVersion = minAppVersion;
    }

    public String getMaxAppVersion()
    {
        return maxAppVersion;
    }

    public void setMaxAppVersion(String maxAppVersion)
    {
        this.maxAppVersion = maxAppVersion;
    }

    @Override
    public int compareTo(PluginVersion other)
    {
        return ((uploadTime != null) ? uploadTime : new Date())
                .compareTo(((other.uploadTime != null) ? other.uploadTime : new Date()));
    }

}
