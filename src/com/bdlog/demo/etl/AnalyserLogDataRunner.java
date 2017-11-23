package com.bdlog.demo.etl;


import com.bdlog.demo.domain.EventLogConstants;
import com.bdlog.demo.utils.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.IOException;

public class AnalyserLogDataRunner implements Tool {
    private static final Logger logger = Logger.getLogger(AnalyserLogDataRunner.class);
    private Configuration mConfiguration = null;

    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(), new AnalyserLogDataRunner(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setConf(Configuration configuration) {
        configuration.set("fs.defaultFS", "hdfs://mini1:9000");
        configuration.set("yarn.resourcemanager.hostname", "mini1");
        configuration.set("hbase.zookeeper.quorum", "mini1,mini2,mini3");
//        System.setProperty("HADOOP_USER_NAME", "hadoop");
        this.mConfiguration = HBaseConfiguration.create(configuration);
    }

    @Override
    public Configuration getConf() {
        return this.mConfiguration;
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = this.getConf();
        procesArgs(configuration, args);
        Job job = Job.getInstance(configuration, "analyser_logdata");
        job.setJarByClass(AnalyserLogDataRunner.class);
        job.setMapperClass(AnalyserLogDataMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Put.class);
        //本地运行要求参数 addDependencyJars 为 false
        TableMapReduceUtil.initTableReducerJob(EventLogConstants.HBASE_NAME_EVENT_LOGS, null, job, null, null, null, null, false);
        job.setNumReduceTasks(0);
        setJobInputPaths(job);
        return job.waitForCompletion(true) ? 0 : -1;
    }

    /**
     * 设置job的输入路径
     *
     * @param job
     */
    private void setJobInputPaths(Job job) {
        Configuration configuration = job.getConfiguration();
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(configuration);
            String date = configuration.get(EventLogConstants.RUNNING_DATE_PARAMES);
            Path inputPath = new Path("/flume/" + TimeUtil.formatDate(date, "yyyy-MM-dd"));
            if (fileSystem.exists(inputPath)) {
                FileInputFormat.addInputPath(job, inputPath);
            } else {
                throw new RuntimeException("文件不存在：" + inputPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileSystem != null)
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 处理参数
     *
     * @param configuration
     * @param args
     */
    private void procesArgs(Configuration configuration, String[] args) {
        String date = null;
        for (int i = 0; i < args.length; i++) {
            if ("-d".equals(args[i])) {
                date = args[++i];
                break;
            }

        }
        //要求date格式为：yyyy-MM-dd
        if (StringUtils.isBlank(date) || !TimeUtil.isValidateRunningDate(date)) {
            //date是一个无效时间数据
            date = TimeUtil.getYesterday();//默认时间是昨天
            System.out.println(date);
        }
        configuration.set(EventLogConstants.RUNNING_DATE_PARAMES, date);
    }
}
