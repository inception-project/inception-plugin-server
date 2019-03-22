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

import java.util.Collections;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.model.IModel;

import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxFormComponentUpdatingBehavior;
import de.tudarmstadt.ukp.clarin.webanno.support.lambda.LambdaAjaxLink;
import de.tudarmstadt.ukp.clarin.webanno.support.wicket.ListPanel_ImplBase;
import de.tudarmstadt.ukp.clarin.webanno.support.wicket.OverviewListChoice;

public class VersionPanel
    extends ListPanel_ImplBase
{
    private static final long serialVersionUID = 7143511570153129695L;

    private IModel<PlaceholderPlugin> pluginModel;
    private OverviewListChoice<PlaceholderPlugin> overviewList;

    public VersionPanel(String id, IModel<PlaceholderPlugin> pluginModel,
            IModel<PlaceholderPlugin> versionModel)
    {
        super(id, pluginModel);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        this.pluginModel = pluginModel;

        overviewList = new OverviewListChoice<>("plugin");
        overviewList.setChoiceRenderer(new ChoiceRenderer<PlaceholderPlugin>()
        {

            private static final long serialVersionUID = 3484052447177235280L;

            @Override
            public Object getDisplayValue(PlaceholderPlugin aPlugin)
            {
                return aPlugin.getVersion();
            }
        });
        overviewList.setModel(versionModel);
        overviewList
                .setChoices(pluginModel.map(x -> x.getVersions()).orElse(Collections.emptyList()));
        overviewList.add(new LambdaAjaxFormComponentUpdatingBehavior("change", this::onChange));
        add(overviewList);

        add(new LambdaAjaxLink("upload", this::actionCreate));
    }

    @Override
    protected void onConfigure()
    {
        super.onConfigure();

        this.setVisible(pluginModel.getObject() != null);

        overviewList.getModel().setObject(null);
    }

}
