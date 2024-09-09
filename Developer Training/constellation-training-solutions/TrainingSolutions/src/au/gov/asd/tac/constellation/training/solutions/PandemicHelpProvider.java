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
package au.gov.asd.tac.constellation.training.solutions;

import au.gov.asd.tac.constellation.help.HelpPageProvider;
import au.gov.asd.tac.constellation.help.utilities.Generator;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Help Provider.
 */
@ServiceProvider(service = HelpPageProvider.class, position = 0)
@NbBundle.Messages("PandemicHelpProvider=Pandemic Help Provider")
public class PandemicHelpProvider extends HelpPageProvider {

    private static final String CODEBASE_NAME = "constellation";
    private static final String SEP = File.separator;

    /**
     * Provides a map of all the help files Maps the file name to the md file name
     *
     * @return Map of the file names vs md file names
     */
    @Override
    public Map<String, String> getHelpMap() {
        final Map<String, String> map = new HashMap<>();
        final String pandemicModulePath = ".." + SEP + getFrontPath() + "ext" + SEP + "docs" + SEP + "TrainingSolutions" + SEP + "src" + SEP + "au" + SEP + "gov" + SEP + "asd"
                + SEP + "tac" + SEP + CODEBASE_NAME + SEP + "training" + SEP + "solutions" + SEP;

        map.put("au.gov.asd.tac.constellation.training.solutions.chapter4.ImportCustomCitiesPlugin", pandemicModulePath + "import-custom-cities.md");
        return map;
    }

    /**
     * Provides a location as a string of the TOC xml file in the module
     *
     * @return List of help resources
     */
    @Override
    public String getHelpTOC() {
        return getFrontPath() + "ext" + SEP + "docs" + SEP + "TrainingSolutions" + SEP + "src" + SEP + "au" + SEP + "gov" + SEP + "asd" + SEP + "tac" + SEP
                + CODEBASE_NAME + SEP + "training" + SEP + "solutions" + SEP + "pandemic-toc.xml";
    }
    
    private String getFrontPath() {
        // check where the application is being run from as the location of help pages is slightly different between running from a release zip and running locally from netbeans
        final boolean isRunningLocally = Generator.getBaseDirectory().contains("build" + SEP + "cluster");
        
        final StringBuilder frontPathBuilder = new StringBuilder("..").append(SEP).append("..").append(SEP);
        if (isRunningLocally) {
            frontPathBuilder.append("..").append(SEP).append("..").append(SEP);
        }
        frontPathBuilder.append("constellation-training").append(SEP)
                .append("Developer Training").append(SEP)
                .append("constellation-training-solutions").append(SEP);
        if (isRunningLocally) {
            frontPathBuilder.append("build").append(SEP).append("cluster").append(SEP);
        }
        frontPathBuilder.append("modules").append(SEP);
        
        return frontPathBuilder.toString();
    }
}
