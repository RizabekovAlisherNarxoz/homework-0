package edu.narxoz.galactic.dispatcher;

import edu.narxoz.galactic.drones.Drone;
import edu.narxoz.galactic.drones.DroneStatus;
import edu.narxoz.galactic.task.DeliveryTask;
import edu.narxoz.galactic.task.TaskState;

public class Dispatcher {

    public Result assignTask(DeliveryTask task, Drone drone) {
        if (task == null) {
            return Result.failure("Task must not be null");
        }
        if (drone == null) {
            return Result.failure("Drone must not be null");
        }
        if (task.getState() != TaskState.CREATED) {
            return Result.failure("Task state is not CREATED (current: " + task.getState() + ")");
        }
        if (drone.getStatus() != DroneStatus.IDLE) {
            return Result.failure("Drone is not IDLE (current: " + drone.getStatus() + ")");
        }
        if (task.getCargo().getWeightKg() > drone.getMaxPayloadKg()) {
            return Result.failure("Cargo weight ("
                    + task.getCargo().getWeightKg()
                    + " kg) exceeds drone max payload ("
                    + drone.getMaxPayloadKg() + " kg)");
        }

        task.setState(TaskState.ASSIGNED);
        task.setAssignedDrone(drone);
        drone.setStatus(DroneStatus.IN_FLIGHT);

        return Result.success();
    }

    public Result completeTask(DeliveryTask task) {
        if (task == null) {
            return Result.failure("Task must not be null");
        }
        if (task.getState() != TaskState.ASSIGNED) {
            return Result.failure("Task state is not ASSIGNED (current: " + task.getState() + ")");
        }
        if (task.getAssignedDrone() == null) {
            return Result.failure("No drone is assigned to this task");
        }
        if (task.getAssignedDrone().getStatus() != DroneStatus.IN_FLIGHT) {
            return Result.failure("Drone is not IN_FLIGHT (current: "
                    + task.getAssignedDrone().getStatus() + ")");
        }

        task.setState(TaskState.DONE);
        task.getAssignedDrone().setStatus(DroneStatus.IDLE);

        return Result.success();
    }
}
