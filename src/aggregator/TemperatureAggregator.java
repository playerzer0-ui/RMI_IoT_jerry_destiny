package aggregator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TemperatureAggregator extends AbstractAggregator {

    protected TemperatureAggregator() throws RemoteException {
        super(50, 0); // Only temperature matters here
    }

    public static void main(String[] args) {
        try {
            TemperatureAggregator temperatureAggregator = new TemperatureAggregator();
            int portNum = 12345;

            startRegistry(portNum);

            Naming.rebind("rmi://localhost:" + portNum + "/temperatureAggregator", temperatureAggregator);
            System.out.println("TemperatureAggregator bound in registry.");

        } catch (RemoteException ex) {
            System.err.println("Error creating TemperatureAggregator: " + ex.getMessage());
        } catch (MalformedURLException ex) {
            System.err.println("URL error: " + ex.getMessage());
        }
    }

    private static void startRegistry(int RMIPortNum) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list(); // Will throw exception if not running
        } catch (RemoteException ex) {
            System.out.println("RMI registry not found at port " + RMIPortNum + ", creating new one.");
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    }
}
