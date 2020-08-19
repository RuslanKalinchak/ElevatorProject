public class Passenger {
    private int floorNumberRoute;
    private int floorNumberLocation;
    private Route route;


    public Passenger(int floorNumberRoute, int floorNumberLocation)  {
        this.floorNumberRoute = floorNumberRoute;
        this.floorNumberLocation = floorNumberLocation;
        if (floorNumberLocation<floorNumberRoute){
            route = Route.UP;
        } else route = Route.DOWN;

    }

    public int getFloorNumberRoute() {
        return floorNumberRoute;
    }

    public void setFloorNumberRoute(int floorNumberRoute) {
        this.floorNumberRoute = floorNumberRoute;
    }

    public void setFloorNumberLocation(int floorNumberLocation) {
        this.floorNumberLocation = floorNumberLocation;
    }


    public int getFloorNumberLocation() {
        return floorNumberLocation;
    }

    public Route getRoute() {
        return route;
    }
}
