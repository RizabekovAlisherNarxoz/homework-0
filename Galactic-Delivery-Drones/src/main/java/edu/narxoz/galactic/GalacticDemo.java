package edu.narxoz.galactic;

import edu.narxoz.galactic.bodies.Planet;
import edu.narxoz.galactic.bodies.SpaceStation;
import edu.narxoz.galactic.cargo.Cargo;
import edu.narxoz.galactic.dispatcher.Dispatcher;
import edu.narxoz.galactic.dispatcher.Result;
import edu.narxoz.galactic.drones.HeavyDrone;
import edu.narxoz.galactic.drones.LightDrone;
import edu.narxoz.galactic.task.DeliveryTask;

public class GalacticDemo {

    public static void main(String[] args) {
        Planet mars = new Planet("Mars", 0.0, 0.0, "CO2");
        SpaceStation alpha = new SpaceStation("Alpha Station", 300.0, 400.0, 3);

        Cargo heavyCargo = new Cargo(80.0, "Mining equipment");
        LightDrone light = new LightDrone("LD-001", 50.0);
        HeavyDrone heavy = new HeavyDrone("HD-001", 100.0);

        Dispatcher dispatcher = new Dispatcher();

        System.out.println("=== Galactic Drone Delivery — Demo ===\n");

        DeliveryTask task = new DeliveryTask(mars, alpha, heavyCargo);
        Result r1 = dispatcher.assignTask(task, light);
        System.out.println("1) Assign 80 kg cargo to LightDrone (max 50 kg):");
        System.out.println("   Success : " + r1.ok());
        System.out.println("   Reason  : " + r1.reason());
        System.out.println("   Task state      : " + task.getState());
        System.out.println("   Drone status    : " + light.getStatus());
        System.out.println();

        Result r2 = dispatcher.assignTask(task, heavy);
        System.out.println("2) Assign 80 kg cargo to HeavyDrone (max 100 kg):");
        System.out.println("   Success : " + r2.ok());
        System.out.println("   Task state      : " + task.getState());
        System.out.println("   Assigned drone  : " + task.getAssignedDrone().getId());
        System.out.println("   Drone status    : " + heavy.getStatus());
        System.out.println();

        double time = task.estimateTime();
        System.out.println("3) Estimated delivery time:");
        System.out.println("   Distance  : 500.0 km  (Mars → Alpha Station)");
        System.out.println("   Speed     : " + heavy.speedKmPerMin() + " km/min  (HeavyDrone)");
        System.out.printf("   Est. time : %.1f min%n", time);
        System.out.println();

        Result r3 = dispatcher.completeTask(task);
        System.out.println("4) Complete delivery:");
        System.out.println("   Success      : " + r3.ok());
        System.out.println("   Task state   : " + task.getState());
        System.out.println("   Drone status : " + heavy.getStatus());
    }
}
