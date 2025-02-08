package poo;

import java.util.ArrayList;

import poo.model.Computer;
import poo.model.ComputerDevice;
import poo.model.FlashMemory;
import poo.model.HardDiskDrive;
import poo.model.Keyboard;
import poo.model.Language;
import poo.model.Monitor;
import poo.model.Mouse;
import poo.model.SolidStateDrive;
import poo.model.TypeComputer;

public class App {

    private static ArrayList<Computer> computers; // Variables globales

    public static void main(String[] args) {
        computers = new ArrayList<>();
        Computer c1 = new Computer();
        c1.setType(TypeComputer.DESKTOP);
        c1.setId("1221");
        c1.getDevices().add(new Keyboard("Logitech MK345", true, Language.SPANISH, false, false));
        c1.getDevices().add(new Mouse("Genius DX-110", false, 1000, 2));
        c1.getDevices().add(new HardDiskDrive("Western Digital WD10EZEX", false, 300, 700, 6, 7200, 3.5));
        computers.add(c1);

        Computer c2 = new Computer();
        c2.setType(TypeComputer.LAPTOP);
        c2.setId("1222");
        c2.getDevices().add(new Monitor("Pantalla FHD, IPS", false, 15.6));
        c2.getDevices().add(new Keyboard("HP Premium retroiluminado", false, Language.SPANISH, false, true));
        c2.getDevices().add(new SolidStateDrive("Crucial CT1000BX500SSD1", false, 200, 800, 6, "SATA III"));
        c2.getDevices().add(new FlashMemory("ataTraveler G3 DT100G3", false, 10, 20, 10, "USB-A"));
        computers.add(c2);

        Computer c3 = new Computer();
        c3.setType(TypeComputer.LAPTOP);
        c3.setId("1223");
        c3.getDevices().add(new Monitor("Viewsonic VX2776-SMH", false, 27));
        c3.getDevices().add(new Keyboard("Logitech MK330", true, Language.SPANISH, false, true));
        c3.getDevices().add(new SolidStateDrive("Samsung 970 Evo Plus", false, 100, 250, 6, "PCIe Gen 3.0 x4, NVMe 1.3"));
        c3.getDevices().add(new FlashMemory("SanDisk Ultra Dual Drive ", false, 7, 16, 10, "USB-C"));
        computers.add(c3);

        Computer c4 = new Computer();
        c4.setType(TypeComputer.DESKTOP);
        c4.setId("1224");
        c4.getDevices().add(new Monitor("Dell S3422DW	", false, 34));
        c4.getDevices().add(new Keyboard("Logitech G915 Lightspeed", false, Language.ENGLISH, true, false));
        c4.getDevices().add(new SolidStateDrive("Samsung 970 EVO Plus", true, 150, 500, 3, "SATA 6 GB/s"));
        c4.getDevices().add(new FlashMemory("PNY Duo Link OTG", false, 10, 32, 130, "USB-A"));
        computers.add(c4);

        Computer c5 = new Computer();
        c5.setType(TypeComputer.LAPTOP);
        c5.setId("1225");
        c5.getDevices().add(new Monitor("LG UltraFine 27UN83A-W", false, 27));
        c5.getDevices().add(new Keyboard("MSI Vigor GK50 Elite", false, Language.SPANISH, true, false));
        c5.getDevices().add(new SolidStateDrive("Samsung 980 PRO", false, 150, 500, 7, "PCIe 4.0"));
        c5.getDevices().add(new FlashMemory("Toshiba TransMemory U365", false, 10, 32, 5, "USB-A"));
        computers.add(c5);

        Computer c6 = new Computer();
        c6.setType(TypeComputer.LAPTOP);
        c6.setId("1226");
        c6.getDevices().add(new Monitor("Samsung S32BM702 ", true, 32));
        c6.getDevices().add(new Keyboard("Asus ROG Strix Scope RX", false, Language.ENGLISH, false, false));
        c6.getDevices().add(new SolidStateDrive("Samsung 980 Pro", false, 250, 800, 6, "PCIe 4.0 x4"));
        c6.getDevices().add(new FlashMemory("Toshiba TransMemory U365 ", false, 5, 10, 6, "USB-C"));
        computers.add(c6);

        Computer c7 = new Computer();
        c7.setType(TypeComputer.DESKTOP);
        c7.setId("1227");
        c7.getDevices().add(new Monitor("widescreen ultra extended graphics array", false, 16));
        c7.getDevices().add(new Keyboard("Asus", true, Language.SPANISH, true, false));
        c7.getDevices().add(new SolidStateDrive("Crucial CT1000BX500SSD1", false, 200, 800, 6, "SATA III"));
        c7.getDevices().add(new FlashMemory("ataTraveler G3 DT100G3", false, 10, 20, 10, "USB-A"));
        c7.getDevices().add(new HardDiskDrive("st500lm012", false, 0, 500, 10, 0, 6));
        computers.add(c7);

        Computer c8 = new Computer();
        c8.setType(TypeComputer.LAPTOP);
        c8.setId("1228");
        c8.getDevices().add(new Monitor("Alta definición (HD)", false, 18));
        c8.getDevices().add(new Monitor("2K", false, 14));
        c8.getDevices().add(new Keyboard("HP Premium retroiluminado", true, Language.SPANISH, false, true));
        c8.getDevices().add(new SolidStateDrive("Crucial CT1000BX500SSD1", false, 200, 800, 6, "SATA III"));
        computers.add(c8);

        Computer c9 = new Computer();
        c9.setType(TypeComputer.DESKTOP);
        c9.setId("1229");
        c9.getDevices().add(new Keyboard("Green Leaf", false, Language.SPANISH, false, true));
        c9.getDevices().add(new HardDiskDrive("PATA", false, 200, 800, 6, 7, 7200.0));
        c9.getDevices().add(new FlashMemory("ataTraveler G3 DT100G3", false, 10, 20, 10, "USB-A"));
        computers.add(c9);

        Computer c10 = new Computer();
        c10.setType(TypeComputer.LAPTOP);
        c10.setId("1230");
        c10.getDevices().add(new Monitor("ViewSonic VA2715-H", false, 15.6));
        c10.getDevices().add(new Keyboard("Logitech", false, Language.FRENCH, false, true));
        c10.getDevices().add(new SolidStateDrive("Kingston A400", false, 200, 800, 6, "SATA III"));
        c10.getDevices().add(new FlashMemory("ataTraveler G3 DT100G3", false, 10, 20, 10, "USB-A"));
        computers.add(c10);

        Computer c11 = new Computer();
        c11.setType(TypeComputer.DESKTOP);
        c11.setId("1231");
        c11.getDevices().add(new Keyboard("Macally", false, Language.ENGLISH, true, true));
        c11.getDevices().add(new HardDiskDrive("SATA", false, 200, 800, 6, 7, 7200.0));
        c11.getDevices().add(new FlashMemory("ataTraveler G3 DT100G3", false, 10, 20, 10, "USB-A"));
        computers.add(c11);

        Computer c12 = new Computer();
        c12.setType(TypeComputer.LAPTOP);
        c12.setId("1232");
        c12.getDevices().add(new Monitor("ASUS VZ279HE-W", false, 15.6));
        c12.getDevices().add(new Keyboard("Eagle Warrior", false, Language.FRENCH, false, true));
        c12.getDevices().add(new SolidStateDrive("ADATA SU800", false, 200, 800, 6, "SATA III"));
        computers.add(c12);

        Computer c13 = new Computer();
        c13.setType(TypeComputer.DESKTOP);
        c13.setId("1233");
        c13.getDevices().add(new Monitor("Samsung FHD, IPS LF22T350", false, 22));
        c13.getDevices().add(new Keyboard("Corsair Rapidfire K70", false, Language.ENGLISH, true, false));
        c13.getDevices().add(new Mouse("Logitech Pebble M350", true, 1000, 2));
        c13.getDevices().add(new SolidStateDrive("Western Digital Green", false, 200, 400, 6, "SATA III"));
        computers.add(c13);

        Computer c14 = new Computer();
        c14.setType(TypeComputer.DESKTOP);
        c14.setId("1234");
        c14.getDevices().add(new Monitor("HP M24 FHD", false, 23.8));
        c14.getDevices().add(new Keyboard("Logitech K400", true, Language.ENGLISH, false, false));
        c14.getDevices().add(new Mouse("NGS FOG", true, 1000, 2));
        c14.getDevices().add(new HardDiskDrive("Seagate BarraCuda", false, 400, 600, 6, 7200, 2.5));
        computers.add(c14);

        Computer c15 = new Computer();
        c15.setType(TypeComputer.LAPTOP);
        c15.setId("1235");
        c15.getDevices().add(new Monitor("LG 32GN650", false, 31.5));
        c15.getDevices().add(new Keyboard("HP NOTEBOOK", false, Language.SPANISH, false, true));
        c15.getDevices().add(new HardDiskDrive("WD Blue", false, 200, 300, 6, 5400, 3.5));
        c15.getDevices().add(new FlashMemory(" Siemens 6AV2181-8AS20", false, 4, 4, 25, "USB-A"));
        computers.add(c15);

        Computer c16 = new Computer();
        c16.setType(TypeComputer.DESKTOP);
        c16.setId("1236");
        c16.getDevices().add(new Monitor("Dell S2421NX", false, 24));
        c16.getDevices().add(new Keyboard("Razer Huntsman V2", false, Language.SPANISH, false, false));
        c16.getDevices().add(new Mouse("Trust Verto", true, 1600, 6));
        c16.getDevices().add(new SolidStateDrive("Samsung 870 QVO", false, 200, 800, 6, "SATA III"));
        computers.add(c16);

        Computer c17 = new Computer();
        c17.setType(TypeComputer.LAPTOP);
        c17.setId("1237");
        c17.getDevices().add(new Monitor("ASUS VZ279HE-W", false, 27));
        c17.getDevices().add(new Keyboard("MSI Vigor GK50 ", false, Language.SPANISH, false, true));
        c17.getDevices().add(new SolidStateDrive("Crucial MX500", false, 300, 700, 6, "SATA III"));
        computers.add(c17);

        Language l = Language.ENGLISH;
        System.out.println("Value: " + l.getValue());
        System.out.println("getEnum: " + Language.getEnum("Idioma fRaNcéS"));
        System.out.println("getAll:");
        System.out.println(Language.getAll().toString(2));

        //Control kc para comentar   //Control ku para descomentar
        int count = 0;          
         for (Computer x : computers) {
             System.out.println(x);
             count++;
         }
         System.out.println(count);
        filterComputers();
        System.out.println(monitorAmount());

    }

    // Impresión de los computadores que aplican con las restricciones
    public static ArrayList<Computer> filterComputers() {
        ArrayList<Computer> listRestrictions = new ArrayList<>();
        int count = 0;
        for (Computer x : computers) {
            if (x.getRestrictions()) {
                System.out.println(x);
                listRestrictions.add(x);
                count++;
            }
        }

        System.out.println(count);
        return listRestrictions;

    }

    // Impresión del ArrayList de monitores
    public static ArrayList<ComputerDevice> monitorAmount() {
        ArrayList<ComputerDevice> listMonitors = new ArrayList<>();
        int count = 0;
        for (Computer computer : computers) {
            for (ComputerDevice computerMonitorList : computer.monitorList()) {
                listMonitors.add(computerMonitorList);
                count++;
            }
        }
        System.out.println("-".repeat(50));
        System.out.println("Cantidad de monitores: " + count);
        return listMonitors;
    }

}
