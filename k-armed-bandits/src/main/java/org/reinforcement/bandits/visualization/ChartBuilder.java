package org.reinforcement.bandits.visualization;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.reinforcement.bandits.simulation.ExperimentRunner;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for creating charts to visualize bandit simulation results.
 * Uses XChart library for clean, publication-quality plots.
 */
public class ChartBuilder {

    /**
     * Create a learning curve showing average reward over time.
     *
     * @param title chart title
     * @param stepData x-axis data (step numbers)
     * @param rewardData y-axis data (average rewards)
     * @param seriesName name for the data series
     * @return configured chart
     */
    public static XYChart createLearningCurve(String title, double[] stepData,
                                             double[] rewardData, String seriesName) {
        XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title(title)
            .xAxisTitle("Steps")
            .yAxisTitle("Average Reward")
            .build();

        configureStyle(chart);
        chart.addSeries(seriesName, stepData, rewardData)
            .setMarker(SeriesMarkers.NONE);

        return chart;
    }

    /**
     * Create a comparison chart with multiple algorithms.
     *
     * @param title chart title
     * @param stepData x-axis data (step numbers)
     * @param algorithmResults map of algorithm name to reward data
     * @return configured chart
     */
    public static XYChart createComparisonChart(String title, double[] stepData,
                                               java.util.Map<String, double[]> algorithmResults) {
        XYChart chart = new XYChartBuilder()
            .width(1000)
            .height(700)
            .title(title)
            .xAxisTitle("Steps")
            .yAxisTitle("Average Reward")
            .build();

        configureStyle(chart);

        for (java.util.Map.Entry<String, double[]> entry : algorithmResults.entrySet()) {
            chart.addSeries(entry.getKey(), stepData, entry.getValue())
                .setMarker(SeriesMarkers.NONE);
        }

        return chart;
    }

    /**
     * Create an optimal action percentage chart.
     *
     * @param title chart title
     * @param stepData x-axis data (step numbers)
     * @param optimalActionData y-axis data (percentage of optimal actions)
     * @param seriesName name for the data series
     * @return configured chart
     */
    public static XYChart createOptimalActionChart(String title, double[] stepData,
                                                  double[] optimalActionData, String seriesName) {
        XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title(title)
            .xAxisTitle("Steps")
            .yAxisTitle("% Optimal Action")
            .build();

        configureStyle(chart);

        // Convert to percentage (0-100)
        double[] percentageData = new double[optimalActionData.length];
        for (int i = 0; i < optimalActionData.length; i++) {
            percentageData[i] = optimalActionData[i] * 100.0;
        }

        chart.addSeries(seriesName, stepData, percentageData)
            .setMarker(SeriesMarkers.NONE);

        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax(100.0);

        return chart;
    }

    /**
     * Create a bar chart showing action selection distribution.
     *
     * @param title chart title
     * @param actionLabels labels for each action
     * @param selectionCounts number of times each action was selected
     * @return configured chart
     */
    public static CategoryChart createActionDistributionChart(String title,
                                                             String[] actionLabels,
                                                             int[] selectionCounts) {
        CategoryChart chart = new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title(title)
            .xAxisTitle("Action")
            .yAxisTitle("Selection Count")
            .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

        // Convert int[] to Integer[] for XChart
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < actionLabels.length; i++) {
            labels.add(actionLabels[i]);
            values.add(selectionCounts[i]);
        }

        chart.addSeries("Selections", labels, values);

        return chart;
    }

    /**
     * Create a parameter study chart (e.g., effect of epsilon on performance).
     *
     * @param title chart title
     * @param parameterValues x-axis values (parameter values tested)
     * @param performanceMetric y-axis values (performance metric)
     * @param parameterName name of the parameter
     * @param metricName name of the metric
     * @return configured chart
     */
    public static XYChart createParameterStudyChart(String title,
                                                   double[] parameterValues,
                                                   double[] performanceMetric,
                                                   String parameterName,
                                                   String metricName) {
        XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title(title)
            .xAxisTitle(parameterName)
            .yAxisTitle(metricName)
            .build();

        configureStyle(chart);
        chart.addSeries(metricName, parameterValues, performanceMetric)
            .setMarker(SeriesMarkers.CIRCLE);

        return chart;
    }

    /**
     * Display a chart in a Swing window.
     *
     * @param chart the chart to display
     */
    public static void displayChart(XYChart chart) {
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Display a category chart in a Swing window.
     *
     * @param chart the chart to display
     */
    public static void displayChart(CategoryChart chart) {
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Save a chart to a file.
     *
     * @param chart the chart to save
     * @param filePath path to save the file (without extension)
     * @param format image format (PNG, JPG, SVG, PDF)
     * @throws IOException if save fails
     */
    public static void saveChart(XYChart chart, String filePath,
                                BitmapEncoder.BitmapFormat format) throws IOException {
        BitmapEncoder.saveBitmap(chart, filePath, format);
    }

    /**
     * Save a category chart to a file.
     *
     * @param chart the chart to save
     * @param filePath path to save the file (without extension)
     * @param format image format (PNG, JPG, SVG, PDF)
     * @throws IOException if save fails
     */
    public static void saveChart(CategoryChart chart, String filePath,
                                BitmapEncoder.BitmapFormat format) throws IOException {
        BitmapEncoder.saveBitmap(chart, filePath, format);
    }

    /**
     * Generate step numbers for x-axis data.
     *
     * @param numSteps number of steps
     * @return array of step numbers [0, 1, 2, ..., numSteps-1]
     */
    public static double[] generateStepData(int numSteps) {
        double[] steps = new double[numSteps];
        for (int i = 0; i < numSteps; i++) {
            steps[i] = i;
        }
        return steps;
    }

    /**
     * Configure default styling for charts.
     */
    private static void configureStyle(XYChart chart) {
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setMarkerSize(0);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
    }

    /**
     * Create a comprehensive visualization from experiment results.
     *
     * @param results aggregated results from multiple trials
     * @param title base title for charts
     * @return array of charts [learning curve, optimal action %]
     */
    public static XYChart[] createExperimentVisualizations(
            ExperimentRunner.AggregatedResults results, String title) {

        double[] steps = generateStepData(results.getAvgReward().length);

        XYChart rewardChart = createLearningCurve(
            title + " - Learning Curve",
            steps,
            results.getAvgReward(),
            "Average Reward"
        );

        XYChart optimalChart = createOptimalActionChart(
            title + " - Optimal Action Selection",
            steps,
            results.getAvgOptimalActionRate(),
            "% Optimal"
        );

        return new XYChart[]{rewardChart, optimalChart};
    }
}