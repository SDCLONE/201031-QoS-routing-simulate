
BEGIN {
 
highest_packet_id=0;
 
FS="[() \t]";#field seperator is ')' or ‘(’or ' '
 
myScrIP = "10.1.0.16";#This is the link that we pay attention to
 
myDstIP = "10.1.255.255"
 
}
 
{

action = $1;
 
time = $2;
 
namespace=$3;
 
for (i=1;i<=NF;i++)#find packet ID
{
   if ($i ~ /id/) #if $i field matches "id"
 
           myPacketID = $(i+1);#record the id of the packet for future use
 
   else if ($i ~ /^10.1./ && $(i+1) ~ />/) #if $i field matches ">"
   {
 
             srcIP = $i;
 
             dstIP = $(i+2);
            
            
             #if(match($(i-1), /^\d+\.\d+\.\d+\.\d+$/))#link matches
        
            #  {      
 
              #printf("%s:%s   %s > %s  %s\n",$1,$2,srcIP,dstIP, myPacketID);
 
            packet_id = myPacketID;
 
                #start to record the information of the packet
 
           if (packet_id>highest_packet_id)
           {
 
              highest_packet_id=packet_id;
 
                  #printf("*****highest_packet_id:  %s\n",highest_packet_id);
 
            }
 
#record send time of the packet
 
           if (start_time[srcIP, packet_id]==0)
            {
 
              start_time[srcIP, packet_id]=time;
 
              #printf("*****packetID: %s,start_time: %s\n",packet_id,time);
 
            }
 
           if (action=="r")
           {
 
              end_time[srcIP, packet_id]=time;
 
              #printf("*****packetID: %s,end_time: %s\n",packet_id,time);
 
           }
 
    #}#if
 
}#else if
  
}#for
 
}
 
END {     
    count = 0;
    result = 0;
    for(k in start_time){
         split(k, idx, SUBSEP);
         srcIP = idx[1];
         packet_id = idx[2];
         start = start_time[idx[1], idx[2]];
         end = end_time[idx[1], idx[2]];
         packet_duration = end-start;
         
        if ( start < end ){ 
             printf idx[1];
             printf(" %d %f %f %f\n", packet_id, start, end, packet_duration);
            result = result + packet_duration;
            count = count + 1;
        }
    }
    result = result / count;
#print the average delay in the last line
    printf("%f\n", result);
}

