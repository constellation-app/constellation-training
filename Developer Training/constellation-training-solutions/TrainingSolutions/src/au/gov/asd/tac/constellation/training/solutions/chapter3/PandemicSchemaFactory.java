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
package au.gov.asd.tac.constellation.training.solutions.chapter3;

import au.gov.asd.tac.constellation.graph.GraphWriteMethods;
import au.gov.asd.tac.constellation.graph.schema.Schema;
import au.gov.asd.tac.constellation.graph.schema.SchemaFactory;
import au.gov.asd.tac.constellation.graph.schema.analytic.AnalyticSchemaFactory;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.AnalyticConcept;
import au.gov.asd.tac.constellation.graph.schema.concept.SchemaConcept;
import au.gov.asd.tac.constellation.graph.schema.concept.SchemaConcept.ConstellationViewsConcept;
import au.gov.asd.tac.constellation.graph.schema.type.SchemaVertexType;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import au.gov.asd.tac.constellation.utilities.color.ConstellationColor;
import au.gov.asd.tac.constellation.utilities.icon.AnalyticIconProvider;
import au.gov.asd.tac.constellation.utilities.icon.ConstellationIcon;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Schema Factory.
 */
@ServiceProvider(service = SchemaFactory.class, position = 0)
public class PandemicSchemaFactory extends AnalyticSchemaFactory {

    public static final String NAME = "au.gov.asd.tac.constellation.training.schema.PandemicSchemaFactory";

    private static final ConstellationIcon ICON_SYMBOL = AnalyticIconProvider.MALWARE;
    private static final ConstellationColor ICON_COLOR = ConstellationColor.PINK;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getLabel() {
        return "Pandemic Graph";
    }

    @Override
    public String getDescription() {
        return "This graph provides support for analysing a pandemic - for training purposes";
    }

    @Override
    public ConstellationIcon getIconSymbol() {
        return ICON_SYMBOL;
    }

    @Override
    public ConstellationColor getIconColor() {
        return ICON_COLOR;
    }

    @Override
    public Set<Class<? extends SchemaConcept>> getRegisteredConcepts() {
        final Set<Class<? extends SchemaConcept>> registeredConcepts = new HashSet<>();
        registeredConcepts.add(ConstellationViewsConcept.class);
        registeredConcepts.add(VisualConcept.class);
        registeredConcepts.add(AnalyticConcept.class);
        return Collections.unmodifiableSet(registeredConcepts);
    }

    @Override
    public Schema createSchema() {
        return new PandemicSchema(this);
    }

    /**
     * Pandemic Schema.
     */
    protected class PandemicSchema extends AnalyticSchema {

        public PandemicSchema(final SchemaFactory factory) {
            super(factory);
        }

        @Override
        public void completeVertex(final GraphWriteMethods graph, final int vertex) {
            final int typeAttributeId = AnalyticConcept.VertexAttribute.TYPE.get(graph);
            final SchemaVertexType vertexType = graph.getObjectValue(typeAttributeId, vertex);
            if (AnalyticConcept.VertexType.LOCATION.equals(vertexType)) {
                graph.setObjectValue(typeAttributeId, vertex, PandemicConcept.VertexType.CITY);
            }

            super.completeVertex(graph, vertex);
        }
    }
}
