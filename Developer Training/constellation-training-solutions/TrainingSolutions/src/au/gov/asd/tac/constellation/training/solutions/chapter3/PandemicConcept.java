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

import au.gov.asd.tac.constellation.graph.GraphElementType;
import au.gov.asd.tac.constellation.graph.attribute.IntegerAttributeDescription;
import au.gov.asd.tac.constellation.graph.attribute.StringAttributeDescription;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.AnalyticConcept;
import au.gov.asd.tac.constellation.graph.schema.attribute.SchemaAttribute;
import au.gov.asd.tac.constellation.graph.schema.concept.SchemaConcept;
import au.gov.asd.tac.constellation.graph.schema.type.SchemaTransactionType;
import au.gov.asd.tac.constellation.graph.schema.type.SchemaVertexType;
import au.gov.asd.tac.constellation.utilities.color.ConstellationColor;
import au.gov.asd.tac.constellation.utilities.icon.AnalyticIconProvider;
import au.gov.asd.tac.constellation.utilities.icon.IconManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Concept.
 */
@ServiceProvider(service = SchemaConcept.class)
public class PandemicConcept extends SchemaConcept {

    @Override
    public String getName() {
        return "Pandemic";
    }

    @Override
    public Set<Class<? extends SchemaConcept>> getParents() {
        final Set<Class<? extends SchemaConcept>> parentSet = new HashSet<>();
        parentSet.add(AnalyticConcept.class);
        return Collections.unmodifiableSet(parentSet);
    }

    public static class VertexAttribute {

        public static final SchemaAttribute OUTBREAK = new SchemaAttribute.Builder(GraphElementType.VERTEX, OutbreakAttributeDescription.ATTRIBUTE_NAME, "Outbreak")
                .setDescription("An outbreak consisting of one or more diseases and their influence")
                .create()
                .build();
        public static final SchemaAttribute POPULATION = new SchemaAttribute.Builder(GraphElementType.VERTEX, IntegerAttributeDescription.ATTRIBUTE_NAME, "Population")
                .setDescription("The population of a geographic region")
                .build();
        public static final SchemaAttribute OUTBREAK_COUNT = new SchemaAttribute.Builder(GraphElementType.VERTEX, IntegerAttributeDescription.ATTRIBUTE_NAME, "Outbreak.Count")
                .setDescription("The count of outbreaks")
                .build();
    }

    public static class TransactionAttribute {

        public static final SchemaAttribute NUMBER_OF_PASSENGERS = new SchemaAttribute.Builder(GraphElementType.TRANSACTION, IntegerAttributeDescription.ATTRIBUTE_NAME, "Number of Passengers")
                .setDescription("The number of passengers on a flight")
                .build();
        public static final SchemaAttribute AIRLINE = new SchemaAttribute.Builder(GraphElementType.TRANSACTION, StringAttributeDescription.ATTRIBUTE_NAME, "Airline")
                .setDescription("The airline associated with a flight")
                .build();
    }

    @Override
    public Collection<SchemaAttribute> getSchemaAttributes() {
        final List<SchemaAttribute> schemaAttributes = new ArrayList<>();
        schemaAttributes.add(VertexAttribute.OUTBREAK);
        schemaAttributes.add(VertexAttribute.POPULATION);
        schemaAttributes.add(VertexAttribute.OUTBREAK_COUNT);
        schemaAttributes.add(TransactionAttribute.NUMBER_OF_PASSENGERS);
        schemaAttributes.add(TransactionAttribute.AIRLINE);
        return Collections.unmodifiableCollection(schemaAttributes);
    }

    public static class VertexType {

        public static final SchemaVertexType CITY = new SchemaVertexType.Builder("City")
                .setDescription("A node representing a city, eg. Canberra, Australia.")
                .setColor(ConstellationColor.CLOUDS)
                .setForegroundIcon(AnalyticIconProvider.GLOBE)
                .setBackgroundIcon(IconManager.getIcon("Background.Flat Square"))
                .build();
    }

    @Override
    public List<SchemaVertexType> getSchemaVertexTypes() {
        final List<SchemaVertexType> schemaVertexTypes = new ArrayList<>();
        schemaVertexTypes.add(VertexType.CITY);
        return Collections.unmodifiableList(schemaVertexTypes);
    }

    public static class TransactionType {

        public static final SchemaTransactionType ROUTE = new SchemaTransactionType.Builder("Route")
                .setDescription("A route connecting two locations.")
                .setColor(ConstellationColor.CLOUDS)
                .build();
        public static final SchemaTransactionType FLIGHT = new SchemaTransactionType.Builder("Flight")
                .setDescription("A flight route connecting two locations.")
                .setSuperType(ROUTE)
                .setColor(ConstellationColor.BANANA)
                .build();
    }

    @Override
    public List<SchemaTransactionType> getSchemaTransactionTypes() {
        final List<SchemaTransactionType> schemaTransactionTypes = new ArrayList<>();
        schemaTransactionTypes.add(TransactionType.ROUTE);
        schemaTransactionTypes.add(TransactionType.FLIGHT);
        return Collections.unmodifiableList(schemaTransactionTypes);
    }
}
