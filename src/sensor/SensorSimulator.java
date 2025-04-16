package sensor;

import java.rmi.Naming;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SensorSimulator {
    private final String sensorId;
    private final Random random = new Random();

    public SensorSimulator(String sensorId) {
        this.sensorId = sensorId;
    }

    public void start(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(()->{
            try {
                // Generate random sensor data
                double temperature = 20 + (random.nextDouble() * 15); // 20-35°C
                double pressure = 0.8 + (random.nextDouble() * 0.4); // 0.8-1.2 atm

                // Send to all aggregators
                ISensor tempAggregator = (ISensor) Naming.lookup("rmi://localhost:12345/temperatureAggregator");
                ISensor pressAggregator = (ISensor) Naming.lookup("rmi://localhost:12345/pressureAggregator");

                try{
                    tempAggregator.onSensorData(sensorId, temperature, pressure);
                    pressAggregator.onSensorData(sensorId, temperature, pressure);
                }
                catch (Exception e){
                    System.err.println("Error sending to aggregator: ");
                    e.printStackTrace();
                }

                System.out.printf("Sensor %s: Temp=%.2f°C, Pres=%.2fatm%n",
                        sensorId, temperature, pressure);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    //java .\SensorSimulator.java Sensor1
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: SensorSimulator <sensorId>");
            System.err.println("run java SensorSimulator.java <sensorId>");
            System.exit(1);
        }

        SensorSimulator simulator = new SensorSimulator(args[0]);
        simulator.start();
    }
}
