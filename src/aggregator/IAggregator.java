package aggregator;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IAggregator extends Remote {
    Map<String, Double> getTemperatureAverages() throws RemoteException;
    Map<String, Double> getPressureAverages() throws RemoteException;
    double getOverallTemperatureAverage() throws RemoteException;
    double getOverallPressureAverage() throws RemoteException;
}
