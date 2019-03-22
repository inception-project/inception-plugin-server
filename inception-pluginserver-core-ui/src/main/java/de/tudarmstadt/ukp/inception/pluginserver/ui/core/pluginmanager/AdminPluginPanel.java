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
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager;

import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;

public class AdminPluginPanel
    extends PluginPanel
{

    private static final long serialVersionUID = 3663159670526519125L;

    /**
     * @param id
     * @param aModel
     * @param plugins
     */
    public AdminPluginPanel(String id, IModel<PlaceholderPlugin> aModel,
            Supplier<List<PlaceholderPlugin>> plugins)
    {
        super(id, aModel, plugins);
        add(new LambdaAjaxLink("removePlugin", this::actionRemovePlugin));
    }
    
    private void actionRemovePlugin(AjaxRequestTarget target)
    {
        //TODO Remove all versins of the selected plugin from the server
    }

}
