/*
 * Copyright 2010-2019 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.training.exercises.chapter4;

import au.gov.asd.tac.constellation.graph.processing.GraphRecordStore;
import au.gov.asd.tac.constellation.graph.processing.RecordStore;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.views.dataaccess.plugins.DataAccessPlugin;
import au.gov.asd.tac.constellation.views.dataaccess.plugins.DataAccessPluginCoreType;
import au.gov.asd.tac.constellation.views.dataaccess.templates.RecordStoreQueryPlugin;
import jdk.tools.jlink.plugin.PluginException;
import org.openide.util.NbBundle;

/**
 * Chain Cities Plugin.
 */
//@ServiceProviders({
//    @ServiceProvider(service = DataAccessPlugin.class),
//    @ServiceProvider(service = Plugin.class)
//})
@NbBundle.Messages("ChainCitiesPlugin=Chain Cities")
public class ChainCitiesPlugin extends RecordStoreQueryPlugin implements DataAccessPlugin {

    @Override
    protected RecordStore query(final RecordStore query, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        RecordStore result = new GraphRecordStore();

        // Step 1. Read selected cities from query RecordStore.
        // Step 2. Query each selected city for connecting flights.
        // Step 3. Add new flights, including source and destination city nodes to the result RecordStore.
        return result;
    }

    @Override
    public String getType() {
        return DataAccessPluginCoreType.EXPERIMENTAL;
    }

    @Override
    public int getPosition() {
        return 2;
    }
}
