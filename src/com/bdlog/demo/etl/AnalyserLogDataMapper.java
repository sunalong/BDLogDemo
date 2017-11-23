package com.bdlog.demo.etl;

import com.bdlog.demo.domain.EventLogConstants;
import com.bdlog.demo.utils.LoggerUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.zip.CRC32;

/**
 * 数据解析类
 * 读取hdfs中flume文件夹下的文件经过etl后输入到hbase中
 *
 * @author along
 * @date 2017/11/21
 */
public class AnalyserLogDataMapper extends Mapper<LongWritable, Text, NullWritable, Put> {
    private final Logger logger = Logger.getLogger(AnalyserLogDataMapper.class);
    private int inputRecords;//输入记录的条数
    private int outputRecords;//输出记录的条数
    private int filterRecords;//过滤失败日志的条数
    private byte[] family = Bytes.toBytes(EventLogConstants.EVENT_LOGS_FAMILY_NAME);
    private CRC32 crc32= new CRC32();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//        super.map(key, value, context);
        logger.debug(key + ":Analyse data of :" + value);
        inputRecords++;
        Map<String, String> clientInfo = LoggerUtil.handleLog(value.toString());
        if (clientInfo.isEmpty()) {
            filterRecords++;
            return;
        }
        //获取事件名称
        String eventAliasName = clientInfo.get(EventLogConstants.LOG_COLUMN_NAME_EVENT_NAME);
        EventLogConstants.EventEnum eventEnum = EventLogConstants.EventEnum.valueofAlias(eventAliasName);
        switch (eventEnum) {
            case Launch:
            case PAGE_VIEW:
            case CHARGE_REQUEST:
            case CHARGE_SUCCESS:
            case CHARGE_REFUND:
            case EVENT:
                handleData(clientInfo, eventEnum, context);
                break;
            default:
                filterRecords++;
                logger.warn("该事件没法进行解析，事件名称为：" + eventAliasName);
                break;
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
        logger.info("输入数据："+this.inputRecords+";输出数据："+this.outputRecords+";过滤数据："+this.filterRecords);
    }

    /**
     * 具体处理数据的方法
     *
     * @param clientInfo
     * @param eventEnum
     * @param context
     */
    private void handleData(Map<String, String> clientInfo, EventLogConstants.EventEnum eventEnum, Context context) throws IOException, InterruptedException {
        String uuid = clientInfo.get(EventLogConstants.LOG_column_Name_UUID);
        String memberId = clientInfo.get(EventLogConstants.LOG_COLUMN_NAME_MEMBER_ID);
        String serverTime = clientInfo.get(EventLogConstants.LOG_COLUMN_NAME_SERVER_TIME);
        if (StringUtils.isBlank(serverTime)) {
            this.filterRecords++;
        } else {
            clientInfo.remove(EventLogConstants.LOG_COLUMN_NAME_USER_AGENT);//去除浏览器信息
            String rowkey = generateRowKey(uuid, memberId, eventEnum.alias, serverTime);
            Put put = new Put(Bytes.toBytes(rowkey));
            for (Map.Entry<String, String> entry : clientInfo.entrySet()) {
                put.add(family, Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
            }
            context.write(NullWritable.get(), put);
            outputRecords++;
        }

    }

    /**
     * 根据uuid memberid servertime aliasName创建rowkey
     * @param uuid
     * @param memberId
     * @param alias
     * @param serverTime
     * @return
     */
    private String generateRowKey(String uuid, String memberId, String alias, String serverTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(serverTime).append("_");
        this.crc32.reset();
        this.crc32.update(uuid.getBytes());
        this.crc32.update(memberId.getBytes());
        this.crc32.update(alias.getBytes());
        sb.append(this.crc32.getValue()%100000000L);
        return sb.toString();
    }
}





















