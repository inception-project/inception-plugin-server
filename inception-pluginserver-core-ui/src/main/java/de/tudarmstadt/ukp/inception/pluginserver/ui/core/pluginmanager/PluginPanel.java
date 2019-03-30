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

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxFormComponentUpdatingBehavior;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.clarin.webanno.support.wicket.ListPanel_ImplBase;
import de.tudarmstadt.ukp.clarin.webanno.support.wicket.OverviewListChoice;
import de.tudarmstadt.ukp.inception.pluginserver.core.plugindb.Plugin;

/**
 * With this panel, a plugin can be selected from a list of plugins.
 */
public class PluginPanel
    extends ListPanel_ImplBase
{

    private static final long serialVersionUID = -4179642903213029874L;

    protected OverviewListChoice<Plugin> overviewList;

    /**
     * Creates a PluginPanel.
     * 
     * @param id
     *            The non-null id of this component
     * @param model
     *            The model of the plugin whose versions can be selected in a VersionPanel
     * @param listModel
     *            A model of a List of all plugins that the panel is supposed to list
     */
    public PluginPanel(final String id, final IModel<Plugin> model, IModel<List<Plugin>> listModel)
    {
        super(id);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        overviewList = new OverviewListChoice<>("plugin");
        overviewList.setChoiceRenderer(new ChoiceRenderer<Plugin>()
        {

            private static final long serialVersionUID = 3484052447177235280L;

            @Override
            public Object getDisplayValue(Plugin aPlugin)
            {
                return aPlugin.newestVersion().getName() + ": " + aPlugin.getId();
            }
        });
        overviewList.setModel(model);
        overviewList.setChoices(listModel);
        overviewList.add(new LambdaAjaxFormComponentUpdatingBehavior("change", this::onChange));
        add(overviewList);

        add(new LambdaAjaxLink("create", this::actionCreate));
    }
}
