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
package au.gov.asd.tac.constellation.training.solutions.chapter3;

import au.gov.asd.tac.constellation.visual.icons.ConstellationIcon;
import au.gov.asd.tac.constellation.visual.icons.ConstellationIconProvider;
import au.gov.asd.tac.constellation.visual.icons.FileIconData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Pandemic Icon Provider.
 */
@ServiceProvider(service = ConstellationIconProvider.class)
public class PandemicIconProvider implements ConstellationIconProvider {

    public static final ConstellationIcon BIOHAZARD = new ConstellationIcon.Builder("Biohazard", new FileIconData("modules/ext/biohazard.png", "au.gov.asd.tac.constellation.training"))
            .addCategory("Training")
            .build();

    @Override
    public List<ConstellationIcon> getIcons() {
        final List<ConstellationIcon> pandemicIcons = new ArrayList<>();
        pandemicIcons.add(BIOHAZARD);
        return Collections.unmodifiableList(pandemicIcons);
    }
}
