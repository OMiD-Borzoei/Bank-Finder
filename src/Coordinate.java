public class Coordinate {
    double x, y;

    Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistance(Coordinate c) {
        return mySqrt((this.x - c.x) * (this.x - c.x) + (this.y - c.y) * (this.y - c.y));
    }

    public double getVerticalDistance(Coordinate c) {
        return this.y - c.y;
    }

    public double getHorizontalDistance(Coordinate c) {
        return this.x - c.x;
    }

    public boolean isEqual(Coordinate c) {
        return this.x == c.x && this.y == c.y;
    }

    public double mySqrt(double num){
        double sqrt=num/2,temp=0;
        while(sqrt!=temp){
            temp=sqrt;
            sqrt=(num/temp + temp)/2;
        }
        return sqrt;
    }
}
