/*
 * Copyright 2010-2024 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.training.exercises.chapter5;

import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.plugins.Plugin;
import au.gov.asd.tac.constellation.plugins.PluginException;
import au.gov.asd.tac.constellation.plugins.PluginInfo;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.PluginType;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameter;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.plugins.templates.SimpleEditPlugin;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Spread Infection Plugin.
 */
@ServiceProvider(service = Plugin.class)
@NbBundle.Messages("SpreadInfectionPlugin=Spread Infection")
@PluginInfo(pluginType = PluginType.NONE, tags = {"OUTBREAK"})
public class SpreadInfectionPlugin extends SimpleEditPlugin {

    public static final String NUMBER_OF_DAYS_PARAMETER_ID = PluginParameter.buildId(SpreadInfectionPlugin.class, "num_days");
    private static final String NUMBER_OF_DAYS_PARAMETER_LABEL = "Number of Days";

    @Override
    protected void edit(final GraphWriteMethods writableGraph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        // Implement plugin logic here
    }

}
