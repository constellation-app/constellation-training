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

import au.gov.asd.tac.constellation.graph.attribute.IntegerAttributeDescription;
import au.gov.asd.tac.constellation.graph.processing.GraphRecordStoreUtilities;
import au.gov.asd.tac.constellation.graph.processing.Record;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.AnalyticConcept;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.SpatialConcept;
import au.gov.asd.tac.constellation.graph.schema.analytic.concept.TemporalConcept;
import au.gov.asd.tac.constellation.graph.schema.visual.concept.VisualConcept;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 * Outbreak Utilities.
 */
public class OutbreakUtilities {

    private static final Logger LOGGER = Logger.getLogger(OutbreakUtilities.class.getName());

    private static final double DAILY_LOCAL_SPREAD_FACTOR = 0.01;

    /**
     * Causes the diseases from the specified outbreak to spread, both within
     * their current city, as well as to other cities that are connected by
     * flights.
     *
     * @param outbreak the outbreak to spread.
     * @param hostPopulation the population of the host city where the outbreak
     * is located.
     * @param neighboutOutbreaks the outbreak instances of neighbouring cities.
     * @param neighbourPopulations the populations of neighbouring cities.
     * @param neighbourDailyFlightVolumes the number of flights between the host
     * city and its neighbours.
     * @param numberOfDays the number of days over which to simulate the spread.
     * @return
     */
    public static Outbreak spreadDisease(final Outbreak outbreak, final int hostPopulation, final List<Outbreak> neighboutOutbreaks, final List<Integer> neighbourPopulations, final List<Integer> neighbourDailyFlightVolumes, final int numberOfDays) {
        final Map<String, Integer> spreadOutbreakData = new HashMap<>();
        if (outbreak != null) {
            outbreak.getOutbreakData().forEach((diseaseName, afflicted) -> {
                spreadOutbreakData.put(diseaseName, (int) Math.min(Math.floor(afflicted * Math.pow(1 + DAILY_LOCAL_SPREAD_FACTOR, numberOfDays)), hostPopulation));
            });
            for (int i = 0; i < neighboutOutbreaks.size(); i++) {
                final Outbreak neighbourOutbreak = neighboutOutbreaks.get(i);
                final double population = neighbourPopulations.get(i);
                final double neighbourDailyFlightVolume = neighbourDailyFlightVolumes.get(i);
                neighbourOutbreak.getOutbreakData().forEach((diseaseName, afflicted) -> {
                    spreadOutbreakData.put(diseaseName,
                            Math.min((spreadOutbreakData.containsKey(diseaseName) ? spreadOutbreakData.get(diseaseName) : 0)
                                    + (int) Math.ceil(neighbourDailyFlightVolume * (afflicted / population) * numberOfDays), hostPopulation)
                    );
                });
            }
        }
        return new Outbreak(spreadOutbreakData);
    }

    /**
     * Returns the City instance with the specified name or null if not such
     * city exists.
     *
     * @param cityName the name of the city.
     * @return the City instance with the specified name or null if not such
     * city exists.
     */
    public static City getCity(final String cityName) {
        return getCities().get(cityName.toLowerCase());
    }

    /**
     * Returns a list of all cities that currently have an outbreak.
     *
     * @return a list of all cities that currently have an outbreak.
     */
    public static List<City> getInfectedCities() {
        final List<City> infectedCities = new ArrayList<>();
        for (final City city : getCities().values()) {
            if (city.getOutbreak() != null && city.getOutbreak().getNumberOfDiseases() > 0) {
                infectedCities.add(city);
            }
        }
        return infectedCities;
    }

    /**
     * Returns a list of all flights arriving or departing from the specified
     * city.
     *
     * @param cityName the name of the city.
     * @return a list of all flights arriving or departing from the specified
     * city.
     */
    public static List<Flight> getFlights(final String cityName) {
        final List<Flight> flights = new ArrayList<>();
        final City city = getCity(cityName);
        if (city != null) {
            for (final Flight flight : getFlights()) {
                if (flight.getSource() == city || flight.getDestination() == city) {
                    flights.add(flight);
                }
            }
        }
        return flights;
    }

