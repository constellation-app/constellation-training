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

import au.gov.asd.tac.constellation.training.solutions.chapter2.ImportInfectedCitiesPlugin;
import au.gov.asd.tac.constellation.training.solutions.chapter4.ChainCitiesPlugin;
import au.gov.asd.tac.constellation.training.solutions.chapter4.ImportCustomCitiesPlugin;
import au.gov.asd.tac.constellation.training.solutions.chapter5.PandemicArrangementPlugin;
import au.gov.asd.tac.constellation.training.solutions.chapter5.PercentageAfflictedPlugin;
import au.gov.asd.tac.constellation.training.solutions.chapter5.SpreadInfectionPlugin;

/**
 * Pandemic Plugin Registry.
 */
public class PandemicPluginRegistry {

    public static final String IMPORT_INFECTED_CITIES = ImportInfectedCitiesPlugin.class.getName();
    public static final String IMPORT_CUSTOM_CITIES = ImportCustomCitiesPlugin.class.getName();
    public static final String CHAIN_CITIES = ChainCitiesPlugin.class.getName();

    public static final String ARRANGE_BY_GEOGRAPHIC_COORDINATES = PandemicArrangementPlugin.class.getName();
    public static final String SPREAD_INFECTION = SpreadInfectionPlugin.class.getName();
    public static final String PERCENTAGE_AFFLICTED = PercentageAfflictedPlugin.class.getName();
}
