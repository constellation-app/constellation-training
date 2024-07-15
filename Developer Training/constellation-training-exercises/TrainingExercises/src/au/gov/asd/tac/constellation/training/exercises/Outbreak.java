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
package au.gov.asd.tac.constellation.training.exercises;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Outbreak.
 */
public class Outbreak {

    private final Map<String, Integer> outbreakData;

    public Outbreak() {
        this(Collections.emptyMap());
    }

    public Outbreak(final Map<String, Integer> outbreakData) {
        this.outbreakData = new HashMap<>();
        final Iterator<String> iter = outbreakData.keySet().iterator();
        while (iter.hasNext()) {
            final String disease = iter.next();
            if (outbreakData.get(disease) == 0) {
                iter.remove();
            }
        }
        this.outbreakData.putAll(outbreakData);
    }

    public Map<String, Integer> getOutbreakData() {
        return Collections.unmodifiableMap(outbreakData);
    }

    public Set<String> getDiseases() {
        return Collections.unmodifiableSet(outbreakData.keySet());
    }

    public int getNumberOfDiseases() {
        return outbreakData.keySet().size();
    }

    public int getAffectedPopulation(final String diseaseName) {
        return outbreakData.get(diseaseName);
    }

    public Outbreak spreadDisease(final String diseaseName, final Integer populationAffected) {
        final Outbreak spreadOutbreak = new Outbreak(outbreakData);
        if (outbreakData.containsKey(diseaseName)) {
            spreadOutbreak.outbreakData.put(diseaseName, outbreakData.get(diseaseName) + populationAffected);
        } else {
            spreadOutbreak.outbreakData.put(diseaseName, populationAffected);
        }
        return spreadOutbreak;
    }

    public Outbreak treatDisease(final String diseaseName, final Integer populationAffected) {
        final Outbreak treatedOutbreak = new Outbreak(outbreakData);
        if (outbreakData.containsKey(diseaseName)) {
            treatedOutbreak.outbreakData.put(diseaseName, outbreakData.get(diseaseName) - populationAffected);
        }
        return treatedOutbreak;
    }

    public Outbreak cureDisease(final String diseaseName) {
        final Outbreak curedOutbreak = new Outbreak(outbreakData);
        curedOutbreak.outbreakData.remove(diseaseName);
        return curedOutbreak;
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        outbreakData.forEach((name, value) -> {
            str.append(name).append(":").append(value).append(",");
        });
        if (str.length() > 1) {
            str.replace(str.length() - 1, str.length(), "");
        }
        return str.toString();
    }

    public static Outbreak valueOf(final String s) {
        if (s == null) {
            return null;
        }
        final Outbreak outbreak = new Outbreak();
        if (!s.isEmpty()) {
            final String[] diseases = s.split(",");
            for (final String disease : diseases) {
                final String[] nameAndValue = disease.split(":");
                try {
                    outbreak.outbreakData.put(nameAndValue[0], Integer.valueOf(nameAndValue[1]));
                } catch (final ArrayIndexOutOfBoundsException | NumberFormatException ex) {
                    throw new IllegalArgumentException("Not a valid outbreak status");
                }
            }
        }
        return outbreak;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Outbreak ? outbreakData.equals(((Outbreak) obj).outbreakData) : false;
    }

    @Override
    public int hashCode() {
        return outbreakData.hashCode();
    }

    /*
     @Override
     public int compareTo(final Outbreak o) {
     final Iterator<Integer> numsInfected = outbreakData.values().stream().sorted(Collections.reverseOrder()).collect(Collectors.toList()).iterator();
     final Iterator<Integer> oNumsInfected = ((Outbreak) o).outbreakData.values().stream().sorted(Collections.reverseOrder()).collect(Collectors.toList()).iterator();

     while (numsInfected.hasNext() && oNumsInfected.hasNext()) {
     final int num = numsInfected.next();
     final int oNum = oNumsInfected.next();
     if (num != oNum) {
     return Integer.compare(num, oNum);
     }
     }
     return Boolean.compare(numsInfected.hasNext(), oNumsInfected.hasNext());
     }
     */
}
