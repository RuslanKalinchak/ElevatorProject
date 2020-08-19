import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {


        House house = new House();
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> starterOrderingPeopleMap = house.firstCreateOrderingPeopleMap();
      /*  for (ConcurrentHashMap.Entry<Integer,ConcurrentLinkedQueue<Passenger>> entry: starterOrderingPeopleMap.entrySet()){
            System.out.println("Floor number is " + entry.getKey());
            ConcurrentLinkedQueue<Passenger> passengerQueue = entry.getValue();
            for (Passenger passenger: passengerQueue) {
                System.out.print(passenger+" *** ");
            }
            System.out.println("");
        }*/

        Elevator elevator = new Elevator();
        List<Passenger> starterElevatorContainer = elevator.firstFillingElevator(starterOrderingPeopleMap);

      /* for (int i = 0; i<starterElevatorContainer.size(); i++){
            System.out.println(starterElevatorContainer.get(i));
        }*/
      elevator.elevatorStartedMove(starterElevatorContainer, elevator.getAfterFirstFillingOrderingPeopleMap());

}}
