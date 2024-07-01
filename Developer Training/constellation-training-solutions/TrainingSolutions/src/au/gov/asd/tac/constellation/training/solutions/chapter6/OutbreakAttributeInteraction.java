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
package au.gov.asd.tac.constellation.training.solutions.chapter6;

import au.gov.asd.tac.constellation.graph.attribute.StringAttributeDescription;
import au.gov.asd.tac.constellation.graph.attribute.interaction.AbstractAttributeInteraction;
import au.gov.asd.tac.constellation.graph.attribute.interaction.AttributeValueTranslator;
import au.gov.asd.tac.constellation.graph.attribute.interaction.ValueValidator;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.chapter3.OutbreakAttributeDescription;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Outbreak Attribute Interaction.
 */
@ServiceProvider(service = AbstractAttributeInteraction.class)
public class OutbreakAttributeInteraction extends AbstractAttributeInteraction<Outbreak> {

    private static final String NO_OUTBREAKS_STRING = "Clear of Infection";

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
        if (attrVal == null) {
            return null;
        } else if (((Outbreak) attrVal).getOutbreakData().isEmpty()) {
            return NO_OUTBREAKS_STRING;
        }
        return attrVal.toString();
    }

    @Override
    public List<String> getPreferredEditTypes() {
        return Arrays.asList(StringAttributeDescription.ATTRIBUTE_NAME);
    }

    @Override
    public AttributeValueTranslator toEditTranslator(final String dataType) {
        if (dataType.equals(StringAttributeDescription.ATTRIBUTE_NAME)) {
            return v -> {
                return v == null ? null : ((Outbreak) v).toString();
            };
        }
        return super.toEditTranslator(dataType);
    }

    @Override
    public AttributeValueTranslator fromEditTranslator(final String dataType) {
        if (dataType.equals(StringAttributeDescription.ATTRIBUTE_NAME)) {
            return v -> {
                return v == null ? null : Outbreak.valueOf((String) v);
            };
        }
        return super.fromEditTranslator(dataType);
    }

    @Override
    public ValueValidator fromEditValidator(final String dataType) {
        if (dataType.equals(StringAttributeDescription.ATTRIBUTE_NAME)) {
            return v -> {
                try {
                    Outbreak.valueOf((String) v);
                    return null;
                } catch (final IllegalArgumentException ex) {
                    return "Invalid String for OutbreakStatus";
                }
            };
        }
        return super.fromEditValidator(dataType);
    }

    private static final Comparator<Outbreak> COMPARATOR = (o1, o2) -> {
        if (o1 == null) {
            return o2 == null ? 0 : -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return Integer.compare(o1.getNumberOfDiseases(), o2.getNumberOfDiseases());
        }
    };

    @Override
    public Comparator<Outbreak> createComparator() {
        return COMPARATOR;
    }
}
