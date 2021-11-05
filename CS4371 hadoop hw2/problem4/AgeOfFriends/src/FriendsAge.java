import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class FriendsAge extends Configured implements Tool{

public static class MapClass extends Mapper<LongWritable, Text, Pair, Text>{
		
		private HashMap<String, String[]> user = new HashMap<String, String[]>();
		private Pair pairs = new Pair();
		
		
		public void setup(Context context) throws IOException, InterruptedException{
			URI[] cacheFiles = context.getCacheFiles();
			for (URI path:cacheFiles){
				readCacheFile(path);
			}
		}
		
		private void readCacheFile(URI path) throws IOException{
			int last = path.toString().lastIndexOf("/");
			String filename = path.toString().substring(last+1);
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			while (reader.ready()){
				String line = reader.readLine();
				String[] components = line.split(",");
				String[] pair = new String[2];
				pair[0] = components[1];
				int temp = components[9].lastIndexOf("/");
				String temp1 = components[9].substring(temp +1);
				pair[1] = temp1;
				user.put(components[0], pair);
			}
			reader.close();
		}
		
		public void map(LongWritable key, Text value, Context context) 
				throws IOException, InterruptedException{
			String record = value.toString();
			String[] split1 = record.split("\t");
			if(split1.length > 1){
				String[] parts = split1[1].split(",");
				split1[0] = split1[0].replaceAll("\\W+","");

				for(int i = 0;i < parts.length;i++)
				{
					parts[i] = parts[i].replaceAll("\\W+","");
					if(parts[i].equals("")){
	                    continue;
	                }
					if(parts[i] != "")
					{
						int age = Integer.parseInt(user.get(parts[i])[1]);
						age = 2020 - age;
						pairs.setkey(Integer.parseInt(split1[0]));
						pairs.setvalue1(age);
						context.write(pairs, new Text(user.get(parts[i])[0]));
					}
				}
			}
			
		}
	}

	public static class PairPartitioner extends Partitioner<Pair,Text> {
	
	    @Override
	    public int getPartition(Pair pair, Text text, int numPartitions) {
	        //return wordPair.getWord().hashCode() % numPartitions;
	    	return (pair.getkey().hashCode() & Integer.MAX_VALUE) % numPartitions;
	    }
	}
	public static class PairComparator extends WritableComparator{
		public PairComparator()
		{
			super(Pair.class, true);
		}
		@Override
		public int compare(WritableComparable temp1, WritableComparable temp2) {
			return ((Pair) temp1).getkey().compareTo(((Pair) temp2).getkey());
		}
	}

	public static class reducer extends Reducer <Pair, Text, IntWritable, Text>
	{
		public void reduce(Pair pair, Iterable<Text> values, Context context)
		throws IOException, InterruptedException 
		{
			String value = "";
			String record = "";
			for (Text t : values) 
			{ 
				record = t.toString();
				if(record != "")
				{
					value = record + " " + pair.getvalue1().toString();
					context.write(pair.getkey(),new Text(value));
				}
			}
			
		}
	}


	
	public int run(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException{
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf, "Age of Friends");	
		job.setJarByClass(FriendsAge.class);
		
		Path in = new Path(args[0]);
		Path out = new Path(args[2]);
		
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapperClass(MapClass.class);
		job.addCacheFile(new URI(args[1]));
		
		job.setMapOutputKeyClass(Pair.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setPartitionerClass(PairPartitioner.class);
		
		job.setReducerClass(reducer.class);
		job.setGroupingComparatorClass(PairComparator.class);
		
        job.setNumReduceTasks(3);
		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		
		System.exit(job.waitForCompletion(true)?0:1);
		
		return 0;
		
	}
	
	public static void main(String[] args) throws Exception{
		int res = ToolRunner.run(new Configuration(), new FriendsAge(), args);
		System.exit(res);
	}

}
