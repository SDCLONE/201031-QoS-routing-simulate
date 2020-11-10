#BEGIN表明这是程序开头执行的一段语句，且只执行一次。

BEGIN {
    #程序初始化，设定一变量以记录目前处理的封包的最大ID号码。在awk环境下变量的使用不需要声明，直接赋值。
    highest_uid = 0;
}

#下面大括号里面的内容会针对要进行处理的记录（也就是我们的trace文件）的每一行都重复执行一次

{
    #获取对应的字段信息
    action = $1;
    time = $2;
    packet_uid = $6;
    routing_type = $7;

    # 如果是发的包（只有一行）
    if (action == "s" && routing_type == "IQRRL") {
        #将时间存储进start_time数组，索引是packet_uid
        start_time[packet_uid] = time;
    } 
    # 如果是收的包（可能有多行）
    else if (action == "r" && routing_type == "IQRRL") {
        # 判断包的id是否最大
        if (packet_uid > highest_uid) {
            highest_uid = packet_uid;
        }
        #将时间存储进end_time数组，索引是packet_uid
        end_time[packet_uid] = time;
    }
}

#END表明这是程序结束前执行的语句，也只执行一次

END {
    #用于统计平均时间的两个变量
    total_time = 0;
    count = 0;

    #当每行资料都读取完毕后，开始计算有效封包的端到端延迟时间。
    for (i = 0; i <= highest_uid; i++) {
        start = start_time[i];
        end = end_time[i];
        packet_duration = end - start;
        if (start < end) {
            total_time += packet_duration;
            count++;
            printf("%d %f %f %f\n", i, start, end, packet_duration);
        }
    }
    average_delay = total_time / count;
    printf("%f", average_delay);
}
