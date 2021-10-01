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
package au.gov.asd.tac.constellation.training.solutions.chapter2;

import au.gov.asd.tac.constellation.graph.processing.GraphRecordStore;
import au.gov.asd.tac.constellation.graph.processing.GraphRecordStoreUtilities;
import au.gov.asd.tac.constellation.graph.processing.RecordStore;
import au.gov.asd.tac.constellation.plugins.Plugin;
import au.gov.asd.tac.constellation.plugins.PluginException;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.training.solutions.OutbreakUtilities;
import au.gov.asd.tac.constellation.views.dataaccess.plugins.DataAccessPlugin;
import au.gov.asd.tac.constellation.views.dataaccess.plugins.DataAccessPluginCoreType;
import au.gov.asd.tac.constellation.views.dataaccess.templates.RecordStoreQueryPlugin;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * Import Infected Cities Plugin.
 */
@ServiceProviders({
    @ServiceProvider(service = DataAccessPlugin.class)
    ,
    @ServiceProvider(service = Plugin.class)
})
@NbBundle.Messages("ImportInfectedCitiesPlugin=Import Infected Cities")
public class ImportInfectedCitiesPlugin extends RecordStoreQueryPlugin implements DataAccessPlugin {

    @Override
    protected RecordStore query(final RecordStore query, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        RecordStore result = new GraphRecordStore();

        for (OutbreakUtilities.City infectedCity : OutbreakUtilities.getInfectedCities()) {
            result.add();

//            result.set(GraphRecordStoreUtilities.SOURCE + VisualConcept.VERTEX_ATTRIBUTE_IDENTIFIER, city.getName());
//            result.set(GraphRecordStoreUtilities.SOURCE + AnalyticConcept.VERTEX_ATTRIBUTE_TYPE, AnalyticConcept.VERTEX_TYPE_LOCATION);
//            result.set(GraphRecordStoreUtilities.SOURCE + "Population" + "<" + IntegerAttributeDescription.ATTRIBUTE_NAME + ">", city.getPopulation());
//            result.set(GraphRecordStoreUtilities.SOURCE + SpatialConcept.VERTEX_ATTRIBUTE_LATITUDE, city.getLatitude());
//            result.set(GraphRecordStoreUtilities.SOURCE + SpatialConcept.VERTEX_ATTRIBUTE_LONGITUDE, city.getLongitude());
//            result.set(GraphRecordStoreUtilities.SOURCE + "Outbreak", city.getOutbreak());
            OutbreakUtilities.addCityToRecord(infectedCity, result, GraphRecordStoreUtilities.SOURCE);
        }

        return result;
    }

    @Override
    public String getType() {
        return DataAccessPluginCoreType.EXPERIMENTAL;
    }

    @Override
    public int getPosition() {
        return 0;
    }
}
