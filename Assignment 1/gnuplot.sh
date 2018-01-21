#!/bin/bash

ylabel="Average delay"
xlabel="Utilization factor"
gnuplotdata="graph1.txt"
gnuscript="GNU_script_file.scr"

graph="ans1.ps"

#gnuplot  script generation
echo "set terminal postscript color" > $gnuscript
echo "set output \"$graph\" " >> $gnuscript
echo "set xlabel \"$xlabel\"" >> $gnuscript
echo "set ylabel \"$ylabel\"" >> $gnuscript
echo "plot \"$gnuplotdata\" using 2:1 with lines" >> $gnuscript
echo "quit" >> $gnuscript

#plotting
gnuplot $gnuscript





ylabel="Average delay"
xlabel="Utilization factor"
gnuplotdata="graph2.txt"
gnuscript="GNU_script_file.scr"

graph="ans2.ps"

#gnuplot  script generation
echo "set terminal postscript color" > $gnuscript
echo "set output \"$graph\" " >> $gnuscript
echo "set xlabel \"$xlabel\"" >> $gnuscript
echo "set ylabel \"$ylabel\"" >> $gnuscript
echo "plot \"$gnuplotdata\" using 2:1 with lines" >> $gnuscript
echo "quit" >> $gnuscript

#plotting
gnuplot $gnuscript
