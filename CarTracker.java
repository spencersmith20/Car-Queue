import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Comparator;
import java.lang.Math;
import java.lang.IllegalArgumentException;

public class CarTracker{

  public static final int MAX_TYPES = 511;

  public static void main(String[] args) throws FileNotFoundException{

    // generate two general queues storing all cars basd on mileage and price
    CarQueue mq = new CarQueue(new Car.MileageOrder());
    CarQueue pq = new CarQueue(new Car.PriceOrder());

    // generate two queue arrays for each "type" (make + model) of car, for mileage and price
    // keep array entries null until needed to preserve memory
    CarQueue[] ptypes = new CarQueue[MAX_TYPES];
    CarQueue[] mtypes = new CarQueue[MAX_TYPES];

    // read file into system to add initial batch of cars
    File file = new File("cars2.txt");
    Scanner inFile = new Scanner(file);

    // skip header line
    inFile.nextLine();

    // attributes to be used throughout driver program
    String VIN, make, model, color; int price, mileage, k, n;

    while (inFile.hasNextLine()){

      // read info from file + split
      String line = inFile.nextLine();
      String[] items = line.split(":", 6);

      // get neccesary information from the list
      VIN = items[0];
      make = items[1];
      model = items[2];
      price = Integer.parseInt(items[3]);
      mileage = Integer.parseInt(items[4]);
      color = items[5];

      // add the car to each general queue
      mq.insert(VIN, make, model, price, mileage, color);
      pq.insert(VIN, make, model, price, mileage, color);

      // hash the make & model of the car and add to the queue at that index
      k = hash(make + model);

      // if type has not been used yet, instantiate
      if (ptypes[k] == null){
        ptypes[k] = new CarQueue(new Car.PriceOrder());
        mtypes[k] = new CarQueue(new Car.MileageOrder());
      }

      // now generated, add to the type queues
      ptypes[k].insert(VIN, make, model, price, mileage, color);
      mtypes[k].insert(VIN, make, model, price, mileage, color);
    }

    inFile.close();

    // begin using scanner for keyboard input`
    Scanner scanner = new Scanner(System.in);

    while(true){

      System.out.println("Enter a number to decide what to do:\n"
      + "(1) Add a car\n"
      + "(2) Update a car\n"
      + "(3) Remove a car from consideration\n"
      + "(4) Retrieve the lowest price car\n"
      + "(5) Retrieve the lowest mileage car\n"
      + "(6) Retrieve the lowest price car by make and model\n"
      + "(7) Retrieve the lowest mileage car by make and model");

      // get action number from user
      n = Integer.parseInt(scanner.nextLine());

      switch(n){

        case 1: // add a car

          System.out.print("Enter vehicle VIN: ");
          VIN = scanner.nextLine();

          System.out.print("Enter vehicle make: ");
          make = scanner.nextLine();

          System.out.print("Enter vehicle model: ");
          model = scanner.nextLine();

          System.out.print("Enter vehicle price: ");
          price = Integer.parseInt(scanner.nextLine());

          System.out.print("Enter vehicle mileage: ");
          mileage = Integer.parseInt(scanner.nextLine());

          System.out.print("Enter vehicle color: ");
          color = scanner.nextLine();

          // add to both general queues
          mq.insert(VIN, make, model, price, mileage, color);
          pq.insert(VIN, make, model, price, mileage, color);

          // add to types array at hashed index
          k = hash(make + model);

          // if type has not been used yet, instantiate
          if (ptypes[k] == null){
            ptypes[k] = new CarQueue(new Car.PriceOrder());
            mtypes[k] = new CarQueue(new Car.MileageOrder());
          }

          ptypes[k].insert(VIN, make, model, price, mileage, color);
          mtypes[k].insert(VIN, make, model, price, mileage, color);

          break;

        case 2: // update a car

          System.out.print("Enter VIN of vehicle to update: ");
          VIN = scanner.nextLine();

          // get index in types array based on VIN
          k = hash(mq.getType(VIN));

          if (mq.contains(VIN)){  // check for existence in PQ (both queue contents are equal)

            // display current car attributes
            System.out.println(mq.get(VIN));

            System.out.println("Would you like to update the (1) price, (2) mileage or (3) color of the car?");
            int m = Integer.parseInt(scanner.nextLine());

            switch(m){
              case 1:
                System.out.print("Enter updated price: ");
                price = Integer.parseInt(scanner.nextLine());

                // set new price in each queue
                mq.setPrice(VIN, price); pq.setPrice(VIN, price);
                ptypes[k].setPrice(VIN, price); mtypes[k].setPrice(VIN, price);
                break;

              case 2:
                System.out.print("Enter updated mileage: ");
                mileage = Integer.parseInt(scanner.nextLine());

                // set new mileage in both queues
                mq.setMileage(VIN, mileage); pq.setMileage(VIN, mileage);
                ptypes[k].setMileage(VIN, mileage); mtypes[k].setMileage(VIN, mileage);
                break;

              case 3:
                System.out.print("Enter updated color: ");
                color = scanner.nextLine();

                // set new color in both queues
                mq.setColor(VIN, color); pq.setColor(VIN, color);
                ptypes[k].setColor(VIN, color); mtypes[k].setColor(VIN, color);
                break;

              default:
                throw new IllegalArgumentException("Please enter 1, 2 or 3 to pick which spec to update");
            }
          }

          else{
            System.out.println("Vehicle not found!\n");
          }

          break;

        case 3: // remove a car
          System.out.print("Enter VIN of vehicle to be removed: ");
          VIN = scanner.nextLine();

          // get index in type array based on VIN
          k = hash(mq.getType(VIN));

          if (!mq.contains(VIN))
            System.out.println("Vehicle not found!\n");
          else{
            // remove from both queues
            System.out.println(mq.get(VIN));
            pq.remove(VIN); mq.remove(VIN);
            ptypes[k].remove(VIN); mtypes[k].remove(VIN);
          }

          break;

        case 4: // retrieve min from price queue
          System.out.println(pq.min().toString());
          break;

        case 5: // retrieve min from mileage queue
          System.out.println(mq.min().toString());
          break;

        case 6: // retrieve lowest price car by make and model
          System.out.print("Enter vehicle make: ");
          make = scanner.nextLine();

          System.out.print("Enter vehicle model: ");
          model = scanner.nextLine();

          // get type index
          k = hash(make + model);

          if (ptypes[k] == null)
            System.out.println("No " + make + " " + model + " vehicles found!\n");
          else
            System.out.println(ptypes[k].min().toString());

          break;

        case 7: // retrieve lowest mileage car by make and model
          System.out.print("Enter vehicle make: ");
          make = scanner.nextLine();

          System.out.print("Enter vehicle model: ");
          model = scanner.nextLine();

          // get type index
          k = hash(make + model);

          // check for existence of type
          if (mtypes[k] == null)
          System.out.println("No " + make + " " + model + " vehicles found!\n");
          else
            System.out.println(mtypes[k].min().toString());

          break;

        default: // end program
          System.out.println("Thanks for visiting, goodbye!");
          break;
      }
    }
  }

  // hash string to index in array of Queues
  public static int hash(String str){
    int n = str.length(); int hash = 0;

    // use Horner's method to hash the VIN string
    for (int i = 0; i < n; i++){
      int j = (int) str.charAt(n - (i + 1));
      hash += j; hash %= MAX_TYPES;
    }
    return hash % MAX_TYPES;
  }

}
