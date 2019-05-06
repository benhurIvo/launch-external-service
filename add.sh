#!/bin/bash

# if no command line arg given
# set rental to Unknown
if [ -z $1 ]
then
  IN="*** Unknown command ***"
elif [ -n $1 ]
then
# otherwise take first arg
  IN=$1
fi

sm=0
IFS='&' read -ra ADDR <<< "$2"
for i in "${ADDR[@]}"; do
    b=`echo $i | grep -aob '=' | grep -oE '[0-9]+'`
    c=`echo ${i:$b+1}`
    if [ "$sm" == "0" ]
    then
      sm=$c
    else
#      sm=`echo $(( $sm + $c ))`
       case $IN in
            add)
                sm=`echo $(( $sm + $c ))`
          ;;
            multiply)
                sm=`echo $(( $sm * $c ))`
          ;;
            subtract)
                sm=`echo $(( $sm - $c ))`
          ;; 
            divide)
                sm=`echo $(( $sm / $c ))`
          ;;
            *)
                sm=`echo "Hmm, I can't do that."`
          ;;
    esac
    fi
done

echo $sm
