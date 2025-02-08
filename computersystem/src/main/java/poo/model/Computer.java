package poo.model;

import java.util.ArrayList;

import org.json.JSONObject;

public class Computer {

    /* Objetivos
    * foreach para hacer la revisión de valores
    * 
    
     */
    // Atributos
    protected String id;
    private TypeComputer type;
    private ArrayList<ComputerDevice> devices;

    // Constructor por defecto
    public Computer() {
        this("Undefined", new ArrayList<>(), TypeComputer.DESKTOP);
    }

    // Constructor parametrizado
    public Computer(String id, ArrayList<ComputerDevice> devices, TypeComputer type) {
        setId(id);
        setDevices(devices);
        setType(type);
    }

    // Accesores y Mutadores
    public ArrayList<ComputerDevice> getDevices() {
        return devices;
    }

    public final void setDevices(ArrayList<ComputerDevice> devices) {
        this.devices = devices;
    }

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final void setType(TypeComputer type) {
        this.type = type;
    }
    public TypeComputer getType(){
        return type;
    }

    /**
     * Recorre la colección de Devices y totaliza la capacidad libre de los de
     * tipo StorageDevice
     *
     *      *return La suma de lo libre en cada dispositvo de almacenamiento del
     * computador.
     */
    public int getFullFreeCapacity() { // En total
        int fullFreeCapacity = 0;
        for (ComputerDevice device : devices) {
            if (device instanceof StorageDevice) {
                fullFreeCapacity += ((StorageDevice) device).getFreeCapacity();
            }
        }
        return fullFreeCapacity;
    }

    /**
     * Recorre la colección de Devices para totalizar la acapacidad libre de los
     * de tipo StorageDevice
     *
     * @return La suma de lo libre en cada dispositivo de almacenamiento del
     * computador
     */
    public int getFullUsedCapacity() { // En total
        int fullUsedCapacity = 0;
        for (ComputerDevice device : devices) {
            if (device instanceof StorageDevice) {
                fullUsedCapacity += ((StorageDevice) device).getUsedCapacity();
            }
        }
        return fullUsedCapacity;
    }

    /**
     * Recorre la colección de Devices para totalizar la capacidad total de los
     * de tipo StorageDevice
     *
     * @return La suma del total en cada dispositivo de almacenamiento del
     * computador.
     */
    public int getFullCapacity() {  // En total
        int fullCapacity = 0;
        for (ComputerDevice device : devices) {
            if(device instanceof StorageDevice) {
                fullCapacity += ((StorageDevice) device).getCapacity();
            }
        }
        return fullCapacity;
    }

    /**
     * Recorre la colección de Dvices para comprobar los filtros puestos Entre
     * esos esta: 1 instancia de FlashDrive y SolidStateDrive y 0 de
     * HardDiskDrive 1 instancia de monitor y 0 de mouse
     *
     * @return Un boolean para ver si la instancia de Computer cumple con los
     * filtros.
     */
    public boolean getRestrictions() {
        int amountSolidStateDrive = 0;
        int amountHardDiskDrive = 0;
        int amountFlashMemory = 0;
        int amountMonitor = 0;
        int amountMouse = 0;
        for (ComputerDevice device : devices) {
            if (device instanceof SolidStateDrive) {
                amountSolidStateDrive++;
            } else if (device instanceof FlashMemory) {
                amountFlashMemory++;
            } else if (device instanceof Monitor) {
                amountMonitor++;
            } else if (device instanceof Mouse) {
                amountMouse++;
            } else if (device instanceof HardDiskDrive) {
                amountHardDiskDrive++;
            }
        }
        if ((amountFlashMemory == 1 && amountSolidStateDrive == 1 && amountHardDiskDrive == 0) && (amountMonitor >= 1 && amountMouse == 0)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Recorre la colección de Devices para comprobar los filtros de la cantidad minima de elementos
     * de los StorageDevice, monitores, mouses, teclados.
     * @return Un boolean para ver si la instancia cumple con la cantidad minima para un computer.
     */
    public boolean amountRestrictions() {
        int amountSolidStateDrive = 0;
        int amountHardDiskDrive = 0;
        int amountFlashMemory = 0;
        int amountKeyboard = 0;
        int amountMonitor = 0;
        int amountMouse = 0;
        for (ComputerDevice device : devices) {
            if (device instanceof SolidStateDrive) {
                amountSolidStateDrive++;
            } else if (device instanceof FlashMemory) {
                amountFlashMemory++;
            } else if (device instanceof Monitor) {
                amountMonitor++;
            } else if (device instanceof Mouse) {
                amountMouse++;
            } else if (device instanceof HardDiskDrive) {
                amountHardDiskDrive++;
            } else if (device instanceof Keyboard) {
                amountKeyboard++;
            }
        }
        if ((amountFlashMemory + amountSolidStateDrive + amountHardDiskDrive > 4) || 
        (amountMouse > 1) || (amountKeyboard > 1) || (amountMonitor > 2)) {
            throw new IllegalArgumentException("No cumple con el requsito minimo");
        } else {
            return true;
        }

    }
    public ArrayList<ComputerDevice> monitorList(){
        ArrayList<ComputerDevice>monitorList = new ArrayList<>();
        for (ComputerDevice device : devices) {
            if(device instanceof Monitor){
                monitorList.add(device);
            }
        }
        return monitorList;
    }

    // Sobreescritura de toString()    
    @Override
    public String toString() {
        return (new JSONObject(this)).toString(2);
    }

}
