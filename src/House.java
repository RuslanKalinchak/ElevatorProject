import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class House {
    final int minN = 5;
    final int maxN = 20;
    public final int n = (int) (Math.random()*(maxN-minN+1))+minN;
    final int minK = 0;
    final int maxK = 10;
    final int minFloor = 1;
    final int maxFloor = n;

    private boolean canUp;
    private boolean canDown;

    private ConcurrentLinkedQueue<Passenger> passengers;
    private ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> someorderingPeopleMap;

    public ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> getOrderingPeopleMap() {
        return someorderingPeopleMap;
    }
    public int getN() {
        return n;
    }

    public int generateFloorRoute (int floorLocation){
        int floorNumberRoute = 0;
        do{
                floorNumberRoute = (int) (Math.random() * (maxFloor - minFloor + 1)) + minFloor;}

         while(floorNumberRoute==floorLocation);
        return floorNumberRoute;
    }
    public ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> firstCreateOrderingPeopleMap(){
       someorderingPeopleMap = new ConcurrentHashMap<>();
       do {
        for (int i = 1; i <= n; i++) {
            int floorNumber = i;
            ConcurrentLinkedQueue<Passenger> passengers = new ConcurrentLinkedQueue();
            for (int j = 0; j < ((int) (Math.random()*(maxK-minK+1))+minK); j++) {
                int floorNumberRoute = generateFloorRoute(floorNumber);
                passengers.offer(new Passenger(floorNumberRoute, i));
            }
           someorderingPeopleMap.put(i, passengers);
        }} while (someorderingPeopleMap.get(1).size()==0 );
        return someorderingPeopleMap;
    }
}
