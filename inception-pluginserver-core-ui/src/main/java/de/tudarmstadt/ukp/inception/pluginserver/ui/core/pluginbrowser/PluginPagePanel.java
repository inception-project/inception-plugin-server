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
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginbrowser;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.util.lang.Bytes;

import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

public class PluginPagePanel
    extends Panel
{

    private static final long serialVersionUID = -3431335093802133184L;

    public PluginPagePanel(String id, PluginVersion plugin)
    {
        super(id);

        add(new Label("name", plugin.getName()));
        add(new Label("version", plugin.getVersionNumber()));
        add(new Label("author", plugin.getAuthor()));
        add(new Label("license", plugin.getLicense()));
        add(new Label("description", plugin.getDescription()));
        add(new Label("uploadTime", plugin.getUploadTime()));
        add(new Label("updateTime", plugin.getUpdateTime()));

        add(new ExternalLink("projectPage", plugin.getProjectPage()));
        add(new ExternalLink("docPage", plugin.getDocPage()));

        ResourceLink<Void> downloadLink = new ResourceLink<Void>("download",
                new ByteArrayResource("application/zip", plugin.getFile(), plugin.getFileName()));
        downloadLink.add(
                new Label("fileName", plugin::getFileName),
                new Label("downloadSize", Bytes.bytes(plugin.getFileSize())::toString));
        add(downloadLink);

        add(new ResourceLink<Void>("downloadWithDeps", new ByteArrayResource("application/zip",
                makeMultiDownload(plugin), multiDownloadName(plugin))));
    }

    private String multiDownloadName(PluginVersion plugin)
    {
        return plugin.getName() + " " + plugin.getVersionNumber() + " + dependencies.zip";
    }

    private byte[] makeMultiDownload(PluginVersion plugin)
    {
        byte[] emptyZipFile = new byte[22];
        emptyZipFile[0] = 0x50;
        emptyZipFile[1] = 0x4b;
        emptyZipFile[2] = 0x05;
        emptyZipFile[3] = 0x06;

        return emptyZipFile;
    }

}
