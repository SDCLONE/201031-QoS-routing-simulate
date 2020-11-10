
# IQRRL routing agent settings
for {set i 0} {$i < $opt(nn)} {incr i} {
    $ns_ at 0.00002 "$ragent_($i) turnon"
    $ns_ at 0.5 "$ragent_($i) neighborlist"
#    $ns_ at 30.0 "$ragent_($i) turnoff"
}
#$ns_ at 11.0 "$ragent_(0) startSink 10.0"
$ns_ at 0.5 "$ragent_(100) startSink"


# IQRRL routing agent dumps
$ns_ at 25.0 "$ragent_(1) sinklist"


# 1 connecting to 2 at time 2.5568388786897245
#
set udp_(0) [new Agent/UDP]
$ns_ attach-agent $node_(120) $udp_(0)

set null_(0) [new Agent/Null]
$ns_ attach-agent $node_(100) $null_(0)

set cbr_(0) [new Application/Traffic/CBR]
$cbr_(0) set packetSize_ 512
$cbr_(0) set interval_ 1.0
$cbr_(0) set random_ 1
#$cbr_(0) set maxpkts_ 10000
$cbr_(0) attach-agent $udp_(0)
$ns_ connect $udp_(0) $null_(0)
$ns_ at 0.2 "$cbr_(0) start"
$ns_ at 300.0 "$cbr_(0) stop" 


