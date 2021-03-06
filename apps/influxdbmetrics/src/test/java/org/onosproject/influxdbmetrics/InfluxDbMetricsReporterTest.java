/*
 * Copyright 2016 Open Networking Laboratory
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
package org.onosproject.influxdbmetrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.junit.Before;
import org.onlab.metrics.MetricsComponent;
import org.onlab.metrics.MetricsFeature;
import org.onlab.metrics.MetricsService;
import org.onosproject.cfg.ComponentConfigAdapter;
import org.onosproject.cluster.ClusterServiceAdapter;
import org.onosproject.core.CoreServiceAdapter;

import java.util.Map;

/**
 * Unit tests for influxDB metrics reporter.
 */
public class InfluxDbMetricsReporterTest {

    private DefaultInfluxDbMetricsReporter influxReporter;

    /**
     * Sets up the services required by influxDB metrics reporter.
     */
    @Before
    public void setUp() {
        influxReporter = new DefaultInfluxDbMetricsReporter();
        influxReporter.coreService = new CoreServiceAdapter();
        influxReporter.cfgService = new ComponentConfigAdapter();
        influxReporter.clusterService = new ClusterServiceAdapter();
        influxReporter.metricsService = new TestMetricsService();
        influxReporter.activate();
    }

    /**
     * Tears down influxDB metrics reporter.
     */
    public void tearDown() {
        influxReporter.deactivate();
    }

    private class TestMetricsService implements MetricsService {

        @Override
        public MetricsComponent registerComponent(String name) {
            return null;
        }

        @Override
        public MetricRegistry getMetricRegistry() {
            return null;
        }

        @Override
        public Counter createCounter(MetricsComponent component,
                                     MetricsFeature feature, String metricName) {
            return null;
        }

        @Override
        public Histogram createHistogram(MetricsComponent component,
                                         MetricsFeature feature, String metricName) {
            return null;
        }

        @Override
        public Timer createTimer(MetricsComponent component,
                                 MetricsFeature feature, String metricName) {
            return null;
        }

        @Override
        public Meter createMeter(MetricsComponent component,
                                 MetricsFeature feature, String metricName) {
            return null;
        }

        @Override
        public <T extends Metric> T registerMetric(MetricsComponent component,
                                                   MetricsFeature feature,
                                                   String metricName, T metric) {
            return null;
        }

        @Override
        public boolean removeMetric(MetricsComponent component,
                                    MetricsFeature feature, String metricName) {
            return false;
        }

        @Override
        public Map<String, Timer> getTimers(MetricFilter filter) {
            return null;
        }

        @Override
        public Map<String, Gauge> getGauges(MetricFilter filter) {
            return null;
        }

        @Override
        public Map<String, Counter> getCounters(MetricFilter filter) {
            return null;
        }

        @Override
        public Map<String, Meter> getMeters(MetricFilter filter) {
            return null;
        }

        @Override
        public Map<String, Histogram> getHistograms(MetricFilter filter) {
            return null;
        }

        @Override
        public Map<String, Metric> getMetrics() {
            return null;
        }

        @Override
        public void removeMatching(MetricFilter filter) {
        }
    }
}
