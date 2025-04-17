package client;

import aggregator.IAggregator;

import java.rmi.Naming;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientSimulator {

    public static void main(String[] args) {
        try {
            IAggregator tempAggregator = (IAggregator) Naming.lookup("rmi://localhost:12345/temperatureAggregator");
            IAggregator pressureAggregator = (IAggregator) Naming.lookup("rmi://localhost:12345/pressureAggregator");

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                try {
                    System.out.println("=== Aggregated Sensor Data ===");

                    Map<String, Double> tempAverages = tempAggregator.getTemperatureAverages();
                    double overallTemp = tempAggregator.getOverallTemperatureAverage();
                    System.out.println("Temperature Averages per Sensor:");
                    tempAverages.forEach((sensorId, avg) ->
                            System.out.printf("  Sensor %s: %.2f°C%n", sensorId, avg));
                    System.out.printf("Overall Temperature Average: %.2f°C%n", overallTemp);

                    Map<String, Double> pressureAverages = pressureAggregator.getPressureAverages();
                    double overallPressure = pressureAggregator.getOverallPressureAverage();
                    System.out.println("Pressure Averages per Sensor:");
                    pressureAverages.forEach((sensorId, avg) ->
                            System.out.printf("  Sensor %s: %.2f atm%n", sensorId, avg));
                    System.out.printf("Overall Pressure Average: %.2f atm%n", overallPressure);

                    System.out.println("================================\n");

                } catch (Exception e) {
                    System.err.println("Error retrieving data from aggregator: ");
                    e.printStackTrace();
                }
            }, 0, 5, TimeUnit.SECONDS);

        } catch (Exception e) {
            System.err.println("Client failed to connect to aggregators:");
            e.printStackTrace();
        }
    }
}
