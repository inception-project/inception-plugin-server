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

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.AjaxDownloadBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;

import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.dao.PluginVersionDao;

public class PluginPagePanel
    extends Panel
{

    private static final long serialVersionUID = -3431335093802133184L;

    private @SpringBean PluginVersionDao versionRepo;

    private MarkupContainer counter;
    private IModel<PluginVersion> versionModel;

    public PluginPagePanel(String id, PluginVersion version)
    {
        super(id);

        this.versionModel = Model.of(version);

        add(new Label("name", version.getName()));
        add(new Label("version", version.getVersionNumber()));
        add(new Label("author", version.getAuthor()));
        add(new Label("license", version.getLicense()));
        add(new Label("description", version.getDescription()));
        add(new Label("uploadTime", version.getUploadTime()));
        add(new Label("updateTime", version.getUpdateTime()));
        add(new Label("minAppVersion", version.getMinAppVersion()));

        String maxAppVersion = version.getMaxAppVersion();
        add(new Label("maxAppVersion", "Last supported INCEpTION version: " + maxAppVersion)
                .setVisible(maxAppVersion != null && !maxAppVersion.isBlank()));

        add(new ExternalLink("projectPage", version.getProjectPage()));
        add(new ExternalLink("docPage", version.getDocPage()));

        initAjaxDownloadLink(version);

        add(new ResourceLink<Void>("downloadWithDeps", new ByteArrayResource("application/zip",
                makeMultiDownload(version), multiDownloadName(version))));

        counter = add(new Label("downloads", PropertyModel.of(versionModel, "downloads")));
    }

    private String multiDownloadName(PluginVersion version)
    {
        return version.getName() + " " + version.getVersionNumber() + " + dependencies.zip";
    }

    private byte[] makeMultiDownload(PluginVersion version)
    {
        byte[] emptyZipFile = new byte[22];
        emptyZipFile[0] = 0x50;
        emptyZipFile[1] = 0x4b;
        emptyZipFile[2] = 0x05;
        emptyZipFile[3] = 0x06;

        return emptyZipFile;
    }

    private void initAjaxDownloadLink(PluginVersion version)
    {
        ResourceReference reference = new ResourceReference("referenceToResource")
        {
            private static final long serialVersionUID = 2212041292525673086L;

            @Override
            public ByteArrayResource getResource()
            {
                return new ByteArrayResource("application/zip", version.getFile(),
                        version.getFileName());
            }
        };

        final AjaxDownloadBehavior download = new AjaxDownloadBehavior(reference)
        {
            private static final long serialVersionUID = 2835440907285675732L;

            @Override
            protected void onDownloadFailed(AjaxRequestTarget target)
            {
                target.appendJavaScript("alert('Download failed');");
            }

            @Override
            protected void onDownloadSuccess(AjaxRequestTarget target)
            {
                versionModel.setObject(versionRepo.registerDownload(version));
                ;

                counter.modelChanged();
                target.add(PluginPagePanel.this);
            }
        };
        add(download);

        AjaxLink<Void> downloadLink = new AjaxLink<Void>("download")
        {
            private static final long serialVersionUID = -7685063257005316428L;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
                download.initiate(target);
            }
        };

        downloadLink.add(new Label("fileName", version::getFileName),
                new Label("downloadSize", Bytes.bytes(version.getFileSize())::toString));
        add(downloadLink);
    }

}
