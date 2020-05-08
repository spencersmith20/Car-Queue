import java.util.Comparator;

public class Car {

  // fields
  private String VIN, make, model, color;
  private int price, mileage;

  // constructor, all inputs are user-obtained in driver method
  public Car(String VIN, String make, String model, int price, int mileage, String color){
    this.VIN = VIN; this.make = make;
    this.model = model; this.price = price;
    this.mileage = mileage; this.color = color;
  }

  // comparator classes to order by price and mileage
  public static class MileageOrder implements Comparator<Car>{
    public int compare(Car c, Car d){
      return Integer.compare(c.mileage, d.mileage);
    }
  }

  public static class PriceOrder implements Comparator<Car>{
    public int compare(Car c, Car d){
      return Integer.compare(c.price, d.price);
    }
  }

  public String toString(){
      return "\n" + color + " " + make + " " + model + "\nVIN: " + VIN
              + "\n" + mileage + " miles\n$" + price + "\n";
  }

  // setter methods
  public void setPrice(int price){
    this.price = price;
  }

  public void setMileage(int mileage){
    this.mileage = mileage;
  }

  public void setColor(String color){
    this.color = color;
  }

  public String getType(){
    return make + model;
  }
}