    /**
     * Returns a list of all Flights arriving or departing from the specified
     * city with a departure time within the specified range.
     *
     * @param cityName the name of the city.
     * @param startTime the start of the departure time range.
     * @param endTime the end of the departure time range.
     * @return a list of all Flights arriving or departing from the specified
     * city with a departure time within the specified range.
     */
    public static List<Flight> getFlights(final String cityName, final long startTime, final long endTime) {
        final List<Flight> flights = new ArrayList<>();
        final City city = getCity(cityName);
        if (city != null) {
            for (final Flight flight : getFlights()) {
                if (flight.getSource() == city || flight.getDestination() == city) {
                    if (flight.departureTime >= startTime && flight.departureTime <= endTime) {
                        flights.add(flight);
                    }
                }
            }
        }
        return flights;
    }

    /**
     * A City instance represents a city that can act as the departure or
     * arrival point for a Flight.
     */
    public static class City {

        private final String name;
        private final String country;
        private final int population;
        private final float latitude;
        private final float longitude;

        private Outbreak outbreak = null;

        public City(final String name, final String country, final int population, final float latitude, final float longitude) {
            this.name = name;
            this.country = country;
            this.population = population;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /**
         * Returns the name of the City.
         *
         * @return the name of the City.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the country the city is located in.
         *
         * @return the country the city is located in.
         */
        public String getCountry() {
            return country;
        }

        /**
         * Returns the population of the city.
         *
         * @return the population of the city.
         */
        public int getPopulation() {
            return population;
        }

        /**
         * Returns the latitude of the city's location.
         *
         * @return the latitude of the city's location.
         */
        public float getLatitude() {
            return latitude;
        }

        /**
         * Returns the longitude of the city's location.
         *
         * @return the longitude of the city's location.
         */
        public float getLongitude() {
            return longitude;
        }

        /**
         * Returns the current outbreak of the City, or null if not outbreak is
         * currently occurring.
         *
         * @return the current outbreak of the City.
         */
        public Outbreak getOutbreak() {
            return outbreak;
        }

        /**
         * Sets the current outbreak of the City.
         *
         * @param outbreak the new outbreak for the city, or null to represent
         * no outbreak in the city.
         */
        public void setOutbreak(final Outbreak outbreak) {
            this.outbreak = outbreak;
        }
    }

    /**
     * A Flight instance represents a single flight between two cities at a
     * specified time.
     */
    public static class Flight {

        private final City source;
        private final City destination;
        private final long departureTime;
        private final int passengers;
        private final String airline;

        public Flight(final City source, final City destination, final long departureTime, final int passengers, final String airline) {
            this.source = source;
            this.destination = destination;
            this.departureTime = departureTime;
            this.passengers = passengers;
            this.airline = airline;
        }

        /**
         * Returns the City the flight departs from.
         *
         * @return the City the flight departs from.
         */
        public City getSource() {
            return source;
        }

        /**
         * Returns the City the flight arrives in.
         *
         * @return the City the flight arrives in.
         */
        public City getDestination() {
            return destination;
        }

        /**
         * Returns the datetime when the Flight departs, specified in a format
         * suitable for passing to Constellation.
         *
         * @return the datetime when the Flight departs.
         */
        public String getDepartureTime() {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(departureTime), ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z");
            return zonedDateTime.format(formatter);
        }

        /**
         * Returns the number of passengers on the flight.
         *
         * @return the number of passengers on the flight.
         */
        public int getPassengers() {
            return passengers;
        }

        /**
         * Returns the airline and number of the flight.
         *
         * @return the airline and number of the flight.
         */
        public String getAirline() {
            return airline;
        }
    }

    /**
     * Adds the attributes of a City instance to the current row of a
     * RecordStore, specified as a Record. In general, the attributes will be
     * added as source or destination attributes as a City generally represents
     * a node in the graph.
     *
     * @param city the City instance to be added.
     * @param record the Record to add the City to.
     * @param category the category of the Record to add the city attributes
     * under.
     */
    public static void addCityToRecord(final City city, final Record record, final String category) {
        record.set(category + VisualConcept.VertexAttribute.IDENTIFIER, city.getName());
        record.set(category + AnalyticConcept.VertexAttribute.TYPE, AnalyticConcept.VertexType.LOCATION);
        record.set(category + "Population<" + IntegerAttributeDescription.ATTRIBUTE_NAME + ">", city.getPopulation());
        record.set(category + SpatialConcept.VertexAttribute.LATITUDE, city.getLatitude());
        record.set(category + SpatialConcept.VertexAttribute.LONGITUDE, city.getLongitude());

        if (city.getOutbreak() != null) {
            record.set(category + "Outbreak", city.getOutbreak());
        }
    }

