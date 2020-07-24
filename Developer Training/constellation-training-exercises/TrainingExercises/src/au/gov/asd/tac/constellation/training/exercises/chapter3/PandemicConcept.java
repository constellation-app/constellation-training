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
package au.gov.asd.tac.constellation.training.exercises.chapter3;

import au.gov.asd.tac.constellation.graph.GraphElementType;
import au.gov.asd.tac.constellation.graph.attribute.IntegerAttributeDescription;
import au.gov.asd.tac.constellation.graph.attribute.StringAttributeDescription;
import au.gov.asd.tac.constellation.graph.schema.attribute.SchemaAttribute;
import au.gov.asd.tac.constellation.graph.schema.concept.SchemaConcept;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Concept.
 *
 * TODO: register this concept as the SchemaCocnept class.
 */
@ServiceProvider(service = SchemaConcept.class)
public class PandemicConcept extends SchemaConcept {

    @Override
    public String getName() {
        // TODO: give this schema concept a displayable name.
        return null;
    }

    @Override
    public Set<Class<? extends SchemaConcept>> getParents() {
        // TODO: give this schema concept a parent concept.
        return null;
    }

    // TODO: create an 'Outbreak' schema vertex attribute for this schema concept.
    public static class VertexAttribute {

        public static final SchemaAttribute POPULATION = new SchemaAttribute.Builder(GraphElementType.VERTEX, IntegerAttributeDescription.ATTRIBUTE_NAME, "Population")
                .setDescription("The population of a geographic region")
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
        schemaAttributes.add(VertexAttribute.POPULATION);
        schemaAttributes.add(TransactionAttribute.NUMBER_OF_PASSENGERS);
        schemaAttributes.add(TransactionAttribute.AIRLINE);
        return Collections.unmodifiableCollection(schemaAttributes);
    }

    // TODO: Add the city and flight types
}
