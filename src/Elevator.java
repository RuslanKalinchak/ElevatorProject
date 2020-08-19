import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Elevator {
    private House house = new House();
    private final int maxCapacityElevator = 5;
    List <Passenger>  totalElevatorContainer = new ArrayList<>();

    ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> afterFirstFillingOrderingPeopleMap =  new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> totalOrderingPeopleMap =  getAfterFirstFillingOrderingPeopleMap();

    public ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> getTotalOrderingPeopleMap() {
        return totalOrderingPeopleMap;
    }

    public ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> getAfterFirstFillingOrderingPeopleMap() {
        return afterFirstFillingOrderingPeopleMap;
    }

    public List<Passenger> firstFillingElevator(ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> orderingPeopleMap){
        totalElevatorContainer.clear();
        ConcurrentLinkedQueue<Passenger> passengers = orderingPeopleMap.get(1);
        int maxSizeElevator;
        if (passengers.size()>=5){
            maxSizeElevator = 5;
        } else {
            maxSizeElevator = passengers.size();
        }

            while (maxSizeElevator>0){
            totalElevatorContainer.add(passengers.peek());
            passengers.poll();
            maxSizeElevator--;
        }
        orderingPeopleMap.put(1, passengers);
        afterFirstFillingOrderingPeopleMap = orderingPeopleMap;
        return totalElevatorContainer;
    }

    public void exitFromTheElevator(int floorNumberLocation, List <Passenger>  elevatorContainer, ConcurrentHashMap <Integer, ConcurrentLinkedQueue<Passenger>> orderingPeopleMap){
        totalOrderingPeopleMap = orderingPeopleMap;
        int spaceCount = elevatorContainer.size();
        ConcurrentLinkedQueue<Passenger> passengerQueue = totalOrderingPeopleMap.get(floorNumberLocation);
        if (passengerQueue==null){
            passengerQueue = new ConcurrentLinkedQueue<>();
        }

       // System.out.println("Количество пасажиров на этаже до прибытия лифта: "+ passengerQueue.size());
        if (passengerQueue==null){
            passengerQueue = new ConcurrentLinkedQueue<>();
        }
      //  System.out.println("количество пасссажиров лифта до вихода: "+ elevatorContainer.size() );
         Iterator <Passenger> iterator = elevatorContainer.iterator();
         int  exitPeopleCount = 0;
         while (iterator.hasNext()){
             if (iterator.next().getFloorNumberRoute()==floorNumberLocation){
                 iterator.remove();
         //        System.out.println("Вышел  1 пасажир");
                 exitPeopleCount++;
                 spaceCount--;
             }
         }
        for (int i = 0; i < exitPeopleCount ; i++) {
            Passenger newPassenger = new Passenger(house.generateFloorRoute(floorNumberLocation), floorNumberLocation);
            int floorRoute = newPassenger.getFloorNumberRoute();
      //      System.out.println("Новопришедший на этаж человек хочет попасть на "+floorRoute+" этаж");
            passengerQueue.add(newPassenger);
        }

         if (passengerQueue==null){
            passengerQueue = new ConcurrentLinkedQueue<>();
        }


      totalOrderingPeopleMap.remove(floorNumberLocation);
      totalOrderingPeopleMap.put(floorNumberLocation, passengerQueue);
     // System.out.println("Количество людей на этаже после выхода пассажиров из лифта: "+ totalOrderingPeopleMap.get(floorNumberLocation).size());
      totalElevatorContainer = elevatorContainer;
     //   System.out.println("В ЛИФТЕ ОСТАЛОСЬ "+ totalElevatorContainer.size() +" ЧЕЛОВЕК");
    }

    public void entranceToTheElevator(int floorNumberLocation, Route route, List <Passenger>  elevatorContainer,  ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> orderingPeopleMap){
        ConcurrentLinkedQueue<Passenger> passengerQueue = orderingPeopleMap.get(floorNumberLocation);
       // System.out.println("Количество пасажиров на этаже ДО МОМЕНТА ВХОДА в лифт: " + passengerQueue.size());
        ConcurrentLinkedQueue<Passenger> passengersTrueRoute = new ConcurrentLinkedQueue();
        int freeSpaceCount = 5-elevatorContainer.size();

            if (route==Route.UP){
                for (Passenger passenger: passengerQueue) {
            if (passenger.getFloorNumberRoute()>floorNumberLocation){
                passengersTrueRoute.offer(passenger);
            }}} else if (route==Route.DOWN){
                for (Passenger passenger: passengerQueue) {
                if (passenger.getFloorNumberRoute()<floorNumberLocation){
                    passengersTrueRoute.offer(passenger);
                }}
            }

     //   System.out.println("Количество людей на этаже КОМУ НУЖНО ЕХАТЬ В НАПРАВЛЕНИИ лифта: " + passengersTrueRoute.size());
     //    System.out.println("Количество свободных мест в лифте ДО ПОСАДКИ НОВЫХ ПАСАЖИРОВ: " + freeSpaceCount);
       if (passengersTrueRoute.size()<freeSpaceCount){
           freeSpaceCount = passengersTrueRoute.size();
       }
      //  System.out.println("КОРРЕКЦИЯ свободных мест в лифте ДО ПОСАДКИ НОВЫХ ПАСАЖИРОВ: " + freeSpaceCount);
       if (freeSpaceCount>0){

            for (int i = freeSpaceCount; i > 0 ; i--) {
               elevatorContainer.add(passengersTrueRoute.peek());
               passengerQueue.remove(passengersTrueRoute.peek());
               passengersTrueRoute.poll();
           }
    }

       totalOrderingPeopleMap.put(floorNumberLocation, passengerQueue);
       totalElevatorContainer = elevatorContainer;
    //   System.out.println("КОЛИЧЕСТВО ПАССАЖИРОВ ЛИФТЕ ПОСЛЕ ПОСАДКИ НОВЫХ ПАСАЖИРОВ: " + totalElevatorContainer.size());
    //   System.out.println("КОЛИЧЕСТВО ЛЮДЕЙ НА ЭТАЖЕ ПОСЛЕ ПОСАДКИ В ЛИФТ: "+totalOrderingPeopleMap.size());
    }

    public int getMaxFloorNumber(List <Passenger>  elevatorContainer){
        int maxFloor = 0;
        for (int i = 0; i < elevatorContainer.size(); i++) {
            if(elevatorContainer.get(i).getFloorNumberRoute()>maxFloor){
                maxFloor = elevatorContainer.get(i).getFloorNumberRoute();
            }
        }
        return maxFloor;
    }

    public int getMinFloorNumber(List <Passenger>  elevatorContainer){
        int minFloor = 0;
        minFloor = house.n;
        for (int i = 0; i < elevatorContainer.size(); i++) {
            int a = elevatorContainer.get(i).getFloorNumberRoute();
            if(elevatorContainer.get(i).getFloorNumberRoute()<minFloor){
                minFloor = elevatorContainer.get(i).getFloorNumberRoute();
            }
        }
        return minFloor;
    }

   public void fillingEmptySpaces(int currentFloor,  Route route, List <Passenger>  elevatorContainer, ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> orderingPeopleMap){

       ConcurrentLinkedQueue<Passenger> queuePassengersOnTheCurrentFloor = orderingPeopleMap.get(currentFloor);
       // System.out.println("Количество пасажиров на этаже ДО МОМЕНТА ВХОДА в лифт: " + queuePassengersOnTheCurrentFloor.size());
       ConcurrentLinkedQueue<Passenger> queuePassengerTrueRoute = new ConcurrentLinkedQueue();
       if (route==Route.UP){
           for (Passenger passenger: queuePassengersOnTheCurrentFloor) {
               if (passenger.getFloorNumberRoute()>currentFloor){
                   queuePassengerTrueRoute.offer(passenger);
               }}} else if (route==Route.DOWN){
           for (Passenger passenger: queuePassengersOnTheCurrentFloor) {
               if (passenger.getFloorNumberRoute()<currentFloor){
                   queuePassengerTrueRoute.offer(passenger);
               }}
       }
   //    System.out.println("Количество людей на этаже КОМУ НУЖНО ЕХАТЬ В НАПРАВЛЕНИИ лифта: " + queuePassengerTrueRoute.size());
      int  freeSpaceCount = 5-elevatorContainer.size();
    //   System.out.println("Количество свободных мест в лифте ДО ПОСАДКИ НОВЫХ ПАСАЖИРОВ: " + freeSpaceCount);
      if(freeSpaceCount>queuePassengerTrueRoute.size()){
          freeSpaceCount = queuePassengerTrueRoute.size();
      }
   //    System.out.println("КОРРЕКЦИЯ свободных мест в лифте ДО ПОСАДКИ НОВЫХ ПАСАЖИРОВ: " + freeSpaceCount);
      if (elevatorContainer.size()<5){
           for (int i = freeSpaceCount; i > 0 ; i--) {
               elevatorContainer.add(queuePassengerTrueRoute.peek());
               queuePassengersOnTheCurrentFloor.remove(queuePassengerTrueRoute.peek());
               queuePassengerTrueRoute.poll();
           }
       }

       totalOrderingPeopleMap.remove(currentFloor);
       totalOrderingPeopleMap.put(currentFloor, queuePassengersOnTheCurrentFloor);
       totalElevatorContainer = elevatorContainer;
      // System.out.println("КОЛИЧЕСТВО ПАССАЖИРОВ ЛИФТЕ ПОСЛЕ ПОСАДКИ НОВЫХ ПАСАЖИРОВ: " + totalElevatorContainer.size());
     //  System.out.println("КОЛИЧЕСТВО ЛЮДЕЙ НА ЭТАЖЕ ПОСЛЕ ПОСАДКИ В ЛИФТ: "+totalOrderingPeopleMap.get(currentFloor).size());
    }

    public void fillingEmptyElevator (int currentFloor, List <Passenger>  elevatorContainer, ConcurrentLinkedQueue<Passenger> queuePassengersOnTheCurrentFloor, ConcurrentLinkedQueue<Passenger> queuePassengersTrueRoute){
        int freeSpaceCount=5;

       if (queuePassengersTrueRoute.size()<5){
           freeSpaceCount=queuePassengersTrueRoute.size();
       }
        for (int i = freeSpaceCount; i > 0; i--) {
            elevatorContainer.add(queuePassengersTrueRoute.peek());
            queuePassengersTrueRoute.poll();
        }
        totalOrderingPeopleMap.put(currentFloor, queuePassengersOnTheCurrentFloor);
        totalElevatorContainer = elevatorContainer;
    }

    public void up(int startLevel, List <Passenger>  elevatorContainer, ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> orderingPeopleMap){

       totalOrderingPeopleMap = orderingPeopleMap;

       totalElevatorContainer = elevatorContainer;
        int endRoute = getMaxFloorNumber(totalElevatorContainer);


        for (int i = startLevel; i <= endRoute; i++) {
            try {

                for (int j = 0; j < totalElevatorContainer.size(); j++) {
                    System.out.println("Passenger №"+(j+1)+" goes to the " +totalElevatorContainer.get(j).getFloorNumberRoute() + " floor");
                }

                System.out.println("===============================ELEVATOR GOING TO THE " + i + " FLOOR===================================");

                Thread.sleep(1000);
                int elevatorSizeBefore = totalElevatorContainer.size();
                exitFromTheElevator(i, totalElevatorContainer, totalOrderingPeopleMap);

                System.out.println("Elevator has stopped on the " + i + " floor");
                System.out.println(elevatorSizeBefore-totalElevatorContainer.size()+" people got off the elevator");

                System.out.println(" ");

                //  System.out.println("****************************************************************************************");

                Thread.sleep(500);

                 if (totalElevatorContainer.size()==0&&getTotalOrderingPeopleMap().get(i).size()>0){
                     ConcurrentLinkedQueue<Passenger> queuePassengersOnTheCurrentFloor = totalOrderingPeopleMap.get(i);
                     ConcurrentLinkedQueue<Passenger> queuePassengersToUp = new ConcurrentLinkedQueue<>();
                     ConcurrentLinkedQueue<Passenger> queuePassengersToDown = new ConcurrentLinkedQueue<>();
                     for (Passenger passenger: queuePassengersOnTheCurrentFloor) {
                         if (passenger.getRoute()==Route.UP){
                             queuePassengersToUp.offer(passenger);
                         } else if (passenger.getRoute()==Route.DOWN){
                             queuePassengersToDown.offer(passenger);
                         }
                     }
                     if (queuePassengersToUp.size()>=queuePassengersToDown.size()){
                         int countPeopleOnTheFloorBefore = totalOrderingPeopleMap.get(i).size();
                         fillingEmptyElevator(i,totalElevatorContainer, queuePassengersOnTheCurrentFloor, queuePassengersToUp);
                         endRoute = getMaxFloorNumber(totalElevatorContainer);
                         System.out.println(countPeopleOnTheFloorBefore-orderingPeopleMap.get(i).size()+" people entered the elevator");
                         System.out.println("The elevator goes to UP");
                         startLevel = i+1;
                         if (startLevel>house.n){
                             startLevel=house.n;
                         }
                         up(startLevel, totalElevatorContainer, totalOrderingPeopleMap); // подумати

                     } else if (queuePassengersToUp.size()<queuePassengersToDown.size()){
                         int countPeopleOnTheFloorBefore = totalOrderingPeopleMap.get(i).size();
                         fillingEmptyElevator(i,totalElevatorContainer, queuePassengersOnTheCurrentFloor, queuePassengersToDown);
                         System.out.println(countPeopleOnTheFloorBefore-getTotalOrderingPeopleMap().get(i).size()+" people entered the elevator");
                         System.out.println("The elevator changes direction!!! Now the elevator is moving DOWN");
                         int startLevelFloor = i-1;
                         if (i<=1){
                             startLevelFloor = 1;
                         }
                         down(startLevelFloor, totalElevatorContainer, totalOrderingPeopleMap);
                     }
                 } else if (totalElevatorContainer.size()==0&&getTotalOrderingPeopleMap().get(i).size()==0){
                     totalElevatorContainer = firstFillingElevator(totalOrderingPeopleMap);
                     System.out.println("Elevator is EMPTY. Restart process");
                     up(2, totalElevatorContainer, getAfterFirstFillingOrderingPeopleMap());
                 } else {

                 int countPeopleOnTheFloorBefore = totalOrderingPeopleMap.get(i).size();
                 fillingEmptySpaces(i, Route.UP, totalElevatorContainer, totalOrderingPeopleMap);
                 entranceToTheElevator(i, Route.UP, totalElevatorContainer, totalOrderingPeopleMap);

                 endRoute = getMaxFloorNumber(totalElevatorContainer);

                System.out.println(countPeopleOnTheFloorBefore-totalOrderingPeopleMap.get(i).size()+" people entered the elevator");
                System.out.println("The elevator goes to UP");

            }} catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void down(int startLevel, List <Passenger>  elevatorContainer, ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> orderingPeopleMap){

       totalElevatorContainer = elevatorContainer;
        totalOrderingPeopleMap = orderingPeopleMap;


        int endRoute = getMinFloorNumber(totalElevatorContainer);

        for (int i = startLevel; i >= endRoute; i--) {
            try {

                for (int j = 0; j < totalElevatorContainer.size(); j++) {
                    System.out.println("Passenger №"+(j+1)+" goes to the " +totalElevatorContainer.get(j).getFloorNumberRoute() + " floor");
                }

                System.out.println("===============================ELEVATOR GOING TO THE " + i + " FLOOR===================================");

                Thread.sleep(1000);
                int elevatorSizeBefore = totalElevatorContainer.size();
                exitFromTheElevator(i, totalElevatorContainer, totalOrderingPeopleMap);

                System.out.println("Elevator has stopped on the " + i + " floor");
                System.out.println(elevatorSizeBefore-totalElevatorContainer.size()+" people got off the elevator");

                System.out.println(" ");

              //  System.out.println("****************************************************************************************");

                Thread.sleep(500);

                if (totalElevatorContainer.size()==0&&getTotalOrderingPeopleMap().get(i).size()>0){
                    ConcurrentLinkedQueue<Passenger> queuePassengersOnTheCurrentFloor = totalOrderingPeopleMap.get(i);
                    ConcurrentLinkedQueue<Passenger> queuePassengersToUp = new ConcurrentLinkedQueue<>();
                    ConcurrentLinkedQueue<Passenger> queuePassengersToDown = new ConcurrentLinkedQueue<>();
                    for (Passenger passenger: queuePassengersOnTheCurrentFloor) {
                        if (passenger.getRoute()==Route.UP){
                            queuePassengersToUp.offer(passenger);
                        } else if (passenger.getRoute()==Route.DOWN){
                            queuePassengersToDown.offer(passenger);
                        }
                    }
                    if (queuePassengersToUp.size()<=queuePassengersToDown.size()){
                        int countPeopleOnTheFloorBefore = totalOrderingPeopleMap.get(i).size();
                        fillingEmptyElevator(i, totalElevatorContainer, queuePassengersOnTheCurrentFloor, queuePassengersToDown);
                        endRoute = getMinFloorNumber(totalElevatorContainer);
                        System.out.println(countPeopleOnTheFloorBefore-totalOrderingPeopleMap.get(i).size()+" people entered the elevator");
                        System.out.println("The elevator goes to DOWN");

                    } else if (queuePassengersToUp.size()>queuePassengersToDown.size()){
                        int countPeopleOnTheFloorBefore = totalOrderingPeopleMap.get(i).size();
                        fillingEmptyElevator(i, totalElevatorContainer, queuePassengersOnTheCurrentFloor, queuePassengersToUp);
                        System.out.println(countPeopleOnTheFloorBefore-getTotalOrderingPeopleMap().get(i).size()+" people entered the elevator");
                        System.out.println("The elevator changes direction!!! Now the elevator is moving UP");
                        int startLevelFloor = i+1;
                        if (i>=house.n){
                            startLevelFloor = house.n;
                        }
                        up(startLevelFloor, totalElevatorContainer, totalOrderingPeopleMap);
                    }
                } else if (totalElevatorContainer.size()==0&&getTotalOrderingPeopleMap().get(i).size()==0){
                    totalElevatorContainer = firstFillingElevator(totalOrderingPeopleMap);
                    System.out.println("Elevator is EMPTY. Restart process");
                    up(2, totalElevatorContainer, getAfterFirstFillingOrderingPeopleMap());
                } else {

                int countPeopleOnTheFloorBefore = totalOrderingPeopleMap.get(i).size();
                fillingEmptySpaces(i, Route.DOWN, totalElevatorContainer, totalOrderingPeopleMap);
                entranceToTheElevator(i, Route.DOWN, totalElevatorContainer, totalOrderingPeopleMap);

                endRoute = getMinFloorNumber(totalElevatorContainer);

                System.out.println(countPeopleOnTheFloorBefore-totalOrderingPeopleMap.get(i).size()+" people entered the elevator");
                System.out.println("The elevator goes to DOWN");


            } }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
     public void elevatorStartedMove(List <Passenger>  elevatorContainer, ConcurrentHashMap<Integer, ConcurrentLinkedQueue<Passenger>> orderingPeopleMap){
        int startLevel = 2;
        up(startLevel, elevatorContainer, orderingPeopleMap);
     }
}
