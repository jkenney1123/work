import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class Pair implements Writable,WritableComparable<Pair> {

    private IntWritable key;
    private IntWritable value1;

    public Pair(IntWritable key, IntWritable value1) {
        this.key = key;
        this.value1 = value1;
    }

    public Pair(int key, int value1) {
        this(new IntWritable(key),new IntWritable(value1));
    }

    public Pair() {
        this.key = new IntWritable();
        this.value1 = new IntWritable();
    }

    @Override
    public int compareTo(Pair other) {
        int returnVal = this.key.compareTo(other.getkey());
        if(returnVal != 0){
            return returnVal;
        }
        return this.value1.compareTo(other.getvalue1());
    }

    public static Pair read(DataInput in) throws IOException {
        Pair pair = new Pair();
        pair.readFields(in);
        return pair;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        key.write(out);
        value1.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        key.readFields(in);
        value1.readFields(in);
    }

    @Override
    public String toString() {
        return ""+key+""+","+value1+"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (value1 != null ? !value1.equals(pair.value1) : pair.value1 != null) return false;
        if (key != null ? !key.equals(pair.key) : pair.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 163 * result + (value1 != null ? value1.hashCode() : 0);
        return result;
    }

    public void setkey(int key){
        this.key.set(key);
    }
    public void setvalue1(int value1){
        this.value1.set(value1);
    }

    public IntWritable getkey() {
        return key;
    }

    public IntWritable getvalue1() {
        return value1;
    }
}