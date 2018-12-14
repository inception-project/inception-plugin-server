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
package de.tudarmstadt.ukp.inception.ui.core.dashboard.admin;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import de.tudarmstadt.ukp.clarin.webanno.security.UserDao;
import de.tudarmstadt.ukp.clarin.webanno.security.model.User;
import de.tudarmstadt.ukp.clarin.webanno.support.ApplicationContextProvider;
import de.tudarmstadt.ukp.clarin.webanno.support.SettingsUtil;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.login.LoginPage;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItem;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.menu.MenuItemRegistry;
import de.tudarmstadt.ukp.clarin.webanno.ui.core.page.ApplicationPageBase;
import de.tudarmstadt.ukp.inception.pluginserver.ui.core.dashboard.DashboardMenu;

/**
 * Admin menu page.
 */
@MountPath(value = "/manage/overview.html")
public class AdminDashboardPage
    extends ApplicationPageBase
{
    private static final long serialVersionUID = -2487663821276301436L;

    private @SpringBean UserDao userRepository;
    private @SpringBean MenuItemRegistry menuItemService;

    private DashboardMenu menu;

    public AdminDashboardPage()
    {
        setStatelessHint(true);
        setVersioned(false);
        
        // In case we restore a saved session, make sure the user actually still exists in the DB.
        // redirect to login page (if no user is found, admin/admin will be created)
        User user = userRepository.getCurrentUser();
        if (user == null) {
            setResponsePage(LoginPage.class);
        }
                
        menu = new DashboardMenu("menu", LoadableDetachableModel.of(this::getMenuItems));
        add(menu);
    }
    
    private List<MenuItem> getMenuItems()
    {
        return menuItemService.getMenuItems().stream()
                .filter(item -> item.getPath().matches("/admin/[^/]+"))
                .collect(Collectors.toList());
    }

    public static boolean adminAreaAccessRequired(UserDao aUserRepo)
    {
        User user = aUserRepo.getCurrentUser();
        
        // Admins need access to the admin area to manage projects 
        if (aUserRepo.isAdministrator(user)) {
            return true;
        }
    
        // If users are allowed to access their profile information, the also need to access the
        // admin area. Note: access to the users own profile should be handled differently.
        List<String> activeProfiles = asList(ApplicationContextProvider.getApplicationContext()
                .getEnvironment().getActiveProfiles());
        Properties settings = SettingsUtil.getSettings();
        return !activeProfiles.contains("auto-mode-preauth") && "true"
                        .equals(settings.getProperty(SettingsUtil.CFG_USER_ALLOW_PROFILE_ACCESS));
    }
}
