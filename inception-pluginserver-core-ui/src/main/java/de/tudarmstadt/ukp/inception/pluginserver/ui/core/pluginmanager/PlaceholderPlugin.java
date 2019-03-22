/*
 * Copyright 2019
 * Ubiquitous Knowledge Processing (UKP) Lab and FG Language Technology
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
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a placeholder for the UI mock-ups and will be replaced either by a single Plugin
 * class or a Plugin class and a PluginVersion class that represent plugins in the database.
 * 
 * Its fields contain the metadata that will be stored in addition to the plugin *.jar files
 * themselves.
 */
public class PlaceholderPlugin
    implements Serializable
{

    private static final long serialVersionUID = -4562347292826148146L;

    private String name, author, version, description, license, projectPage, docPage;
    private int id;
    private List<PlaceholderPlugin> versions;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
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

    public String toString()
    {
        return name + " (" + getID() + ") " + version;
    }

    /**
     * Because a plugin name and author might change between versions, a unique identifier is
     * necessary. In the actual Plugin class, this identifier might for example be a hash of the
     * first version's *.jar file or it might be a human-readable string derived from the first
     * version's name and upload timestamp (e.g. myplugin-2019-04-01-1234).
     * 
     * @return a value that could make sense as a primary key in the database.
     */
    public String getID()
    {
        return Integer.toHexString(id);
    }

    public PlaceholderPlugin(String name, String author, String version, String description,
            String license, String projectPage, String docPage)
    {
        this.name = name;
        this.description = description;
        this.author = author;
        this.version = version;
        this.license = license;
        this.projectPage = projectPage;
        this.docPage = docPage;

        this.versions = new LinkedList<>();
        versions.add(this);

        this.id = name.hashCode() ^ author.hashCode();
    }

    public PlaceholderPlugin()
    {
        this("", "", "", "", "", "", "");
    }

    public List<PlaceholderPlugin> getVersions()
    {
        return versions;
    }

    public void setVersions(List<PlaceholderPlugin> versions)
    {
        this.versions = versions;
    }

}
