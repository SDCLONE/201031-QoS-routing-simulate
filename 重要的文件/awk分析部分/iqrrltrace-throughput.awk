BEGIN {
    highest_time_offset = 0.0;
    total_packet_size = 0.0;
    count = 0;
}

{
    action = $1;
    time = $2;
    routing_type = $7;
    packet_size_byte = $8;  #unit: B
    # packet_size_byte = packet_size_byte / 1024.0;  #unit: KB

    if (action == "r" && routing_type == "IQRRL") {
        if (time > highest_time_offset) {
            highest_time_offset = time;
        }
        total_packet_size += packet_size_byte;
        count++;
    }
}

END	{
    #单位 KB/s
    total_average_throughput = (total_packet_size / 1024.0) / highest_time_offset;
    printf("%f %f %d %f", total_packet_size, highest_time_offset, count, total_average_throughput);
}
