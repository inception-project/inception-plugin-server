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

public class PlaceholderPlugin
    implements Serializable
{

    private static final long serialVersionUID = -4562347292826148146L;

    private String name, author, version, description, license;
    private boolean enabled;
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

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
    
    public String toString()
    {
        return name + " " + version;
    }

    public PlaceholderPlugin(String name, String author, String version, String description,
            String license, boolean enabled)
    {
        this.name = name;
        this.description = description;
        this.author = author;
        this.version = version;
        this.setLicense(license);
        this.enabled = enabled;
        this.versions = new LinkedList<>();
        versions.add(this);
    }

    public PlaceholderPlugin()
    {
        this("", "", "", "", "", false);
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
