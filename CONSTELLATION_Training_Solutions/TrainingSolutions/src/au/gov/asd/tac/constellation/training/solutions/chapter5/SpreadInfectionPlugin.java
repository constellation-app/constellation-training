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
package au.gov.asd.tac.constellation.training.solutions.chapter5;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.pluginframework.Plugin;
import au.gov.asd.tac.constellation.pluginframework.PluginException;
import au.gov.asd.tac.constellation.pluginframework.PluginInfo;
import au.gov.asd.tac.constellation.pluginframework.PluginInteraction;
import au.gov.asd.tac.constellation.pluginframework.PluginType;
import au.gov.asd.tac.constellation.pluginframework.parameters.PluginParameter;
import au.gov.asd.tac.constellation.pluginframework.parameters.PluginParameters;
import au.gov.asd.tac.constellation.pluginframework.parameters.types.IntegerParameterType;
import au.gov.asd.tac.constellation.pluginframework.parameters.types.IntegerParameterType.IntegerParameterValue;
import au.gov.asd.tac.constellation.pluginframework.templates.SimpleEditPlugin;
import au.gov.asd.tac.constellation.schema.analyticschema.concept.AnalyticConcept;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.OutbreakUtilities;
import au.gov.asd.tac.constellation.training.solutions.chapter3.PandemicConcept;
import java.util.ArrayList;
import java.util.List;
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
    public PluginParameters createParameters() {
        final PluginParameters parameters = new PluginParameters();
        PluginParameter diseaseParameter = IntegerParameterType.build(NUMBER_OF_DAYS_PARAMETER_ID, new IntegerParameterValue(1));
        diseaseParameter.setName(NUMBER_OF_DAYS_PARAMETER_LABEL);
        parameters.addParameter(diseaseParameter);
        return parameters;
    }

    @Override
    protected void edit(final GraphWriteMethods writableGraph, final PluginInteraction interaction, final PluginParameters parameters) throws InterruptedException, PluginException {
        final int numberOfDays = parameters.getIntegerValue(NUMBER_OF_DAYS_PARAMETER_ID);
        final int outbreakAttributeId = PandemicConcept.VertexAttribute.OUTBREAK.get(writableGraph);
        final int vertexTypeAttributeId = AnalyticConcept.VertexAttribute.TYPE.get(writableGraph);
        final int transactionTypeAttributeId = AnalyticConcept.TransactionAttribute.TYPE.get(writableGraph);
        final int populationAttributeId = PandemicConcept.VertexAttribute.POPULATION.get(writableGraph);
        final int passengersAttributeId = PandemicConcept.TransactionAttribute.NUMBER_OF_PASSENGERS.get(writableGraph);
        if (outbreakAttributeId != Graph.NOT_FOUND && vertexTypeAttributeId != Graph.NOT_FOUND && transactionTypeAttributeId != Graph.NOT_FOUND && populationAttributeId != Graph.NOT_FOUND) {
            for (int vertexPosition = 0; vertexPosition < writableGraph.getVertexCount(); vertexPosition++) {
                final int vertexId = writableGraph.getVertex(vertexPosition);
                final Outbreak outbreak = writableGraph.getObjectValue(outbreakAttributeId, vertexId);
                final int population = writableGraph.getIntValue(populationAttributeId, vertexId);
                if (PandemicConcept.VertexType.CITY.equals(writableGraph.getObjectValue(vertexTypeAttributeId, vertexId))) {
                    List<Outbreak> neighbourOutbreaks = new ArrayList<>();
                    List<Integer> neighbourPopulations = new ArrayList<>();
                    List<Integer> neighbourDailyFilghtVolumes = new ArrayList<>();
                    for (int vertexNeighbourPosition = 0; vertexNeighbourPosition < writableGraph.getVertexNeighbourCount(vertexId); vertexNeighbourPosition++) {
                        final int neighbourId = writableGraph.getVertexNeighbour(vertexId, vertexNeighbourPosition);
                        final int neighbourLink = writableGraph.getLink(vertexId, neighbourId);
                        final Outbreak neighbourOutbreak = writableGraph.getObjectValue(outbreakAttributeId, neighbourId);
                        int passengerTotal = 0;
                        for (int neighbourTransactionPosition = 0; neighbourTransactionPosition < writableGraph.getLinkTransactionCount(neighbourLink); neighbourTransactionPosition++) {
                            final int neighbourTransactionId = writableGraph.getLinkTransaction(neighbourLink, neighbourTransactionPosition);
                            if (PandemicConcept.TransactionType.FLIGHT.equals(writableGraph.getObjectValue(transactionTypeAttributeId, neighbourTransactionId)) && writableGraph.getTransactionDestinationVertex(neighbourTransactionId) == vertexId) {
                                passengerTotal += writableGraph.getIntValue(passengersAttributeId, neighbourTransactionId);
                            }
                        }
                        if (neighbourOutbreak != null && passengerTotal > 0) {
                            neighbourOutbreaks.add(neighbourOutbreak);
                            neighbourPopulations.add(writableGraph.getIntValue(populationAttributeId, neighbourId));
                            neighbourDailyFilghtVolumes.add(passengerTotal);
                        }
                    }
                    writableGraph.setObjectValue(outbreakAttributeId, vertexId, OutbreakUtilities.spreadDisease(outbreak, population, neighbourOutbreaks, neighbourPopulations, neighbourDailyFilghtVolumes, numberOfDays));
                }
            }
        }
    }
}
