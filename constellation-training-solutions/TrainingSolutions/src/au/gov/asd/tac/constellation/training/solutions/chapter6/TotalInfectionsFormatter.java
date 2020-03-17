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
import au.gov.asd.tac.constellation.plugins.parameters.PluginParameters;
import au.gov.asd.tac.constellation.training.solutions.Outbreak;
import au.gov.asd.tac.constellation.training.solutions.chapter3.OutbreakAttributeDescription;
import au.gov.asd.tac.constellation.views.histogram.Bin;
import au.gov.asd.tac.constellation.views.histogram.bins.AttributeBin;
import au.gov.asd.tac.constellation.views.histogram.bins.IntBin;
import au.gov.asd.tac.constellation.views.histogram.formats.BinFormatter;
import org.openide.util.lookup.ServiceProvider;

/**
 * Total Infected Formatter.
 */
@ServiceProvider(service = BinFormatter.class)
public class TotalInfectionsFormatter extends BinFormatter {

    public TotalInfectionsFormatter() {
        super("Total People Infected", 1);
    }

    @Override
    public boolean appliesToBin(final Bin bin) {
        return bin instanceof AttributeBin && ((AttributeBin) bin).getAttributeType().equals(OutbreakAttributeDescription.ATTRIBUTE_NAME);
    }

    @Override
    public Bin createBin(final GraphReadMethods graph, final int attribute, final PluginParameters parameters, final Bin bin) {
        return new TotalInfectionsdBin((AttributeBin) bin);
    }

    private class TotalInfectionsdBin extends IntBin {

        private final AttributeBin bin;

        public TotalInfectionsdBin(final AttributeBin bin) {
            this.bin = bin;
        }

        @Override
        public Bin create() {
            return new TotalInfectionsdBin(bin);
        }

        @Override
        public void setKey(final GraphReadMethods graph, final int attribute, final int element) {
            bin.setKey(graph, attribute, element);
            key = bin.key == null ? -1
                    : ((Outbreak) bin.key).getNumberOfDiseases() == 0 ? 0
                    : ((Outbreak) bin.key).getOutbreakData().values().stream().reduce((x, y) -> {
                        return x + y;
                    }).get();
        }

        @Override
        public void prepareForPresentation() {
            label = bin.key == null ? null : String.valueOf(key);
        }

    }
}
