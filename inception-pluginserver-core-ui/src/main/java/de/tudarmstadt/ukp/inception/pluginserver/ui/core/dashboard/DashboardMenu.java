/*
 * Copyright 2018
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
package de.tudarmstadt.ukp.inception.pluginserver.ui.core.dashboard;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItem;

public class DashboardMenu
    extends Panel
{
    private static final long serialVersionUID = 8582941766827165724L;

    public DashboardMenu(String aId, final IModel<List<MenuItem>> aModel)
    {
        super(aId, aModel);

        add(new ListView<MenuItem>("items", aModel)
        {
            private static final long serialVersionUID = 6345129880666905375L;

            @Override
            protected void populateItem(ListItem<MenuItem> aItem)
            {
                MenuItem item = aItem.getModelObject();
                final Class<? extends Page> pageClass = item.getPageClass();
                StatelessLink<Void> menulink = new StatelessLink<Void>("item")
                {
                    private static final long serialVersionUID = 4110674757822252390L;

                    @Override
                    public void onClick()
                    {
                        setResponsePage(pageClass);
                    }
                };
                UrlResourceReference imageRef = new UrlResourceReference(Url.parse(item.getIcon()));
                imageRef.setContextRelative(true);
                menulink.add(new Image("icon", imageRef));
                menulink.add(new Label("label", item.getLabel()));

//                Project project = Session.get().getMetaData(SessionMetaData.CURRENT_PROJECT);
//
//                boolean isAdminItem = asList("ProjectPage", "ManageUsersPage")
//                        .contains(item.getPageClass().getSimpleName());

                aItem.add(menulink);
                aItem.setVisible(item.applies() /*&& (project != null || isAdminItem)*/);
            }

        });
    }
}
