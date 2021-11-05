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
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class FriendsAverageAge extends Configured implements Tool{

	public static class MapClass extends Mapper<LongWritable, Text, Text, DoubleWritable>{
		
		private HashMap<String, String> user = new HashMap<String, String>();

		
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
				int temp = components[9].lastIndexOf("/");
				String temp1 = components[9].substring(temp +1);
				user.put(components[0], temp1);
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
				double sum = 0;
				double count = 0;

				for(int i = 0;i < parts.length;i++)
				{
					parts[i] = parts[i].replaceAll("\\W+","");
					if(parts[i].equals("")){
	                    continue;
	                }
					double age = Double.parseDouble(user.get(parts[i]));
					age = 2020 - age;
					sum = sum + age;
					count = count + 1;
					
				}
				if(count > 0)
				{
					double avg = sum/count;
					context.write(new Text(split1[0]), new DoubleWritable(avg));
				}
			}
			
		}
	}


	
	public int run(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException{
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf, "Average Age of Friends");	
		job.setJarByClass(FriendsAverageAge.class);
		
		Path in = new Path(args[0]);
		Path out = new Path(args[2]);
		
		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapperClass(MapClass.class);
		job.addCacheFile(new URI(args[1]));
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DoubleWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);
		
		System.exit(job.waitForCompletion(true)?0:1);
		
		return 0;
		
	}
	
	public static void main(String[] args) throws Exception{
		int res = ToolRunner.run(new Configuration(), new FriendsAverageAge(), args);
		System.exit(res);
	}

}
