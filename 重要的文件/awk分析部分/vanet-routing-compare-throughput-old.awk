
BEGIN {  
 
init=0;  
 
#cnt = 0;
 
FS="[() \t]";#field seperator is ')' or'('or ' '
 
myScrIP = "10.1.1.1";#This is the link that we pay attention to
 
myDstIP = "10.1.7.1"
 
}  
 
{
 
action = $1;
 
time = $2;
 
namespace=$3;
 
 
 
for (i=1;i<=NF;i++)#find packet ID
 
 
 
if ($i ~ /id/) #if $i field matches "id"
 
           myPacketID = $(i+1);#record the id of the packet for future use
 
    else if ($i ~ /length:/) #if $i field matches "length:"
 
           myLength =  $(i+1);#record the length of the packet for future use
 
else if ($i ~ />/) #if $i field matches ">"
 
{
 
             srcIP = $(i-1);
 
             dstIP = $(i+1);
 
            # if(match(srcIP, myScrIP) && match(dstIP, myDstIP) )#link matches
 
            # {      
 
                packet_id = myPacketID;
 
                pktsize = myLength;
 
                #record send time of the packet
 
                if (start_time[srcIP, packet_id]==0)
 
                {
 
                   start_time[srcIP, packet_id]=time;
 
                }
 
                if (action=="r")
 
                {
 
                     if(end_time_packet[srcIP, packet_id] ==0 )#the first time we receive this packet
 
                    {
 
 
 
                        end_time_packet[srcIP, packet_id] = time;#record time according to id
 
                        packetCNT[srcIP, packet_id] = CNT[srcIP];
 
                        pkt_byte_sum[srcIP, CNT[srcIP]+1]=pkt_byte_sum[srcIP, CNT[srcIP]]+ pktsize;   
 
                        end_time[srcIP, CNT[srcIP]]=time;
                       
 
                        CNT[srcIP]++;
 
                   }#if(end_time_packet[packet_id] ==0 )                                         else#not the 1st time we receive this packet,so we update receive time
 
                    {
 
                    #printf("*****duplicate packetID: %s,cnt=%s,end_time_old=%s,end_time new: %s\n",packet_id,cnt,end_time[packetCNT[packet_id]], time);
 
                      end_time[srcIP, packetCNT[srcIP, packet_id]]=time;
                      
                  }
 
                 #if (action=="r")
 
            #}#if match(srcIP, myScrIP)
 
      }#else if ($i ~ />/) #if $i field matches ">"
 
  }#for (i=1;i<=NF;i++)#find packet ID
 
}  



END {  
 
       

        total_throughput = 0;
        count = 0;
        for(idx in CNT){
            du_time = end_time[idx, CNT[idx]-1] - start_time[idx,0];
            if(du_time != 0){
            throughput = (pkt_byte_sum[idx, CNT[idx]] /du_time )*8/1000;
            total_throughput = total_throughput + throughput;
            count++;
             
             printf("%s %s %f\n", end_time[idx, CNT[idx]-1], throughput, du_time);  
            }
           
            
         }
       result = 0;
       if(count != 0)
       result = total_throughput / count;
        printf("%f\n", result);
 
}