    /**
     * Adds the attributes of a Flight instance to the current row of a
     * RecordStore, specified as a Record. The Flight attributes will be added
     * as transaction attributes in the Record.
     *
     * @param flight the Flight instance to be added.
     * @param record the Record to add the Flight to.
     */
    public static void addFlightToRecord(final Flight flight, final Record record) {
        addCityToRecord(flight.getSource(), record, GraphRecordStoreUtilities.SOURCE);
        addCityToRecord(flight.getDestination(), record, GraphRecordStoreUtilities.DESTINATION);
        record.set(GraphRecordStoreUtilities.TRANSACTION + TemporalConcept.TransactionAttribute.DATETIME, flight.getDepartureTime());
        record.set(GraphRecordStoreUtilities.TRANSACTION + "Number of Passengers<" + IntegerAttributeDescription.ATTRIBUTE_NAME + ">", flight.getPassengers());
        record.set(GraphRecordStoreUtilities.TRANSACTION + "Airline", flight.getAirline());
        record.set(GraphRecordStoreUtilities.TRANSACTION + AnalyticConcept.TransactionAttribute.TYPE, "Flight");
    }

    private static final File CITIES_FILE = InstalledFileLocator.getDefault().locate("modules/ext/Cities.csv", "au.gov.asd.constellation.training.exercises", true);
    private static final File OUTBREAKS_FILE = InstalledFileLocator.getDefault().locate("modules/ext/Outbreaks.csv", "au.gov.asd.constellation.training.exercises", true);
    private static final File FLIGHTS_FILE = InstalledFileLocator.getDefault().locate("modules/ext/Flights.csv", "au.gov.asd.constellation.training.exercises", true);

    private static final Map<String, City> CITIES = new HashMap<>();
    private static final List<Flight> FLIGHTS = new ArrayList<>();

