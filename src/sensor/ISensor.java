package sensor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISensor extends Remote {
    public void onSensorData(String sensorId, double temperature, double pressure) throws RemoteException;
}
