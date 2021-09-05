/*
 * Benchmark of ARX's Heuristic Algorithms
 * Copyright 2020 by Thierry Meurers and contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.deidentifier.arx.benchmark;

import java.io.IOException;
import java.util.List;

import org.deidentifier.arx.ARXConfiguration.AnonymizationAlgorithm;
import org.deidentifier.arx.benchmark.BenchmarkSetup.BenchmarkDataset;

/**
 * Benchmark class defining tests for high-dimensional datasets with global
 * transformation. The utility improvement is tracked continuously over time.
 * 
 * @author Thierry Meurers
 */
public class BenchmarkExperiment2Global extends AbstractBenchmark {

    /**
     * Constructor
     * 
     * @param fileName
     */
    BenchmarkExperiment2Global(String fileName) {
        super(fileName, true, true);
    }

    /**
     * Entry point.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String args[]) throws IOException {
        new BenchmarkExperiment2Global("results/Experiment2_merged.csv").start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void generateTestConfigurations(List<TestConfiguration> testConfigs) {

        // Definition of properties that will be varied for the Benchmark
        AnonymizationAlgorithm[] algorithms = new AnonymizationAlgorithm[] { AnonymizationAlgorithm.BEST_EFFORT_BOTTOM_UP,
                                                                             AnonymizationAlgorithm.BEST_EFFORT_GENETIC,
                                                                             AnonymizationAlgorithm.BEST_EFFORT_TOP_DOWN };

        BenchmarkDataset[] datasets = new BenchmarkDataset[] { BenchmarkDataset.CREDITCARD,
                                                               BenchmarkDataset.MACH2019,
                                                               BenchmarkDataset.SS13ACS };

        PrivacyModel[] privacyModels = new PrivacyModel[] { PrivacyModel.K_ANONYMITY,
                                                            PrivacyModel.POPULATION_UNIQUENESS };

        // Number of testruns
        int testRuns = 6;

        // iterate through all possible configuration permutations
        for (PrivacyModel privacyModel : privacyModels) {
            for (int testRun = 0; testRun < testRuns; testRun++) {
                for (BenchmarkDataset dataset : datasets) {
                    for (AnonymizationAlgorithm algorithm : algorithms) {

                        TestConfiguration testConfig = new TestConfiguration();

                        // Meta info(s)
                        testConfig.testRunNumber = testRun;

                        // Experiment settings
                        testConfig.algorithm = algorithm;
                        testConfig.timeLimit = 100000;
                        testConfig.dataset = dataset;
                        testConfig.privacyModel = privacyModel;
                        testConfig.supression = 1d;
                        testConfig.useLocalTransformation = false;
                        testConfig.weightedQids = false;

                        // GA parameters
                        testConfig.eliteFraction = 0.2;
                        testConfig.crossoverFraction = 0.4;
                        testConfig.productionFraction = 0.2;
                        testConfig.mutationProbability = 0.05;
                        testConfig.immigrationFraction = 0.2;
                        testConfig.immigrationInterval = 10;
                        testConfig.subpopulationSize = 50;

                        // GA variant
                        testConfig.useTriangle = true;
                        testConfig.dualPopulation = true;

                        // dont log first run (warm up)
                        if (testRun == 0) {
                            testConfig.writeToFile = false;
                            testConfig.timeLimit = 10000;
                        }

                        // add finished config
                        testConfigs.add(testConfig);
                    }
                }
            }
        }
    }

}