    /**
     * Returns a list of all available cities, lazy-loading the data file as
     * required.
     *
     * @return a list of all available cities.
     */
    private static Map<String, City> getCities() {
        if (CITIES.isEmpty()) {
            try (final BufferedReader in = new BufferedReader(new FileReader(CITIES_FILE))) {
                final String headerLine = in.readLine();
                String line = in.readLine();
                while (line != null) {
                    final String[] fields = line.split(",", -1);
                    final String country = fields[0];
                    final String name = fields[1];
                    final String accentCity = fields[2];
                    final String region = fields[3];
                    final int population = Integer.parseInt(fields[4]);
                    final float latitude = Float.parseFloat(fields[5]);
                    final float longitude = Float.parseFloat(fields[6]);
                    City city = CITIES.get(name);
                    if (city == null || city.getPopulation() < population) {
                        city = new City(accentCity, country, population, latitude, longitude);
                        CITIES.put(name, city);
                    }
                    line = in.readLine();
                }
            } catch (final IOException ex) {
                Exceptions.printStackTrace(ex);
            }

            try (final BufferedReader in = new BufferedReader(new FileReader(OUTBREAKS_FILE))) {
                final String headerLine = in.readLine();
                String line = in.readLine();
                while (line != null) {
                    final String[] fields = line.split(",", -1);
                    final String cityName = fields[0];
                    final String infection = fields[1];
                    final int count = Integer.parseInt(fields[2]);
                    final City city = getCity(cityName);
                    if (city != null && city.getOutbreak() == null) {
                        city.setOutbreak(new Outbreak().spreadDisease(infection, count));
                    }
                    line = in.readLine();
                }
            } catch (final IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return CITIES;
    }

    /**
     * Returns a list of all available flights, lazy loading the data file as
     * required.
     *
     * @return a list of all available flights.
     */
    public static List<Flight> getFlights() {
        if (FLIGHTS.isEmpty()) {
            try (final BufferedReader in = new BufferedReader(new FileReader(FLIGHTS_FILE))) {
                final String headerLine = in.readLine();
                String line = in.readLine();
                while (line != null) {
                    final String[] fields = line.split(",", -1);
                    final String fromCityName = fields[0];
                    final String toCityName = fields[1];
                    final long departureTime = Long.parseLong(fields[2]);
                    final int passengerCount = Integer.parseInt(fields[3]);
                    final String airline = fields[4];

                    final City fromCity = getCity(fromCityName);
                    final City toCity = getCity(toCityName);
                    if (fromCity != null && toCity != null) {
                        final Flight flight = new Flight(fromCity, toCity, departureTime, passengerCount, airline);
                        FLIGHTS.add(flight);
                    }
                    line = in.readLine();
                }
            } catch (final IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return FLIGHTS;
    }

    /**
     * Cities will populations below this value will be excluded from getting
     * flights. This is to ensure that the graph of flight data includes mostly
     * well-known cities.
     */
    private static final int MIN_POPULATION = 500000;

    /**
     * Creates a new Flights.csv file with date times over the previous 30 days.
     * To use the output, replace the Flights.csv file in the
     * /release/modules/ext directory of both the Training Solutions and
     * Training Exercises modules.
     *
     * @param file the file path where the flight CSV file will be created.
     */
    public static void createFlights(final File file) {
        final List<String> airways = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            airways.add("" + (char) ('A' + (int) (Math.random() * 26)) + (char) ('A' + (int) (Math.random() * 26)));
        }

        final long timeRange = 30L * 24L * 60L;
        final long minuteMillis = 60L * 1000L;

        long currentTime = System.currentTimeMillis();
        currentTime -= currentTime % minuteMillis;

        final List<City> cities = new ArrayList<>(getCities().values());

        int maxPopulation = 0;
        final Iterator<City> cityIter = cities.iterator();
        while (cityIter.hasNext()) {
            final City city = cityIter.next();
            if (city.getPopulation() < MIN_POPULATION) {
                cityIter.remove();
            } else {
                maxPopulation = Math.max(maxPopulation, city.getPopulation());
            }
        }
        final float maxDistance = (float) Math.sqrt(180 * 180 * 2f);

        int flightCount = 0;
        int failed = 0;

        try (final BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write("fromCity,toCity,departureTime,passengers,airline");

            while (flightCount < 100000) {
                final City sourceCity = cities.get((int) (Math.random() * cities.size()));
                final City destinationCity = cities.get((int) (Math.random() * cities.size()));
                if (sourceCity != destinationCity && maxPopulation > 0) {
                    final float populationScore = (sourceCity.getPopulation() + destinationCity.getPopulation()) / (maxPopulation * 2.0f);

                    float latDifference = sourceCity.getLatitude() - destinationCity.getLatitude();
                    if (latDifference < 0) {
                        latDifference += 360;
                    }
                    if (latDifference > 180) {
                        latDifference = 360 - latDifference;
                    }

                    float lonDifference = sourceCity.getLongitude() - destinationCity.getLongitude();
                    if (lonDifference < 0) {
                        lonDifference += 360;
                    }
                    if (lonDifference > 180) {
                        lonDifference = 360 - lonDifference;
                    }

                    final float distance = (float) Math.sqrt(latDifference * latDifference + lonDifference * lonDifference);
                    final float distanceScore = 1.0f - (distance / maxDistance);

                    final float randomScore = (float) Math.random();

                    final float totalScore = (populationScore * 1.0f) + (distanceScore * 1.0f) + (randomScore * 0.5f);

                    if (totalScore > 1.45f) {
                        flightCount++;
                        if (flightCount % 1000 == 0) {
                            LOGGER.log(Level.INFO, "{0} {1}", new Object[]{flightCount, failed});
                        }
                        final long departureTime = currentTime - (long) (Math.random() * timeRange) * minuteMillis;
                        final int passengers = (int) (populationScore * 1000);

                        final String flight = airways.get((int) (Math.random() * airways.size())) + "-" + ((int) (Math.random() * 900) + 100);

                        out.write("\n" + sourceCity.getName() + "," + destinationCity.getName() + "," + departureTime + "," + passengers + "," + flight);
                    } else {
                        failed++;
                    }
                }
            }
        } catch (final IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        LOGGER.log(Level.INFO, "Flights: {0}", flightCount);
    }
}
