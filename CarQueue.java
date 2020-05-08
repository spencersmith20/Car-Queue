import java.util.NoSuchElementException;
import java.util.Comparator;

public class CarQueue{

  private static final int MAX_N = 1501;

  private Car[] keys;                 // holds cars
  private int[] queue;                // binary heap
  private int[] st;                   // queue[st[i]] = st[queue[i]] = i
  private int n;                      // number of items in the queue (zero-based)
  private Comparator<Car> comparator; // comparison technique used

  public CarQueue(Comparator<Car> comparator){

    // array initialization
    this.keys = new Car[MAX_N];
    this.st = new int[MAX_N];
    this.queue = new int[MAX_N];

    // if not in the array, st[i] = -1
    for (int i = 0; i < MAX_N; i++) st[i] = -1;

    this.n = 0;
    this.comparator = comparator;
  }

  // add a car based on the input attributes
  public void insert(String VIN, String make, String model, int price, int mileage, String color) {
      int i = hash(VIN);

      // at the hashed value in the hash table, put the current index in the queue
      queue[++n] = i;
      keys[i] = new Car(VIN, make, model, price, mileage, color);
      st[i] = n;

      swim(n);
  }

  public String getType(String VIN){
    int i = hash(VIN);
    return keys[i].getType();
  }

  public String get(String VIN){
    int i = hash(VIN);
    return keys[i].toString();
  }

  // remove entry with given VIN
  public void remove(String VIN){

    // get index from hash table
    int i = hash(VIN);

    // move last entry to this one and adjust
    exch(queue[i], n--);
    sink(queue[i]);
    swim(queue[i]);

    // remove last entry
    keys[i] = null;
    st[i] = -1;
  }

  // get minimum from the queue
  public Car min() {
      if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
      return keys[queue[1]];
  }

  // use comparator to compare the two Cars
  private boolean greater(int i, int j) {
      return comparator.compare(keys[queue[i]], keys[queue[j]]) > 0;
  }

  // setter methods that access a given Car's setter methods
  public void setPrice(String VIN, int price){
    int i = hash(VIN);
    keys[i].setPrice(price);
    swim(st[i]); sink(st[i]);
  }

  public void setMileage(String VIN, int mileage){
    int i = hash(VIN);
    keys[i].setMileage(mileage);
    swim(st[i]); sink(st[i]);
  }

  public void setColor(String VIN, String color){
    int i = hash(VIN);
    keys[i].setColor(color);
  }

 // is the queue empty?
 public boolean isEmpty() {
     return n == 0;
 }

 // check for existence of a given VIN
 public boolean contains(String VIN){
   int i = hash(VIN);
   return st[i] > -1;
 }

 // exchange two entries, given by key indices i & j
 private void exch(int i, int j) {

    Car swap1 = keys[i];
    keys[i] = keys[j];
    keys[j] = swap1;

    st[queue[i]] = j;
    st[queue[j]] = i;

    int swap2 = queue[i];
    queue[i] = queue[j];
    queue[j] = swap2;
 }

 // maintain heap property with respect to comparator
 private void swim(int k) {
     while (k > 1 && greater(k/2, k)) {
         exch(k, k/2);
         k = k/2;
     }
 }

private void sink(int k) {
    while (2*k <= n) {
        int j = 2*k;
        if (j < n && greater(j, j+1)) j++;
        if (!greater(k, j)) break;
        exch(k, j);
        k = j;
    }
}

 // hash VIN to index in symbol table
 private static int hash(String VIN){
   int n = VIN.length(); int hash = 0;

   // use Horner's method to hash the VIN string
   for (int i = 0; i < n; i++){
     int j = (int) VIN.charAt(n - (i + 1));
     hash += j; hash %= MAX_N;
   }
   return hash % MAX_N;
 }

}
