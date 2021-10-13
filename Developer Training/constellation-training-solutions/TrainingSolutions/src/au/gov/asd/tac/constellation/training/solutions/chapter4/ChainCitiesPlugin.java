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

import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.interaction.InteractiveGraphPluginRegistry;
import au.gov.asd.tac.constellation.graph.processing.GraphRecordStore;
import au.gov.asd.tac.constellation.graph.processing.GraphRecordStoreUtilities;
import au.gov.asd.tac.constellation.graph.processing.RecordStore;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.graph.visual.VisualGraphPluginRegistry;
import au.gov.asd.tac.constellation.plugins.Plugin;
import au.gov.asd.tac.constellation.plugins.PluginException;
import au.gov.asd.tac.constellation.plugins.PluginExecution;
import au.gov.asd.tac.constellation.plugins.PluginInteraction;
import au.gov.asd.tac.constellation.plugins.arrangements.ArrangementPluginRegistry;
import au.gov.asd.tac.constellation.plugins.logging.ConstellationLogger;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameter;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.plugins.parameters.types.DateTimeRange;
import au.gov.asd.tac.constellation.plugins.parameters.types.DateTimeRangeParameterType.DateTimeRangeParameterValue;
import au.gov.asd.tac.constellation.training.solutions.OutbreakUtilities;
import au.gov.asd.tac.constellation.views.dataaccess.CoreGlobalParameters;
import au.gov.asd.tac.constellation.views.dataaccess.plugins.DataAccessPlugin;
import au.gov.asd.tac.constellation.views.dataaccess.plugins.DataAccessPluginCoreType;
import au.gov.asd.tac.constellation.views.dataaccess.templates.RecordStoreQueryPlugin;
import java.util.Properties;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 * Chain Cities Plugin.
 */
@ServiceProviders({
    @ServiceProvider(service = DataAccessPlugin.class)
    ,
    @ServiceProvider(service = Plugin.class)
})
@NbBundle.Messages("ChainCitiesPlugin=Chain Cities")
public class ChainCitiesPlugin extends RecordStoreQueryPlugin implements DataAccessPlugin {

    @Override
    public PluginParameters createParameters() {
        final PluginParameters parameters = new PluginParameters();
        final PluginParameter<DateTimeRangeParameterValue> datetime = CoreGlobalParameters.DATETIME_RANGE_PARAMETER;
        parameters.addParameter(datetime);
        return parameters;
    }

    @Override
    protected RecordStore query(final RecordStore query, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {

        final DateTimeRange datetimeRange = parameters.getDateTimeRangeValue(CoreGlobalParameters.DATETIME_RANGE_PARAMETER_ID);
        final long startTime = datetimeRange.getZonedStartEnd()[0].toInstant().toEpochMilli();
        final long endTime = datetimeRange.getZonedStartEnd()[1].toInstant().toEpochMilli();

        RecordStore result = new GraphRecordStore();

        int currentStep = 0;

        query.reset();
        while (query.next()) {
            final String cityName = query.get(GraphRecordStoreUtilities.SOURCE + VisualConcept.VertexAttribute.IDENTIFIER);

            interaction.setProgress(++currentStep, query.size(), "Processing: " + cityName, true);

            for (OutbreakUtilities.Flight flight : OutbreakUtilities.getFlights(cityName, startTime, endTime)) {
                result.add();
                OutbreakUtilities.addFlightToRecord(flight, result);
            }

        }

        ConstellationLogger.getDefault().pluginInfo(this, "Successfully queried " + query.size() + " cities.");

        Properties loggingProperties = new Properties();
        loggingProperties.setProperty("ResultCount", String.valueOf(result.size()));
        ConstellationLogger.getDefault().pluginProperties(this, loggingProperties);

        return result;
    }

    @Override
    protected void edit(final GraphWriteMethods graph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        super.edit(graph, interaction, parameters);
        PluginExecution.withPlugin(VisualGraphPluginRegistry.DESELECT_ALL).executeNow(graph);
        PluginExecution.withPlugin(ArrangementPluginRegistry.TREES).executeNow(graph);
        PluginExecution.withPlugin(InteractiveGraphPluginRegistry.RESET_VIEW).executeNow(graph);
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
