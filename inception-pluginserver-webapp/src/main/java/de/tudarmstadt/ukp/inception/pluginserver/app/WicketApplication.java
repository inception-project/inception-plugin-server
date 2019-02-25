/*
 * Copyright 2012
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
package de.tudarmstadt.ukp.inception.pluginserver.app;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.Page;
import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import de.tudarmstadt.ukp.clarin.webanno.ui.core.WicketApplicationBase;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;
import de.tudarmstadt.ukp.inception.pluginserver.app.config.InceptionResourcesBehavior;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.dashboard.project.DashboardPage;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.menubar.MenuBar;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.pluginmanager.PlaceholderPlugin;

@org.springframework.stereotype.Component("wicketApplication")
public class WicketApplication
    extends WicketApplicationBase
{    
    @Override
    protected void initOnce()
    {
        super.initOnce();
                
        setMetaData(ApplicationPageBase.MENUBAR_CLASS, MenuBar.class);
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage()
    {
        return DashboardPage.class;
    }

    @Override
    protected String getLogoLocation()
    {
        return "/de/tudarmstadt/ukp/inception/app/logo/ukp-logo.png";
    }
    
    @Override
    protected void initWebFrameworks()
    {
        super.initWebFrameworks();

        initInceptionResources();
    }
    
    protected void initInceptionResources()
    {
        getComponentInstantiationListeners().add(component -> {
            if (component instanceof Page) {
                component.add(InceptionResourcesBehavior.get());
            }
        });
    }
}
