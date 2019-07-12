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
package au.gov.asd.tac.constellation.training.exercises.chapter6;

import au.gov.asd.tac.constellation.graph.attribute.interaction.AbstractAttributeInteraction;
import au.gov.asd.tac.constellation.graph.attribute.interaction.AttributeValueTranslator;
import au.gov.asd.tac.constellation.graph.attribute.interaction.ValueValidator;
import au.gov.asd.tac.constellation.training.exercises.Outbreak;
import au.gov.asd.tac.constellation.training.exercises.chapter3.OutbreakAttributeDescription;
import java.util.List;

/**
 * Outbreak Attribute Interaction.
 */
//@ServiceProvider(service = AbstractAttributeInteraction.class)
public class OutbreakAttributeInteraction extends AbstractAttributeInteraction<Outbreak> {

    @Override
    public String getDataType() {
        return OutbreakAttributeDescription.ATTRIBUTE_NAME;
    }

    @Override
    protected Class<Outbreak> getValueType() {
        return Outbreak.class;
    }

    @Override
    public String getDisplayText(final Object attrVal) {
        // Return a user-friendly String representation of the attribute value.
        return null;
    }

    @Override
    public List<String> getPreferredEditTypes() {
        return super.getPreferredEditTypes();
    }

    @Override
    public AttributeValueTranslator toEditTranslator(final String dataType) {
        return super.toEditTranslator(dataType);
    }

    @Override
    public AttributeValueTranslator fromEditTranslator(final String dataType) {
        return super.fromEditTranslator(dataType);
    }

    @Override
    public ValueValidator fromEditValidator(final String dataType) {
        return super.fromEditValidator(dataType);
    }
}
