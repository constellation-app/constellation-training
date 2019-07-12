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
package au.gov.asd.tac.constellation.training.solutions.chapter4;

import au.gov.asd.tac.constellation.graph.processing.GraphRecordStore;
import au.gov.asd.tac.constellation.graph.processing.GraphRecordStoreUtilities;
import au.gov.asd.tac.constellation.graph.processing.RecordStore;
import au.gov.asd.tac.constellation.pluginframework.Plugin;
import au.gov.asd.tac.constellation.pluginframework.PluginException;
import au.gov.asd.tac.constellation.pluginframework.PluginInteraction;
import au.gov.asd.tac.constellation.pluginframework.PluginNotificationLevel;
import au.gov.asd.tac.constellation.pluginframework.parameters.PluginParameter;
import au.gov.asd.tac.constellation.pluginframework.parameters.PluginParameters;
import au.gov.asd.tac.constellation.pluginframework.parameters.types.StringParameterType;
import au.gov.asd.tac.constellation.pluginframework.parameters.types.StringParameterValue;
import au.gov.asd.tac.constellation.training.solutions.OutbreakUtilities;
import au.gov.asd.tac.constellation.views.dataaccess.DataAccessPlugin;
import au.gov.asd.tac.constellation.views.dataaccess.DataAccessPluginCoreType;
import au.gov.asd.tac.constellation.views.dataaccess.templates.RecordStoreQueryPlugin;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * Import Custom Cities Plugin.
 */
@ServiceProviders({
    @ServiceProvider(service = DataAccessPlugin.class)
    ,
    @ServiceProvider(service = Plugin.class)
})
@NbBundle.Messages("ImportCustomCitiesPlugin=Import Custom Cities")
public class ImportCustomCitiesPlugin extends RecordStoreQueryPlugin implements DataAccessPlugin {

    public static final String CITIES_PARAMETER_ID = PluginParameter.buildId(ImportCustomCitiesPlugin.class, "cities");

    @Override
    public PluginParameters createParameters() {
        final PluginParameters parameters = new PluginParameters();
        final PluginParameter<StringParameterValue> citiesParameter = StringParameterType.build(CITIES_PARAMETER_ID);
        citiesParameter.setName("Cities");
        citiesParameter.setDescription("Enter the names of cities to import, one per line");
        StringParameterType.setLines(citiesParameter, 10);
        parameters.addParameter(citiesParameter);
        return parameters;
    }

    @Override
    protected RecordStore query(final RecordStore query, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        RecordStore result = new GraphRecordStore();

        final String citiesString = parameters.getStringValue(CITIES_PARAMETER_ID);

        if (citiesString != null) {
            for (String cityName : citiesString.split("\n", -1)) {
                final OutbreakUtilities.City city = OutbreakUtilities.getCity(cityName);

                if (city == null) {
                    throw new PluginException(PluginNotificationLevel.INFO, "Unknown city name: " + cityName);
                }

                result.add();
                OutbreakUtilities.addCityToRecord(city, result, GraphRecordStoreUtilities.SOURCE);
            }
        }

        return result;
    }

    @Override
    public String getType() {
        return DataAccessPluginCoreType.EXPERIMENTAL;
    }

    @Override
    public int getPosition() {
        /*
         * Specify a position of 1 so that this plugin will appear directly below
         * the ImportInfectedCities plugin developed in Chapter 2.
         */
        return 1;
    }
}
