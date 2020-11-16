BEGIN	{
  total_pkt_size = 0;
  total_time_offset = 0.0;

  # 用于阶段性统计的变量
  start_time_offset = 0.0;  # 设置开始统计的时间
  # end_time_offset = 0.0;
  period_pkt_size = 0;  # 用于记录一个统计间隔内的吞吐量
  period_interval_count = 0;  # 用于记录统计间隔数
  period_interval = 0.5;  # 设置一个统计间隔时长
}

{
  action = $1;
  time = $2;
  pkt_size = 0;
  for (i = 1; i <= NF; i++) {
    if ($i ~ /length:/) {
      pkt_size = $(i+1);
    }
  }
  if (action == "r" && pkt_size > 0) {
    total_pkt_size += pkt_size;
    total_time_offset = time;

    # 需要记录间隔
    if (total_time_offset - start_time_offset >= period_interval) {
      period_total_throughput[period_interval_count] = period_pkt_size;
      period_interval_count++;
      period_pkt_size = 0;
      start_time_offset += period_interval;
    }
    # 不需要记录间隔且在有效记录周期内
    else if(total_time_offset > start_time_offset){
      period_pkt_size += pkt_size;
    }
  }
  

}

END	{
  for (i = 0; i < period_interval_count; i++) {
    period_throughput_rate = period_total_throughput[i] / period_interval_count;
    printf("interval%d %d %f\n", i, period_total_throughput[i], period_throughput_rate);
  }
  # 最后一行打印总吞吐率和总时间
  total_throughput_rate = total_pkt_size / total_time_offset;
  printf("%f %f", total_throughput_rate, total_time_offset);
}