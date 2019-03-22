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

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxButton;


@MountPath(value = "/pluginmgr/admin.html")
public class AdminPluginManagerPage extends PluginManagerPage
{

    private static final long serialVersionUID = -3689156510746843965L;
    
    public AdminPluginManagerPage()
    {
        super();
        pluginDetails.add(new LambdaAjaxButton<>("removeVersion", this::actionRemoveVersion));
    }

    @Override
    protected List<PlaceholderPlugin> applicablePlugins()
    {
        // TODO return all plugins on the server
        return PlaceholderPluginList.allPlugins();
    }
    
    @Override
    protected PluginPanel makePluginPanel(String id, IModel<PlaceholderPlugin> model,
            Supplier<List<PlaceholderPlugin>> plugins)
    {
        return new AdminPluginPanel(id, model, plugins);
    }
    
    private void actionRemoveVersion(AjaxRequestTarget aTarget, Form<PlaceholderPlugin> aForm)
    {
        //TODO: remove the selected plugin version from the server
    }

}
