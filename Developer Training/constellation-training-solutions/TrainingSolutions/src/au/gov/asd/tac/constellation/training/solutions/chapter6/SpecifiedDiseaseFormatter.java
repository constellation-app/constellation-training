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
package au.gov.asd.tac.constellation.training.solutions.chapter6;

import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameter;
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.plugins.parameters.types.StringParameterType;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.chapter3.OutbreakAttributeDescription;
import au.gov.asd.tac.constellation.views.histogram.Bin;
import au.gov.asd.tac.constellation.views.histogram.bins.AttributeBin;
import au.gov.asd.tac.constellation.views.histogram.bins.IntBin;
import au.gov.asd.tac.constellation.views.histogram.formats.BinFormatter;
import org.openide.util.lookup.ServiceProvider;

/**
 * Specific Disease Formatter.
 */
@ServiceProvider(service = BinFormatter.class)
public class SpecifiedDiseaseFormatter extends BinFormatter {

    private static final String DISEASE_NAME_PARAMETER_ID = PluginParameter.buildId(SpecifiedDiseaseFormatter.class, "diseaseName");
    private static final String DISEASE_NAME_PARAMETER_LABEL = "Disease Name";

    public SpecifiedDiseaseFormatter() {
        super("Number Infected with Disease", 2);
    }

    @Override
    public boolean appliesToBin(final Bin bin) {
        return bin instanceof AttributeBin && ((AttributeBin) bin).getAttributeType().equals(OutbreakAttributeDescription.ATTRIBUTE_NAME);
    }

    @Override
    public PluginParameters createParameters() {
        final PluginParameters parameters = new PluginParameters();
        final PluginParameter diseaseNameParam = StringParameterType.build(DISEASE_NAME_PARAMETER_ID);
        diseaseNameParam.setName(DISEASE_NAME_PARAMETER_LABEL);
        parameters.addParameter(diseaseNameParam);
        return parameters;
    }

    @Override
    public Bin createBin(final GraphReadMethods graph, final int attribute, final PluginParameters parameters, final Bin bin) {
        return new SpecifiedDiseaseBin((AttributeBin) bin, parameters.getStringValue(DISEASE_NAME_PARAMETER_ID));
    }

    private class SpecifiedDiseaseBin extends IntBin {

        private final AttributeBin bin;
        private final String diseaseName;

        public SpecifiedDiseaseBin(final AttributeBin bin, final String diseaseName) {
            this.bin = bin;
            this.diseaseName = diseaseName;
        }

        @Override
        public Bin create() {
            return new SpecifiedDiseaseBin(bin, diseaseName);
        }

        @Override
        public void setKey(final GraphReadMethods graph, final int attribute, final int element) {
            bin.setKey(graph, attribute, element);
            key = bin.getKeyAsObject() == null ? -1
                    : !((Outbreak) bin.getKeyAsObject()).getDiseases().contains(diseaseName) ? 0
                    : ((Outbreak) bin.getKeyAsObject()).getOutbreakData().get(diseaseName);
        }

        @Override
        public void prepareForPresentation() {
            label = bin.getKeyAsObject() == null ? null : String.valueOf(key) + " people infected with " + diseaseName;
        }

    }
}
