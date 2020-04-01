#!/bin/bash

# This is a bit hardcoded, but it's meant as a proof of concept.

# used in kubectl get pods when targeting kafka broker pods
KARGS="-n kafka -l release=kafka,app=kafka"
# used in kubectl port-forward (setting the namespace, can be omitted)
KPORTFWD_ARGS="-n kafka"
# port on broker pods to forward
DPORT=9092


TMPRULES=$(mktemp iptablesdnat.XXXXXXX)

function finish {
  echo -n "Closing all port forwards.. "
  teardownDNAT
  rm $TMPRULES
  kill 0
  echo "Bye!"
}
trap finish EXIT

function setupDNAT {
  cat $TMPRULES | xargs -L1 iptables -A OUTPUT
}

function teardownDNAT {
  cat $TMPRULES | xargs -L1 iptables -D OUTPUT
}

function getPods {
  oc get pods $KARGS -o go-template --template="{{range .items}}{{.metadata.name}}:{{.status.podIP}} {{end}}"
}

function init {
  N=0
  for line in $(getPods); do
    IFS=: read POD IP <<<$line
    PORT=$((9092 + $N))
    let "N++"

    (while true; do
      oc port-forward $KPORTFWD_ARGS $POD $PORT:$DPORT
      echo "Restarting portfwd to $POD..."
      sleep 1
    done) &

    echo "-t nat -p tcp -d $IP --dport $DPORT -j DNAT --to-destination 127.0.0.1:$PORT" >> $TMPRULES
  done
}


init
setupDNAT

wait

