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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.PluginVersion;

public class PluginPagePanel
    extends Panel
{

    private static final long serialVersionUID = -3431335093802133184L;

    public PluginPagePanel(String id, PluginVersion plugin)
    {
        super(id);

        add(new Label("name", plugin.getName()));
        add(new Label("author", plugin.getAuthor()));
        add(new Label("uploadTime", plugin.getUploadTime()));
        add(new Label("version", plugin.getVersionNumber()));
        add(new Label("license", plugin.getLicense()));
        add(new ExternalLink("projectPage", plugin.getProjectPage()));
        add(new ExternalLink("docPage", plugin.getDocPage()));
        add(new Label("description", plugin.getDescription()));
        
        add(new LambdaAjaxLink("download", x -> downloadPlugin(x, plugin)));
        add(new LambdaAjaxLink("downloadWithDeps", x -> downloadPluginAndDeps(x, plugin)));
    }

    private void downloadPluginAndDeps(AjaxRequestTarget x, PluginVersion plugin)
    {
        // left empty for now
    }

    private void downloadPlugin(AjaxRequestTarget x, PluginVersion plugin)
    {
        // left empty for now
    }

}
