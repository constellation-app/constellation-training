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

import au.gov.asd.tac.constellation.graph.GraphReadMethods;
import au.gov.asd.tac.constellation.pluginframework.parameters.PluginParameters;
import au.gov.asd.tac.constellation.views.histogram.Bin;
import au.gov.asd.tac.constellation.views.histogram.bins.AttributeBin;
import au.gov.asd.tac.constellation.views.histogram.bins.IntBin;
import au.gov.asd.tac.constellation.views.histogram.formats.BinFormatter;

/**
 * Total Infected Formatter.
 */
//@ServiceProvider(service = BinFormatter.class)
public class TotalInfectionsFormatter extends BinFormatter {

    public TotalInfectionsFormatter() {
        // Call the super constructor with the name and position of this formatter
    }

    @Override
    public boolean appliesToBin(Bin bin) {
        // Make sure the bin is a bin for Outbreak attributes
        return false;
    }

    @Override
    public Bin createBin(final GraphReadMethods graph, final int attribute, final PluginParameters parameters, final Bin bin) {
        return new TotalInfectionsBin((AttributeBin) bin);
    }

    private class TotalInfectionsBin extends IntBin {

        private final AttributeBin bin;

        public TotalInfectionsBin(final AttributeBin bin) {
            this.bin = bin;
        }

        @Override
        public Bin create() {
            return new TotalInfectionsBin(bin);
        }

        @Override
        public void setKey(final GraphReadMethods graph, final int attribute, final int element) {
        }

        @Override
        public void prepareForPresentation() {
        }
    }
}
