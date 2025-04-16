package aggregator;

import sensor.ISensor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class AbstractAggregator extends UnicastRemoteObject implements ISensor, IAggregator {
    protected final Map<String, Queue<Double>> temperatureReadings = new HashMap<>();
    protected final Map<String, Queue<Double>> pressureReadings = new HashMap<>();
    protected final int tempWindowSize;
    protected final int pressureWindowSize;

    protected AbstractAggregator(int tempWindowSize, int pressureWindowSize) throws RemoteException {
        this.tempWindowSize = tempWindowSize;
        this.pressureWindowSize = pressureWindowSize;
    }

    @Override
    public void onSensorData(String sensorId, double temperature, double pressure) throws RemoteException {
        // Initialize queues if they don't exist
        if (!temperatureReadings.containsKey(sensorId)) {
            temperatureReadings.put(sensorId, new LinkedList<>());
        }
        if (!pressureReadings.containsKey(sensorId)) {
            pressureReadings.put(sensorId, new LinkedList<>());
        }

        // Add temperature reading
        Queue<Double> tempQueue = temperatureReadings.get(sensorId);
        tempQueue.add(temperature);
        if (tempQueue.size() > tempWindowSize) {
            tempQueue.poll(); // Remove the oldest reading if window size exceeded
        }

        // Add pressure reading
        Queue<Double> pressureQueue = pressureReadings.get(sensorId);
        pressureQueue.add(pressure);
        if (pressureQueue.size() > pressureWindowSize) {
            pressureQueue.poll(); // Remove the oldest reading if window size exceeded
        }
    }

    @Override
    public synchronized Map<String, Double> getTemperatureAverages() throws RemoteException {
        Map<String, Double> averages = new HashMap<>();
        for (Map.Entry<String, Queue<Double>> entry : temperatureReadings.entrySet()) {
            String sensorId = entry.getKey();
            Queue<Double> readings = entry.getValue();
            averages.put(sensorId, calculateAverage(readings));
        }
        return averages;
    }

    @Override
    public synchronized Map<String, Double> getPressureAverages() throws RemoteException {
        Map<String, Double> averages = new HashMap<>();
        for (Map.Entry<String, Queue<Double>> entry : pressureReadings.entrySet()) {
            String sensorId = entry.getKey();
            Queue<Double> readings = entry.getValue();
            averages.put(sensorId, calculateAverage(readings));
        }
        return averages;
    }

    @Override
    public synchronized double getOverallTemperatureAverage() throws RemoteException {
        double sum = 0.0;
        int count = 0;

        for (Queue<Double> readings : temperatureReadings.values()) {
            for (Double reading : readings) {
                sum += reading;
                count++;
            }
        }

        return count > 0 ? sum / count : 0.0;
    }

    @Override
    public synchronized double getOverallPressureAverage() throws RemoteException {
        double sum = 0.0;
        int count = 0;

        for (Queue<Double> readings : pressureReadings.values()) {
            for (Double reading : readings) {
                sum += reading;
                count++;
            }
        }

        return count > 0 ? sum / count : 0.0;
    }

    private double calculateAverage(Queue<Double> readings) {
        if (readings.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Double reading : readings) {
            sum += reading;
        }
        return sum / readings.size();
    }
}
