package aggregator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PressureAggregator extends AbstractAggregator{
    protected PressureAggregator() throws RemoteException {
        super(0, 100);
    }

    public static void main(String[] args) {
        try {
            PressureAggregator pressureAggregator = new PressureAggregator();

            int portNum = 12345;
            // Start the registry
            startRegistry(portNum);

            //setup URL
            Naming.rebind("rmi://localhost:" + portNum + "/pressureAggregator", pressureAggregator);

        } catch (RemoteException ex) {
            System.out.println("Error occurred when making pressureAggregator object: " + ex.getMessage());
        }
        catch(MalformedURLException ex)
        {
            System.out.println("Error occurred when storing pressureAggregator at specified URL: " + ex.getMessage());
        }
    }

    // This method starts a RMI registry on the local host, if it
    // does not already exist at the specified port number.
    private static void startRegistry(int RMIPortNum) throws RemoteException
    {
        try {
            // Try to get the registry at a specific port number
            // If there is no registry started on that port, an exception will be thrown
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);

            registry.list();
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located at port " + RMIPortNum);

            // Create a registry on the given port number
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    } // end startRegistry
}
