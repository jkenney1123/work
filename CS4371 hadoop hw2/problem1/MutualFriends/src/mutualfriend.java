import java.io.IOException;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

//import mutualfriend.ReduceJoinReducer;

public class mutualfriend {
	public static class MutualMapper extends Mapper <LongWritable, Text, Pair, Text>
	{
		private Pair pair = new Pair();
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException 
				{
					String record = value.toString();
					String[] split1 = record.split("\t");
					if(split1.length > 1){
						String[] parts = split1[1].split(",");
						split1[0] = split1[0].replaceAll("\\W+","");
						int temp1 = 0;
						int temp2 = 0;

						for(int i = 0;i < parts.length;i++)
						{
							parts[i] = parts[i].replaceAll("\\W+","");
							if(parts[i].equals("")){
		                        continue;
		                    }
							temp1 = Integer.parseInt(split1[0]);
							temp2 = Integer.parseInt(parts[i]);
							if(temp1 > temp2)
							{
								pair.setkey(temp2);
								pair.setvalue1(temp1);
							}
							else
							{
								pair.setkey(temp1);
								pair.setvalue1(temp2);
							}
							context.write(pair, new Text(split1[1]));
						}
                    }
					
					
				}
	}
	
	public static class reducer extends Reducer <Pair, Text, Pair, Text>
	{
		public void reduce(Pair key, Iterable<Text> values, Context context)
		throws IOException, InterruptedException 
		{
			String commonFriends = "";
			String record = "";
			HashMap<String, Integer> H = new HashMap<String, Integer>();
			
			for (Text t : values) 
			{ 
				record = t.toString();
				String[] parts = record.split(",");
				for(int i = 0;i < parts.length;i++)
				{
					if(H.containsKey(parts[i]))
					{
						if(commonFriends == "")
						{
							commonFriends = commonFriends + parts[i];
						}
						else
						{
							commonFriends = commonFriends + "," + parts[i];
						}
					}
					else
					{
						H.put(parts[i], 1);
					}
				}
			}
			if(commonFriends != "")
			{
				//String str = String.format("%s", commonFriends);
				context.write(key,new Text(commonFriends));
			}
		}
	}

	public static void main(String[] args) throws IOException,InterruptedException,ClassNotFoundException {
		// TODO Auto-generated method stub
		Job job = Job.getInstance(new Configuration());
		job.setJarByClass(mutualfriend.class);
		job.setJobName("Mutual_Friend");

		FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
	
        job.setMapperClass(MutualMapper.class);
        job.setReducerClass(reducer.class);
        job.setNumReduceTasks(3);

        job.setOutputKeyClass(Pair.class);
        job.setOutputValueClass(Text.class);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
